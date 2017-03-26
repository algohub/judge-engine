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
    final Pattern pattern = Pattern.compile("^solution\\.cpp:(\\d+):\\d+:");

    for(int i = 0; i < lines.length; i++) {
      Matcher matcher = pattern.matcher(lines[i]);
      if(matcher.find()) {
        String lineNo = matcher.group(1);
        sb.append("Line " + lines[i].substring(CPP_SOLUTION_FILE.length() + 1)).append('\n');
        sb.append(lines[i+1]).append('\n');
        sb.append(lines[i+2]).append('\n');
        i += 2;
      } else if(lines[i].startsWith("main.cpp: In function") && lines[i+1].contains("was not declared in this scope") ) {
        int pos = lines[i+1].indexOf("error:");
        sb.append(lines[i+1].substring(pos)).append('\n');
        i+= 1;
      }
    }
    return sb.toString();
  }
}
