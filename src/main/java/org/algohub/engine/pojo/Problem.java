package org.algohub.engine.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Map;
import org.algohub.engine.type.LanguageType;


/**
 * Problem Java Object, corresponds to the problem JSON string.
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.UnusedPrivateField", "PMD.SingularField",
    "PMD.ArrayIsStoredDirectly", "PMD.CommentRequired"})
@JsonIgnoreProperties(ignoreUnknown = true)
public class Problem {
  @JsonProperty(required = true) private final Map<String, String> title;
  @JsonProperty(required = true) private final Map<String, String> description;
  @JsonProperty(required = true) private final Function function;
  @JsonProperty(value = "test_cases", required = true) private final TestCase[] testCases;
  private final String category;
  private final String[] tags;
  private final int difficulty;
  @JsonProperty("time_limit") private final int timeLimit;
  @JsonProperty("memory_limit") private final int memoryLimit;
  @JsonProperty("related_problems") private final String[] relatedProblems;
  private final Map<String, String> author;
  @JsonProperty("test_cases_generator") private final String testCasesGenerator;

  /**
   * Since some fields are immutable, need to provide a method for Jackson.
   */
  @SuppressWarnings({"PMD.ExcessiveParameterList", "PMD.ShortVariable"}) @JsonCreator
  public Problem(
      @JsonProperty(value = "title", required = true) final Map<String, String> title,
      @JsonProperty(value = "description", required = true) final Map<String, String> description,
      @JsonProperty(value = "function", required = true) final Function function,
      @JsonProperty(value = "test_cases", required = true) final TestCase[] testCases,
      @JsonProperty("category") final String category,
      @JsonProperty("tags") final String[] tags,
      @JsonProperty("difficulty") final int difficulty,
      @JsonProperty("time_limit") final int timeLimit,
      @JsonProperty("memory_limit") final int memoryLimit,
      @JsonProperty("related_problems") final String[] relatedProblems,
      @JsonProperty("author") final Map<String, String> author,
      @JsonProperty("test_cases_generator") final String testCasesGenerator) {
    this.title = title;
    this.description = description;
    this.category = category;
    this.tags = tags;
    this.difficulty = difficulty;
    this.timeLimit = timeLimit;
    this.memoryLimit = memoryLimit;
    this.relatedProblems = relatedProblems;
    this.function = function;
    this.author = author;
    this.testCases = testCases;
    this.testCasesGenerator = testCasesGenerator;
  }

  /**
   * Test cases.
   */
  @JsonIgnoreProperties(ignoreUnknown = true) public static class TestCase {
    @JsonProperty(required = true) private final ArrayNode input;
    @JsonProperty(required = true) private final JsonNode output;

    @JsonCreator public TestCase(
        @JsonProperty(value = "input", required = true) final ArrayNode input,
        @JsonProperty(value = "output", required = true) final JsonNode output) {
      this.input = input;
      this.output = output;
    }

    public ArrayNode getInput() {
      return input;
    }

    public JsonNode getOutput() {
      return output;
    }
  }

  public String getId() {
    return this.title.get("en").replaceAll("\\s+", "-").replaceAll("['\\(\\),]+", "").toLowerCase();
  }

  public Map<String, String> getTitle() {
    return title;
  }

  public Map<String, String> getDescription() {
    return description;
  }

  public String getCategory() {
    return category;
  }

  public String[] getTags() {
    return tags;
  }

  public int getDifficulty() {
    return difficulty;
  }

  public int getTimeLimit() {
    return timeLimit;
  }

  public int getMemoryLimit() {
    return memoryLimit;
  }

  public String[] getRelatedProblems() {
    return relatedProblems;
  }

  public Function getFunction() {
    return function;
  }

  public Map<String, String> getAuthor() {
    return author;
  }

  public TestCase[] getTestCases() {
    return testCases;
  }

  public String getTestCasesGenerator() {
    return testCasesGenerator;
  }
}
