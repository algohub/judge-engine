package org.algohub.engine.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.algohub.engine.bo.StatusCode;


/**
 * Judge result.
 */
@SuppressWarnings("PMD.CommentRequired")
public final class JudgeResult {
  @JsonProperty(value = "status_code", required = true) private StatusCode statusCode;
  @JsonProperty("error_message") private String errorMessage;
  private ArrayNode input;
  private JsonNode output;
  @JsonProperty("expected_output") private JsonNode expectedOutput;
  @JsonProperty("testcase_passed_count") private int testcasePassedCount;
  @JsonProperty("testcase_total_count") private int testcaseTotalCount;
  @JsonProperty("elapsed_time") private long elapsedTime; // milliseconds
  @JsonProperty("consumed_memory") private long consumedMemory;  // bytes

  /**
   * Since some fields are immutable, need to provide a method for Jackson.
   */
  @JsonCreator public JudgeResult(@JsonProperty(value = "status_code", required = true) final StatusCode statusCode,
      @JsonProperty("error_message") final String errorMessage,
      @JsonProperty("input") final ArrayNode input, @JsonProperty("output") final JsonNode output,
      @JsonProperty("expected_output") final JsonNode expectedOutput,
      @JsonProperty("testcase_passed_count") final int testcasePassedCount,
      @JsonProperty("testcase_total_count") final int testcaseTotalCount,
      @JsonProperty("elapsed_time") final long elapsedTime,
      @JsonProperty("consumed_memory") final long consumedMemory) {
    this.statusCode = statusCode;
    this.errorMessage = errorMessage;
    this.input = input;
    this.output = output;
    this.expectedOutput = expectedOutput;
    this.testcasePassedCount = testcasePassedCount;
    this.testcaseTotalCount = testcaseTotalCount;
    this.elapsedTime = elapsedTime;
    this.consumedMemory = consumedMemory;
  }

  /**
   * Constructor.
   */
  public JudgeResult(final String compileErrorMsg) {
    this.statusCode = StatusCode.COMPILE_ERROR;
    this.errorMessage = compileErrorMsg;
  }

  // PENDING or RUNNING
  public JudgeResult(final StatusCode statusCode) {
    this.statusCode = statusCode;
  }

  public StatusCode getStatusCode() {
    return statusCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

  public ArrayNode getInput() {
    return input;
  }

  public JsonNode getOutput() {
    return output;
  }

  public JsonNode getExpectedOutput() {
    return expectedOutput;
  }

  public int getTestcasePassedCount() {
    return testcasePassedCount;
  }

  public int getTestcaseTotalCount() {
    return testcaseTotalCount;
  }

  public void setTestcaseTotalCount(int n) {
    this.testcaseTotalCount = n;
  }

  public long getElapsedTime() {
    return elapsedTime;
  }

  public long getConsumedMemory() {
    return consumedMemory;
  }

  public void setElapsedTime(long elapsedTime) {
    this.elapsedTime = elapsedTime;
  }
}
