package org.algohub.engine.judge;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;

/**
 * JavaScript language online judge.
 */
public class JavaScriptJudge implements JudgeInterface {
  //TODO:

  /**
   * {@inheritDoc}.
   */
  public JudgeResult judge(final Function function, final Problem.TestCase[] testCases,
      final String userCode) {
    return new JudgeResult(StatusCode.ACCEPTED.toInt(), null, null, null, null, 0, 0, 0, 0);
  }
}
