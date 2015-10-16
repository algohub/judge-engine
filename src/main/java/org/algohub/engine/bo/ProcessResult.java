package org.algohub.engine.bo;

/**
 * The result of a subprocess.
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.CommentRequired"})
public class ProcessResult {
  private int exitCode;
  private String stdout;
  private String stderr;

  public ProcessResult(final int exitCode, final String stdout, final String stderr) {
    this.exitCode = exitCode;
    this.stdout = stdout;
    this.stderr = stderr;
  }

  public int getExitCode() {
    return exitCode;
  }

  public String getStdout() {
    return stdout;
  }

  public String getStderr() {
    return stderr;
  }
}
