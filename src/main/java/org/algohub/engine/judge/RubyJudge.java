package org.algohub.engine.judge;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.algohub.engine.bo.ProcessResult;
import org.algohub.engine.codegenerator.RubyCodeGenerator;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.util.ObjectMapperInstance;

import java.io.File;
import java.io.IOException;
import org.algohub.engine.bo.StatusCode;


public class RubyJudge implements JudgeInterface {
  private static final String IMPORTS = "require 'set'\n\n\n";
  private static final int IMPORTS_LINES = 3;

  /**
   * @{inhericDoc}.
   */
  public JudgeResult judge(final Function function, final Problem.TestCase[] testCases,
      final String userCode) {
    try {
      final File tmpDir = Files.createTempDir();

      final File mainFile = new File(tmpDir, "main.rb");
      final String mainCode = RubyCodeGenerator.generateMain(function);
      Files.asCharSink(mainFile, Charsets.UTF_8).write(mainCode);

      final File solutionFile = new File(tmpDir, "solution.rb");
      Files.asCharSink(solutionFile, Charsets.UTF_8).write(IMPORTS + userCode);

      final long start = System.currentTimeMillis();
      final Process runCommand = new ProcessBuilder("ruby", mainFile.getAbsolutePath()).start();
      final ProcessResult runResult = JudgeInterface.runCommand(runCommand,
          ObjectMapperInstance.INSTANCE.writeValueAsString(testCases), TIMEOUT);
      final long time = System.currentTimeMillis() - start;

      final JudgeResult result;
      switch (runResult.getExitCode()) {
        case 0: { // ACCEPTED or WRONG_ANSWER
          final String lastLine;
          {
            final String[] splitted = runResult.getStdout().split("\n");
            lastLine = splitted[splitted.length - 1];
          }
          result = ObjectMapperInstance.INSTANCE.readValue(lastLine, JudgeResult.class);
          break;
        }
        case 1: { // Compile Error
          result = new JudgeResult(StatusCode.COMPILE_ERROR,
              createFriendlyMessage(runResult.getStderr()), null, null, null, 0, testCases.length,
              0, 0);
          break;
        }
        default: { // Other runtime errors
          result =
              new JudgeResult(StatusCode.RUNTIME_ERROR, runResult.getStderr(), null, null,
                  null, 0, testCases.length, time, 0);
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

    if (lines[0].contains("in `block in <main>': undefined method")) {
      final int pos = lines[0].indexOf("in `block in <main>': undefined method");
      return lines[0].substring(pos);
    }

    for (final String line : lines) {
      final int pos = line.indexOf("/solution.rb:");
      if (pos == -1) {
        continue;
      }

      final int numberStart = pos + "/solution.rb:".length();
      final int pos2 = line.indexOf(":in `", numberStart);
      if (pos2 == -1) {  // default to original
        sb.append(line).append('\n');
      } else {
        final int lineNumber = Integer.valueOf(line.substring(numberStart, pos2)) - IMPORTS_LINES;
        sb.append("Line " + lineNumber + ": " + line.substring(pos2 + 1)).append('\n');
      }
    }

    return sb.toString();
  }
}
