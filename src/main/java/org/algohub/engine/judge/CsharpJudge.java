package org.algohub.engine.judge;

import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Question;
import org.algohub.engine.pojo.Function;

/**
 * C# language online judge.
 */
public class CsharpJudge implements JudgeInterface {
  //TODO:

  /**
   * {@inheritDoc}.
   */
  public JudgeResult judge(final Function function, final Question.TestCase[] testCases,
      final String userCode) {
    return new JudgeResult(StatusCode.ACCEPTED.toInt(), null, null, null, null, 0, 0, 0, 0);
  }
}
