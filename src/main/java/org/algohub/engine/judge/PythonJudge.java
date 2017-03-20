package org.algohub.engine.judge;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.type.LanguageType;


public class PythonJudge implements JudgeInterface {
  private static final String IMPORTS = "import collections\nimport math\nfrom algohub import *\n\n\n";
  private static final int IMPORTS_LINES = 5;

  /**
   * @{inhericDoc}.
   */
  public JudgeResult judge(final Function function, final Problem.TestCase[] testCases,
      final String userCode) {
    return JudgeInterface.judge(function, testCases, IMPORTS + userCode, LanguageType.PYTHON,
        PythonJudge::createFriendlyMessage);
  }

  private static String createFriendlyMessage(final String errorMessage) {
    if(errorMessage == null || errorMessage.isEmpty()) return null;

    final StringBuilder sb = new StringBuilder();
    final String[] lines = errorMessage.split("\n");

    if(errorMessage.contains("AttributeError: module 'solution' has no attribute")) {
      return lines[3];
    } else if(lines[0].startsWith("Traceback (most recent call last):") && lines[3].startsWith("TypeError:") && lines[3].contains("required positional argument")) {
      return lines[3];
    }

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
