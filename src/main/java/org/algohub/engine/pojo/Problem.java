package org.algohub.engine.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.util.Map;



/**
 * Problem Java Object, corresponds to the problem JSON string.
 */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.UnusedPrivateField", "PMD.SingularField",
    "PMD.ArrayIsStoredDirectly", "PMD.CommentRequired"}) @JsonIgnoreProperties(ignoreUnknown = true)
public class Problem {
  @SuppressWarnings("PMD.ShortVariable") private final String id;
  private final Map<String, String> title;
  private final Map<String, String> description;
  private final String category;
  private final String[] tags;
  private final int difficulty;
  @JsonProperty("time_limit") private final int timeLimit;
  @JsonProperty("memory_limit") private final int memoryLimit;
  @JsonProperty("related_problems") private final String[] relatedProblems;
  private final Function function;
  private final Map<String, String> author;
  @JsonProperty("test_cases") private final TestCase[] testCases;
  @JsonProperty("test_cases_generator") private final String testCasesGenerator;

  /**
   * Since this class is immutable, need to provide a method for Jackson.
   */
  @SuppressWarnings({"PMD.ExcessiveParameterList", "PMD.ShortVariable"}) @JsonCreator
  public Problem(@JsonProperty("id") final String id,
      @JsonProperty("title") final Map<String, String> title,
      @JsonProperty("description") final Map<String, String> description,
      @JsonProperty("category") final String category,
      @JsonProperty("tags") final String[] tags,
      @JsonProperty("difficulty") final int difficulty,
      @JsonProperty("time_limit") final int timeLimit,
      @JsonProperty("memory_limit") final int memoryLimit,
      @JsonProperty("related_problems") final String[] relatedProblems,
      @JsonProperty("function") final Function function,
      @JsonProperty("author") final Map<String, String> author,
      @JsonProperty("test_cases") final TestCase[] testCases,
      @JsonProperty("test_cases_generator") final String testCasesGenerator) {
    this.id = id;
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
    private final ArrayNode input;
    private final JsonNode output;

    @JsonCreator public TestCase(@JsonProperty("input") final ArrayNode input,
        @JsonProperty("output") final JsonNode output) {
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
    return id;
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
