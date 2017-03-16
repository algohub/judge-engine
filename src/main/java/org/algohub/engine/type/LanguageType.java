package org.algohub.engine.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported Languages.
 */
public enum LanguageType {
  JAVASCRIPT("JavaScript"),
  JAVA("Java"),
  CPLUSPLUS("C++"),
  CSHARP("C#"),
  PYTHON("Python"),
  RUBY("Ruby");

  /**
   * Text for display.
   */
  private final String text;

  LanguageType(final String text) {
    this.text = text;
  }

  /**
   * Return real Enum from string.
   */
  @JsonCreator
  public static LanguageType fromString(String text) {
    if (text != null) {
      for (final LanguageType v : LanguageType.values()) {
        if (text.equalsIgnoreCase(v.text)) {
          return v;
        }
      }
    }
    throw new IllegalArgumentException("Unrecognized language: " + text);
  }

  /**
   * {@inheritDoc}.
   */
  @JsonValue
  @Override public String toString() {
    return text;
  }
}
