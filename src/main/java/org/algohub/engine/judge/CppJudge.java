package org.algohub.engine.judge;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    if(errorMessage == null || errorMessage.isEmpty()) return null;

    final StringBuilder sb = new StringBuilder();
    final String[] lines = errorMessage.split("\n");
    final String CPP_SOLUTION_FILE = "solution." + LanguageType.CPLUSPLUS.getFileSuffix();
    final Pattern pattern = Pattern.compile("solution\\.cpp:(\\d+):\\d+:");

    for(int i = 0; i < lines.length; i+=4) {
      if(lines[i].startsWith("In file included from main.cpp:") && lines[i+1].startsWith(CPP_SOLUTION_FILE)) {
        sb.append("Line " + lines[i+1].substring(CPP_SOLUTION_FILE.length() + 1)).append('\n');
        sb.append(lines[i+2]).append('\n');
        sb.append(lines[i+3]).append('\n');
      } else if(lines[i].startsWith("main.cpp: In function") && lines[i+1].contains("was not declared in this scope") ) {
        int pos = lines[i+1].indexOf("error:");
        sb.append(lines[i+1].substring(pos)).append('\n');
      }
    }
    return sb.toString();
  }
}
