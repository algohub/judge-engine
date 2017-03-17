package org.algohub.engine.judge;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.type.LanguageType;


/**
 * C++ language online judge.
 */
public class CppJudge implements JudgeInterface {
  /**
   * {@inheritDoc}.
   */
  public JudgeResult judge(final Function function, final Problem.TestCase[] testCases,
      final String userCode) {
    return JudgeInterface.judge(function, testCases, userCode, LanguageType.CPLUSPLUS,
        CppJudge::createFriendlyMessage);
  }

  private static String createFriendlyMessage(final String errorMessage) {
    final StringBuilder sb = new StringBuilder();
    final String[] lines = errorMessage.split("\n");
    final String CPP_SOLUTION_FILE = "solution." + LanguageType.CPLUSPLUS.getFileSuffix();

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
        final int pos1 = line.indexOf(CPP_SOLUTION_FILE + ": ");
        final String friendlyMessage;
        if (pos1 > 0) {
          friendlyMessage = line.substring(pos + CPP_SOLUTION_FILE.length() + 2);
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
