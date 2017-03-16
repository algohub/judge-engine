package org.algohub.engine.pojo;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.algohub.engine.type.LanguageType;

/**
 * Code written by users.
 */
@SuppressWarnings("PMD.CommentRequired")
public class Code {
  @JsonProperty(required = true) private final LanguageType language;
  @JsonProperty(required = true) private final String code;

  /**
   * Since some fields are immutable, need to provide a method for Jackson.
   */
  @JsonCreator
  public Code(
      @JsonProperty(value = "language", required = true) LanguageType language,
      @JsonProperty(value = "code", required = true) String code
  ) {
    this.language = language;
    this.code = code;
  }

  public LanguageType getLanguage() {
    return language;
  }

  public String getCode() {
    return code;
  }
}
