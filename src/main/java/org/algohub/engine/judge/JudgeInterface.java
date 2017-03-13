package org.algohub.engine.judge;

import org.algohub.engine.bo.ProcessResult;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.pojo.Function;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;


/**
 * Interface of all languages' judges.
 */
public interface JudgeInterface {
  long TIMEOUT = 20;  // 20 seconds
  String CPP_SOLUTION_FILE = "solution.h";

  /**
   * Read a text file.
   */
  static String readText(final InputStream inputStream) {
    try (final BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {

      final StringBuilder sb = new StringBuilder();
      for (String line = br.readLine(); line != null; line = br.readLine()) {
        sb.append(line);
        sb.append('\n');
      }

      if (sb.length() > 0) {
        // delete the last unnecessary newline
        sb.delete(sb.length() - 1, sb.length());
      }
      return sb.toString();
    } catch (IOException ex) {
      return "";
    }
  }

  /**
   * Run a shell command and get the normal output and error output.
   */
  static ProcessResult runCommand(final Process process, final String input, final long seconds)
      throws IOException, InterruptedException {
    if (input != null && !input.isEmpty()) {
      final BufferedWriter bw =
          new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
      bw.write(input);
      bw.flush();
      bw.close();
    }

    final String stdout = readText(process.getInputStream());
    final String stderr = readText(process.getErrorStream());
    final boolean succeeded = process.waitFor(seconds, TimeUnit.SECONDS);
    if (!succeeded) {
      process.destroy(); // timeout, kill the process
    }

    final int exitCode = succeeded ? process.exitValue() : 24;  // SIGXCPU

    return new ProcessResult(exitCode, stdout, stderr);
  }

  /**
   * Delete a directory recursively.
   *
   * @param path path
   * @throws IOException exception happens
   * @see http://stackoverflow.com/questions/779519/delete-directories-recursively-in-java
   */
  static void removeRecursive(final Path path) throws IOException {
    java.nio.file.Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
      @Override public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
          throws IOException {
        java.nio.file.Files.delete(file);
        return FileVisitResult.CONTINUE;
      }

      @Override public FileVisitResult visitFileFailed(final Path file, final IOException exc)
          throws IOException {
        // try to delete the file anyway, even if its attributes could not be read, since
        // delete-only access is theoretically possible
        java.nio.file.Files.delete(file);
        return FileVisitResult.CONTINUE;
      }

      @Override public FileVisitResult postVisitDirectory(final Path dir, final IOException exc)
          throws IOException {
        if (exc == null) {
          java.nio.file.Files.delete(dir);
          return FileVisitResult.CONTINUE;
        } else {
          // directory iteration failed; propagate exception
          throw exc;
        }
      }
    });
  }

  /**
   * Judge the code written by a user.
   *
   * @param function  Function type metadata.
   * @param testCases test cases
   * @param userCode  A function written by user.
   * @return If the output is identical with the test case, JudgeResult.succeed will be true,
   * otherwise, JudgeResult.succeed will be false and contain both output results.
   */
  JudgeResult judge(final Function function, final Problem.TestCase[] testCases,
      final String userCode) throws IOException, InterruptedException;
}
