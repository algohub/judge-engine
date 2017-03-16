package org.algohub.engine.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported Languages.
 */
public enum LanguageType {
  JAVA("Java"),
  PYTHON("Python"),
  CPLUSPLUS("C++"),

  CLOJURE("Clojure"),
  CSHARP("C#"),
  FSHARP("F#"),
  GO("Go"),
  HASKELL("Haskell"),
  JAVASCRIPT("JavaScript"),
  LUA("Lua"),
  OCAML("Ocaml"),
  RACKET("Racket"),
  RUBY("Ruby"),
  RUST("Rust"),
  SCALA("Scala"),
  SWIFT("Swift");

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

  public String getFileSuffix() {
    switch (this) {
      case CPLUSPLUS:
        return "cpp";
      case PYTHON:
        return "py";
      case CLOJURE:
        return "clj";
      case CSHARP:
        return "cs";
      case FSHARP:
        return "fs";
      case HASKELL:
        return "hs";
      case JAVASCRIPT:
        return "js";
      case OCAML:
        return "ml";
      case RACKET:
        return "rkt";
      case RUBY:
        return "rb";
      case RUST:
        return "rs";
      default:
        return text.toLowerCase();
    }
  }
}
