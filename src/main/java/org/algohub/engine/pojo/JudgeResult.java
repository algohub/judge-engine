package org.algohub.engine.pojo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.algohub.engine.judge.StatusCode;


/**
 * Judge result.
 */
@SuppressWarnings("PMD.CommentRequired")
public final class JudgeResult {
  @JsonProperty("status_code") private int statusCode;
  @JsonProperty("error_message") private String errorMessage;
  private String input;
  private String output;
  @JsonProperty("expected_output") private String expectedOutput;
  @JsonProperty("testcase_passed_count") private int testcasePassedCount;
  @JsonProperty("testcase_total_count") private int testcaseTotalCount;
  @JsonProperty("elapsed_time") private long elapsedTime; // milliseconds
  @JsonProperty("consumed_memory") private long consumedMemory;  // bytes

  /**
   * Since this class is immutable, need to provide a method for Jackson.
   */
  @JsonCreator public JudgeResult(@JsonProperty("status_code") final int statusCode,
      @JsonProperty("error_message") final String errorMessage,
      @JsonProperty("input") final String input, @JsonProperty("output") final String output,
      @JsonProperty("expected_output") final String expectedOutput,
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
    this.statusCode = StatusCode.COMPILE_ERROR.toInt();
    this.errorMessage = compileErrorMsg;
  }

  // PENDING or RUNNING
  public JudgeResult(final StatusCode statusCode) {
    this.statusCode = statusCode.toInt();
  }

  public int getStatusCode() {
    return statusCode;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public String getInput() {
    return input;
  }

  public String getOutput() {
    return output;
  }

  public String getExpectedOutput() {
    return expectedOutput;
  }

  public int getTestcasePassedCount() {
    return testcasePassedCount;
  }

  public int getTestcaseTotalCount() {
    return testcaseTotalCount;
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
