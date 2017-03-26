package org.algohub.engine.judge;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import java.io.File;
import java.util.function.UnaryOperator;
import org.algohub.engine.bo.ProcessResult;
import org.algohub.engine.codegenerator.CppCodeGenerator;
import org.algohub.engine.codegenerator.PythonCodeGenerator;
import org.algohub.engine.codegenerator.RubyCodeGenerator;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.bo.StatusCode;

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
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.util.ObjectMapperInstance;


/**
 * Interface of all languages' judges.
 */
public interface JudgeInterface {
  long TIMEOUT = 20;  // 20 seconds
  int N_TTY_BUF_SIZE = 4096;

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

  static JudgeResult runCode(final String[] command, String input, File cwd) {
    try {
      final Process runCommand = (new ProcessBuilder(command)).directory(cwd).start();
      final long start = System.currentTimeMillis();
      final ProcessResult runResult = runCommand(runCommand, input, TIMEOUT);
      final long time = System.currentTimeMillis() - start;

      final JudgeResult result;
      switch (runResult.getExitCode()) {
        case 0:  // ACCEPTED or WRONG_ANSWER
        case 11: { // Segmentation fault
          final String lastLine;
          {
            final String output = runResult.getExitCode() == 0 ? runResult.getStdout() :
                runResult.getStderr();
            final String[] splitted = output.split("\n");
            lastLine = splitted[splitted.length - 1];
          }
          result = ObjectMapperInstance.INSTANCE.readValue(lastLine, JudgeResult.class);
          break;
        }
        default: { // Other runtime errors
          result = new JudgeResult(StatusCode.RUNTIME_ERROR, runResult.getStderr(), null, null,
              null, 0, 0, time, 0);
        }
      }
      return result;
    } catch (IOException | InterruptedException ex) {
      return new JudgeResult(StatusCode.RUNTIME_ERROR,
          ex.getClass().getName() + ", " + ex.getMessage(), null, null, null, 0, 0,
          0, 0);
    }
  }

  /**
   * Delete a directory recursively.
   *
   * @param path path
   * @throws IOException exception happens
   * @see <a href="http://stackoverflow.com/questions/779519/delete-directories-recursively-in-java">Delete directories recursively in Java</a>
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

  /**
   * Judge the code written by a user.
   *
   * @param function  Function type metadata.
   * @param testCases test cases
   * @param userCode  A function written by user.
   * @param language  The programming language that userCode uses
   * @param createFriendlyMessage Create friendly message
   * @return If the output is identical with the test case, JudgeResult.succeed will be true,
   * otherwise, JudgeResult.succeed will be false and contain both output results.
   */
  static JudgeResult judge(final Function function, final Problem.TestCase[] testCases,
      final String userCode, final LanguageType language, UnaryOperator<String> createFriendlyMessage) {
    final File tmpDir = Files.createTempDir();
    try {
      final String testcasesText = ObjectMapperInstance.INSTANCE.writeValueAsString(testCases);
      final boolean fromFile = testcasesText.length() > N_TTY_BUF_SIZE;

      // write solution file
      String solutionFile = "solution." + language.getFileSuffix();
      Files.asCharSink(new File(tmpDir, solutionFile), Charsets.UTF_8).write(userCode);

      // write main file
      final String mainCode;
      final boolean needCompile;
      switch (language) {
        case CPLUSPLUS:
          needCompile = true;
          mainCode = CppCodeGenerator.generateMain(function, fromFile);
          break;
        case PYTHON:
          needCompile = false;
          mainCode = PythonCodeGenerator.generateMain(function, fromFile);
          break;
        case RUBY:
          needCompile = false;
          mainCode = RubyCodeGenerator.generateMain(function, fromFile);
          break;
        default:
          throw new IllegalArgumentException("Not supported language " + language);
      }
      final String mainFilename = "main." + language.getFileSuffix();
      Files.asCharSink(new File(tmpDir, mainFilename), Charsets.UTF_8).write(mainCode);

      // compile, will generate a executable file named "main"
      if(needCompile) {
        final String[] compileCommand;
        switch (language) {
          case CPLUSPLUS:
            compileCommand = new String[]{"g++", "--std=c++17", mainFilename, "-O3", "-o", "main"};
            break;
          default:
            throw new IllegalArgumentException("Not supported language " + language);
        }

        final Process compileProcess = (new ProcessBuilder(compileCommand)).directory(tmpDir).start();
        final ProcessResult compileResult = JudgeInterface.runCommand(compileProcess, null,
            TIMEOUT);
        if (compileResult.getExitCode() != 0) { // COMPILE_ERROR
          return new JudgeResult(StatusCode.COMPILE_ERROR,
              createFriendlyMessage.apply(compileResult.getStderr()), null, null, null, 0,
              testCases.length,
              0, 0);
        }
      }

      // run
      final String[] runCommand;
      if(needCompile) {
        runCommand = new String[]{"./main"};
      } else {
        switch (language) {
          case PYTHON:
            runCommand = new String[]{"python3", mainFilename};
            break;
          case RUBY:
            runCommand = new String[]{"ruby", mainFilename};
            break;
          default:
            throw new IllegalArgumentException("Not supported language " + language);
        }
      }
      final JudgeResult result;
      if(fromFile) {
        // write testcases to file if length > 4096
        Files.asCharSink(new File(tmpDir, "testcases.json"), Charsets.UTF_8).write(testcasesText);
        result = JudgeInterface.runCode(runCommand, null, tmpDir);
      } else {
        result = JudgeInterface.runCode(runCommand, testcasesText, tmpDir);
      }
      result.setTestcaseTotalCount(testCases.length);
      result.setErrorMessage(createFriendlyMessage.apply(result.getErrorMessage()));
      return result;
    } catch (IOException | InterruptedException ex) {
      return new JudgeResult(StatusCode.RUNTIME_ERROR,
          ex.getClass().getName() + ", " + ex.getMessage(), null, null, null, 0, testCases.length,
          0, 0);
    } finally {
      try {
        JudgeInterface.removeRecursive(tmpDir.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
