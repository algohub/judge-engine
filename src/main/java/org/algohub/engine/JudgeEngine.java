package org.algohub.engine;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.algohub.engine.judge.CppJudge;
import org.algohub.engine.judge.RubyJudge;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.judge.CsharpJudge;
import org.algohub.engine.judge.JavaJudge;
import org.algohub.engine.judge.JavaScriptJudge;
import org.algohub.engine.judge.PythonJudge;
import org.algohub.engine.bo.StatusCode;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.util.ObjectMapperInstance;

import java.io.File;
import java.io.IOException;

/**
 * The final judge compose of multiple languages' judges.
 */
public class JudgeEngine {
  /**
   * Java judge.
   */
  private final transient JavaJudge javaJudge = new JavaJudge();
  /**
   * JavaScript judge.
   */
  private final transient JavaScriptJudge javaScriptJudge = new JavaScriptJudge();
  /**
   * C++ judge.
   */
  private final transient CppJudge cppJudge = new CppJudge();
  /**
   * C# judge.
   */
  private final transient CsharpJudge csharpJudge = new CsharpJudge();
  /**
   * Python judge.
   */
  private final transient PythonJudge pythonJudge = new PythonJudge();
  /**
   * Ruby judge.
   */
  private final transient RubyJudge rubyJudge = new RubyJudge();

  /**
   * Entry point main function.
   */
  public static void main(final String[] args) throws IOException, InterruptedException {
    if (args.length != 3) {
      System.err.println("Usage: JudgeEngine problem.json language(PYTHON,RUBY) solution");
      return;
    }

    final String problemStr = Files.asCharSource(new File(args[0]), Charsets.UTF_8).read();
    final Problem problem = ObjectMapperInstance.INSTANCE.readValue(problemStr, Problem.class);
    final LanguageType languageType = LanguageType.valueOf(args[1]);
    final String userCode = Files.asCharSource(new File(args[2]), Charsets.UTF_8).read();

    final JudgeEngine judgeEngine = new JudgeEngine();
    final JudgeResult result = judgeEngine.judge(problem, userCode, languageType);

    if (result.getStatusCode() == StatusCode.ACCEPTED) {
      System.out.println("Accepted!");
    } else {
      System.err.println("Wrong Answer!\n" + result);
    }
  }

  /**
   * Judge the code written by a user.
   *
   * @param function     The function prototype declaration
   * @param testCases    test cases
   * @param userCode     A function implemented by user
   * @param languageType The language that used by the user
   * @return If the output is identical with the test case, JudgeResult.succeed will be true,
   * otherwise, JudgeResult.succeed will be false and contain both output results.
   */
  private JudgeResult judge(final Function function, final Problem.TestCase[] testCases,
      final String userCode, final LanguageType languageType) {
    switch (languageType) {
      case JAVA:
        return javaJudge.judge(function, testCases, userCode);
      case JAVASCRIPT:
        return javaScriptJudge.judge(function, testCases, userCode);
      case CPLUSPLUS:
        return cppJudge.judge(function, testCases, userCode);
      case CSHARP:
        return csharpJudge.judge(function, testCases, userCode);
      case PYTHON:
        return pythonJudge.judge(function, testCases, userCode);
      case RUBY:
        return rubyJudge.judge(function, testCases, userCode);
      default:
        throw new IllegalArgumentException("Unsupported language " + languageType.name());
    }
  }

  /**
   * Judge the code written by a user.
   *
   * @param problem     the problem description and test cases
   * @param userCode     the function written by user.
   * @param languageType the programming language
   * @return If the output is identical with the test case, JudgeResult.succeed will be true,
   * otherwise, JudgeResult.succeed will be false and contain both output results.
   */
  @SuppressWarnings({"PMD.AvoidInstantiatingObjectsInLoops"}) public JudgeResult judge(
      final Problem problem, final String userCode, final LanguageType languageType) {
    final Problem.TestCase[] testCases = problem.getTestCases();
    return judge(problem.getFunction(), testCases, userCode, languageType);

  }
}
