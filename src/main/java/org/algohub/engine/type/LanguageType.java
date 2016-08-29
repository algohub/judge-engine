package org.algohub.engine.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Supported Languages.
 */
public enum LanguageType {
  JAVASCRIPT,
  JAVA,
  CPLUSPLUS,
  CSHARP,
  PYTHON,
  RUBY;

  private static Map<String, LanguageType> nameToEnumMap = new HashMap<>(6);
  private static Map<LanguageType, String> enumToNameMap = new HashMap<>(6);

  static {
    nameToEnumMap.put("javascript", JAVASCRIPT);
    nameToEnumMap.put("java", JAVA);
    nameToEnumMap.put("cpp", CPLUSPLUS);
    nameToEnumMap.put("csharp", CSHARP);
    nameToEnumMap.put("python", PYTHON);
    nameToEnumMap.put("ruby", RUBY);

    enumToNameMap.put(JAVASCRIPT, "javascript");
    enumToNameMap.put(JAVA, "java");
    enumToNameMap.put(CPLUSPLUS, "cpp");
    enumToNameMap.put(CSHARP, "csharp#");
    enumToNameMap.put(PYTHON, "python");
    enumToNameMap.put(RUBY, "ruby");
  }

  @JsonCreator
  public static LanguageType fromValue(String value) {
    return nameToEnumMap.get(value);
  }

  @JsonValue
  public String toValue() {
    return enumToNameMap.get(this);
  }
}
