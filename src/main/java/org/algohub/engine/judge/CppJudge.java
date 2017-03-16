package org.algohub.engine.judge;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.algohub.engine.bo.ProcessResult;
import org.algohub.engine.codegenerator.CppCodeGenerator;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.util.ObjectMapperInstance;

import java.io.File;
import java.io.IOException;
import org.algohub.engine.bo.StatusCode;


/**
 * C++ language online judge.
 */
public class CppJudge implements JudgeInterface {
  private static final long TIMEOUT = 20;  // 20 seconds

  /**
   * {@inheritDoc}.
   */
  public JudgeResult judge(final Function function, final Problem.TestCase[] testCases,
      final String userCode) {
    try {
      final File tmpDir = Files.createTempDir();
      final String exeFilePath = new File(tmpDir, "main.exe").getAbsolutePath();

      // write solution header file
      Files.asCharSink(new File(tmpDir, CPP_SOLUTION_FILE), Charsets.UTF_8).write(userCode);
      // write main cpp file
      final String mainCpp = CppCodeGenerator.generateMain(function);
      final File mainFile = new File(tmpDir, "main.cpp");
      Files.asCharSink(mainFile, Charsets.UTF_8).write(mainCpp);

      // compile
      final Process compileCommand =
          new ProcessBuilder("g++", "--std=c++17", mainFile.getAbsolutePath(), "-O3", "-o",
              exeFilePath).start();
      final ProcessResult compileResult = JudgeInterface.runCommand(compileCommand, null, TIMEOUT);

      final JudgeResult result;

      if (compileResult.getExitCode() != 0) { // COMPILE_ERROR
        result = new JudgeResult(StatusCode.COMPILE_ERROR,
            createFriendlyMessage(compileResult.getStderr()), null, null, null, 0, testCases.length,
            0, 0);
      } else { // run
        final long start = System.currentTimeMillis();
        final Process runCommand = new ProcessBuilder(exeFilePath).start();
        final ProcessResult runResult = JudgeInterface.runCommand(runCommand,
            ObjectMapperInstance.INSTANCE.writeValueAsString(testCases), TIMEOUT);
        final long time = System.currentTimeMillis() - start;

        switch (runResult.getExitCode()) {
          case 0:  // ACCEPTED or WRONG_ANSWER
          case 11: { // Segmentation fault
            final String lastLine;
            {
              final String output =
                  runResult.getExitCode() == 0 ? runResult.getStdout() : runResult.getStderr();
              final String[] splitted = output.split("\n");
              lastLine = splitted[splitted.length - 1];
            }
            result = ObjectMapperInstance.INSTANCE.readValue(lastLine, JudgeResult.class);
            break;
          }
          default: { // Other runtime errors
            result =
                new JudgeResult(StatusCode.RUNTIME_ERROR, runResult.getStderr(), null, null,
                    null, 0, testCases.length, time, 0);
          }
        }
      }
      JudgeInterface.removeRecursive(tmpDir.toPath());
      return result;
    } catch (IOException | InterruptedException ex) {
      return new JudgeResult(StatusCode.RUNTIME_ERROR,
          ex.getClass().getName() + ", " + ex.getMessage(), null, null, null, 0, testCases.length,
          0, 0);
    }
  }

  private String createFriendlyMessage(final String errorMessage) {
    final StringBuilder sb = new StringBuilder();
    final String[] lines = errorMessage.split("\n");

    int startLine = 0;
    for (final String line : lines) {
      final int pos = line.indexOf(CPP_SOLUTION_FILE);
      if (pos > 0) {
        break;
      }
      startLine++;
    }
    if (startLine == lines.length) { // default to original
      return errorMessage;
    }
    for (int i = startLine; i < lines.length; ++i) {
      final String line = lines[i];
      final int pos = line.indexOf(CPP_SOLUTION_FILE);
      if (pos > 0) {
        final int pos1 = line.indexOf("solution.h: ");
        final String friendlyMessage;
        if (pos1 > 0) {
          friendlyMessage = line.substring(pos + "solution.h: ".length());
        } else {
          friendlyMessage = "Line" + line.substring(pos + CPP_SOLUTION_FILE.length());
        }
        sb.append(friendlyMessage).append('\n');
      } else {
        sb.append(line).append('\n');
      }
    }

    return sb.toString();
  }
}
