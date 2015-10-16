package org.algohub.engine.judge;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.algohub.engine.bo.ProcessResult;
import org.algohub.engine.codegenerator.PythonCodeGenerator;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Question;
import org.algohub.engine.util.ObjectMapperInstance;

import java.io.File;
import java.io.IOException;


public class PythonJudge implements JudgeInterface {
  private static final String IMPORTS = "import collections\n\n\n";
  private static final int IMPORTS_LINES = 3;

  /**
   * @{inhericDoc}.
   */
  public JudgeResult judge(final Function function, final Question.TestCase[] testCases,
      final String userCode) {
    try {
      final File tmpDir = Files.createTempDir();

      final File mainFile = new File(tmpDir, "main.py");
      final String mainCode = PythonCodeGenerator.generateMain(function);
      Files.asCharSink(mainFile, Charsets.UTF_8).write(mainCode);

      final File solutionFile = new File(tmpDir, "solution.py");
      Files.asCharSink(solutionFile, Charsets.UTF_8).write(IMPORTS + userCode);

      final long start = System.currentTimeMillis();
      final Process runCommand = new ProcessBuilder("python3", mainFile.getAbsolutePath()).start();
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
          result = new JudgeResult(StatusCode.COMPILE_ERROR.toInt(),
              createFriendlyMessage(runResult.getStderr()), null, null, null, 0, testCases.length,
              0, 0);
          break;
        }
        default: { // Other runtime errors
          result =
              new JudgeResult(StatusCode.RUNTIME_ERROR.toInt(), runResult.getStderr(), null, null,
                  null, 0, testCases.length, time, 0);
        }
      }
      JudgeInterface.removeRecursive(tmpDir.toPath());
      return result;
    } catch (IOException | InterruptedException ex) {
      return new JudgeResult(StatusCode.RUNTIME_ERROR.toInt(),
          ex.getClass().getName() + ", " + ex.getMessage(), null, null, null, 0, testCases.length,
          0, 0);
    }
  }

  private String createFriendlyMessage(final String errorMessage) {
    final StringBuilder sb = new StringBuilder();
    final String[] lines = errorMessage.split("\n");

    int startLine = 0;
    for (final String line : lines) {
      final int pos = line.indexOf("solution.py");
      if (pos > 0) {
        break;
      }
      startLine++;
    }
    for (int i = startLine; i < lines.length; ++i) {
      final String line = lines[i];
      final String uniqueStr = "solution.py\", line ";
      final int pos = line.indexOf(uniqueStr);
      if (pos > 0) {
        final int pos2 = line.indexOf(", ", pos + uniqueStr.length());
        final int lineNumber =
            Integer.parseInt(line.substring(pos + uniqueStr.length(), pos2)) - IMPORTS_LINES;
        final String friendlyMessage = "Line " + lineNumber + ":" + line.substring(pos2 + 1);

        sb.append(friendlyMessage).append('\n');
      } else {
        sb.append(line).append('\n');
      }
    }

    return sb.toString();
  }
}
