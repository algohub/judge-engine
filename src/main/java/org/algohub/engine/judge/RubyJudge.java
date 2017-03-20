package org.algohub.engine.judge;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.type.LanguageType;


public class RubyJudge implements JudgeInterface {
  private static final String IMPORTS = "require 'set'\nrequire 'algohub'\nLinkedListNode = Algohub::LinkedListNode\nBinaryTreeNode = Algohub::BinaryTreeNode\n\n\n";
  private static final int IMPORTS_LINES = 6;

  /**
   * @{inhericDoc}.
   */
  public JudgeResult judge(final Function function, final Problem.TestCase[] testCases,
      final String userCode) {
    return JudgeInterface.judge(function, testCases, IMPORTS + userCode, LanguageType.RUBY,
        RubyJudge::createFriendlyMessage);
  }

  private static String createFriendlyMessage(final String errorMessage) {
    if(errorMessage == null || errorMessage.isEmpty()) return null;

    final StringBuilder sb = new StringBuilder();
    final String[] lines = errorMessage.split("\n");

    if (errorMessage.contains("for main:Object (NoMethodError)")) {
      final int pos = lines[0].indexOf("undefined method");
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
