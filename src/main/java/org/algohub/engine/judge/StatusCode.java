package org.algohub.engine.judge;

/**
 * Status code.
 */
public enum StatusCode {
  PENDING(0),
  RUNNING(1),

  ACCEPTED(2),
  WRONG_ANSWER(3),

  COMPILE_ERROR(4),

  RUNTIME_ERROR(5),
  TIME_LIMIT_EXCEEDED(6),
  MEMORY_LIMIT_EXCEEDED(7),
  OUTPUT_LIMIT_EXCEEDED(8),

  RESTRICTED_CALL(9),
  TOO_LONG_CODE(10);


  private final int code;

  private StatusCode(int code) {
    this.code = code;
  }

  public int toInt() {
    return this.code;
  }
}
