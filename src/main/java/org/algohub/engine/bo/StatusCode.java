package org.algohub.engine.bo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Status code.
 */
public enum StatusCode {
  PENDING(0),
  RUNNING(1),

  COMPILE_ERROR(2),
  RUNTIME_ERROR(3),

  ACCEPTED(4),
  WRONG_ANSWER(5),

  TIME_LIMIT_EXCEEDED(6),
  MEMORY_LIMIT_EXCEEDED(7),
  OUTPUT_LIMIT_EXCEEDED(8),

  RESTRICTED_CALL(9),
  TOO_LONG_CODE(10);


  private final int code;

  StatusCode(int code) {
    this.code = code;
  }

  @JsonCreator
  public static StatusCode fromInt(int code) {
    for (final StatusCode v : StatusCode.values()) {
      if (code == v.code) {
        return v;
      }
    }

    throw new IllegalArgumentException("Unrecognized status code: " + code);
  }

  @JsonValue
  public int toInt() {
    return this.code;
  }
}
