package org.algohub.engine.judge;

/**
 * Status code.
 */
public enum StatusCode {
  PENDING(0),
  RUNNING(1),
  FINISHED(2),

  COMPILE_ERROR(3),

  ACCEPTED(4),
  WRONG_ANSWER(5),

  RUNTIME_ERROR(6),
  TIME_LIMIT_EXCEEDED(7),
  MEMORY_LIMIT_EXCEEDED(8),
  OUTPUT_LIMIT_EXCEEDED(9),

  RESTRICTED_CALL(10),
  TOO_LONG_CODE(11);


  private final int code;

  private StatusCode(int code) {
    this.code = code;
  }

  public int toInt() {
    return this.code;
  }
}
