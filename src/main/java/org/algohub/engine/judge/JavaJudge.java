package org.algohub.engine.judge;

import com.fasterxml.jackson.databind.JsonNode;
import org.algohub.engine.bo.InternalTestCase;
import org.algohub.engine.compiler.java.CompileErrorException;
import org.algohub.engine.compiler.java.MemoryJavaCompiler;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.serde.Serializer;
import org.algohub.engine.type.TypeNode;
import org.algohub.engine.util.Equals;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.bo.StatusCode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Java language online judge.
 */
public class JavaJudge implements JudgeInterface {
  // for finding class name
  private static final Pattern[] PATTERNS =
      new Pattern[] {Pattern.compile("public\\s+class\\s+(\\w+)\\s+"),
          Pattern.compile("final\\s+public\\s+class\\s+(\\w+)\\s+"),
          Pattern.compile("public\\s+final\\s+class\\s+(\\w+)\\s+"),};

  private static final String PACKAGE_NAME = "org.algohub";
  private static final String IMPORTS =
      "package " + PACKAGE_NAME + ";\n" + "import java.util.*;\n" +
          "import org.algohub.engine.collection.*;\n\n\n";
  private static final int IMPORTS_LINES = 5;

  private static JudgeResult judge(final Object clazz, final Method method,
      final InternalTestCase[] testCases, final Problem.TestCase[] testCasesJson,
      final TypeNode returnType) {
    final long start = System.currentTimeMillis();
    for (int i = 0; i < testCases.length; ++i) {
      final InternalTestCase testCase = testCases[i];
      final JudgeOneCaseResult oneResult = judge(clazz, method, testCase, returnType);
      if (!oneResult.correct) {
        final long time = System.currentTimeMillis() - start;
        return new JudgeResult(StatusCode.WRONG_ANSWER, null,
            testCasesJson[i].getInput(), oneResult.wrongOutput,
            testCasesJson[i].getOutput(), i,
            testCases.length, time, 0L);
      }
    }
    final long time = System.currentTimeMillis() - start;
    return new JudgeResult(StatusCode.ACCEPTED, null, null, null,
        null, testCases.length, testCases.length, time, 0);
  }

  private static JudgeOneCaseResult judge(final Object clazz, final Method method,
      final InternalTestCase testCase, final TypeNode returnType) {
    final Object output;
    try {
      output = method.invoke(clazz, testCase.getInput());
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new IllegalStateException(e.getMessage());
    }
    final JudgeOneCaseResult result = new JudgeOneCaseResult();
    if (Equals.equal(testCase.getOutput(), output)) {
      result.correct = true;
    } else {
      result.wrongOutput = Serializer.toJson(output, returnType);
    }
    return result;
  }

  private static Optional<String> getClassName(final String javaCode) {
    for (final Pattern pattern : PATTERNS) {
      final Matcher matcher = pattern.matcher(javaCode);
      if (matcher.find()) {
        return Optional.of(matcher.group(1));
      }
    }
    return Optional.empty();
  }

  /**
   * {@inheritDoc}.
   */
  public JudgeResult judge(final Function function, final Problem.TestCase[] testCases,
      final String userCode) {
    final InternalTestCase[] internalTestCases = new InternalTestCase[testCases.length];
    for (int i = 0; i < testCases.length; ++i) {
      internalTestCases[i] = new InternalTestCase(testCases[i], function);
    }
    try {
      return judge(function, internalTestCases, userCode, testCases);
    } catch (NoSuchMethodError ex) {
      return new JudgeResult(ex.getClass().getCanonicalName() + ": " + ex.getMessage());
    }
  }

  /**
   * Judge a user's code.
   *
   * @param function      Function prototype
   * @param testCases     test cases
   * @param userCode      user code
   * @param testCasesJson test cases in JSON format
   * @return JudgeResult
   */
  public JudgeResult judge(final Function function, final InternalTestCase[] testCases,
      final String userCode, final Problem.TestCase[] testCasesJson) {
    final Object clazz;
    final Method method;
    try {
      final String completeUserCode = IMPORTS + userCode;
      final Optional<String> className = getClassName(completeUserCode);
      if (!className.isPresent()) {
        return new JudgeResult("ClassNotFoundException: No public class found");
      }
      final Object[] tmp = MemoryJavaCompiler.INSTANCE
          .compileMethod("org.algohub." + className.get(), function.getName(), completeUserCode);
      clazz = tmp[0];
      method = (Method) tmp[1];
    } catch (ClassNotFoundException e) {
      return new JudgeResult(e.getClass() + " : " + e.getMessage());
    } catch (CompileErrorException e) {
      return new JudgeResult(createFriendlyMessage(e.getMessage()));
    }

    return judge(clazz, method, testCases, testCasesJson, function.getReturn_().getType());
  }

  private String createFriendlyMessage(final String errorMessage) {
    if(errorMessage == null || errorMessage.isEmpty()) return null;

    final StringBuilder sb = new StringBuilder();
    final String[] lines = errorMessage.split("\n");
    for (final String line : lines) {
      final int pos = line.indexOf(".java:");
      if (pos > 0) {
        // get the line number
        final int pos2 = line.indexOf(':', pos + ".java:".length());
        final int lineNumber;
        {
          final String numberStr = line.substring(pos + ".java:".length(), pos2);
          lineNumber = Integer.valueOf(numberStr) - IMPORTS_LINES;
        }
        final String friendlyMessage = "Line:" + lineNumber + line.substring(pos2);
        sb.append(friendlyMessage).append('\n');
      } else {
        sb.append(line).append('\n');
      }
    }
    return sb.toString();
  }

  private static class JudgeOneCaseResult {
    boolean correct;
    JsonNode wrongOutput;
  }
}
