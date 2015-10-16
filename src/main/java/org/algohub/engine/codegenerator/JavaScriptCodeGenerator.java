package org.algohub.engine.codegenerator;

import org.algohub.engine.pojo.Function;

/**
 * Generate runnable JavaScript code and call user's function.
 */
@SuppressWarnings({"PMD.InsufficientStringBufferDeclaration"})
public final class JavaScriptCodeGenerator {

  private JavaScriptCodeGenerator() {}

  /**
   * Generate a runnable script for JavaScript.
   *
   * @param function the function type info
   * @return A Compilable and runnable JavaScript script.
   */
  public static String generateMain(final Function function) {
    final StringBuilder result = new StringBuilder();

    result.append("input = sys.stdin.read()\njsonValue = json.loads(input)[\"dummy_key\"]\n");

    final Function.Parameter[] parameters = function.getParameters();

    for (int i = 0; i < parameters.length; ++i) {
      result.append(parameters[i].getName() + " = jsonValue[" + i + "]\n");
    }


    result.append("result = " + function.getName() + "(");


    for (final Function.Parameter parameter : parameters) {
      result.append(parameter.getName()).append(", ");
    }

    // delete the last unnecessary " ,"
    result.delete(result.length() - 2, result.length());
    result.append(")\nprint(json.dumps(result, separators=(',', ':')))\n");

    return result.toString();
  }
}
