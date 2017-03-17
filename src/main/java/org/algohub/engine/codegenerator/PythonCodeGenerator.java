package org.algohub.engine.codegenerator;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.util.ObjectMapperInstance;

/**
 * Generate Python code.
 */
@SuppressWarnings({"PMD.InsufficientStringBufferDeclaration"})
public final class PythonCodeGenerator {

  private PythonCodeGenerator() {}

  /**
   * Generate the main function.
   *
   * @param function Function type info.
   * @param fromFile read testcases from file or stdin
   * @return the complete source code
   */
  @SuppressWarnings({"PMD.PreserveStackTrace"})
  public static String generateMain(final Function function, boolean fromFile) {
    final StringBuilder result = new StringBuilder();

    result.append("import json\nimport collections\nfrom algohub import *\nimport solution\n\n"
        + "if __name__ == '__main__':\n");

    final Function.Parameter[] parameters = function.getParameters();
    try {
      for (final Function.Parameter parameter : parameters) {
        Indentation.append(result,
            parameter.getName() + "_type = TypeNode.from_json('" + ObjectMapperInstance.INSTANCE
                .writeValueAsString(parameter.getType()) + "')\n", 1);

      }
      Indentation.append(result,
          "output_type = TypeNode.from_json('" + ObjectMapperInstance.INSTANCE
              .writeValueAsString(function.getReturn_().getType()) + "')\n\n", 1);
    } catch (JsonProcessingException e) {  // impossible
      throw new IllegalStateException(e.getMessage());
    }

    if(fromFile) {
      Indentation.append(result, "with open(\"testcases.json\", \"r\") as f:\n", 1);
      Indentation.append(result, "raw_testcases = json.loads(f.read())\n", 2);
    } else {
      Indentation.append(result, "raw_testcases = json.loads(input())\n", 1);
    }
    Indentation.append(result, "assert isinstance(raw_testcases, list)\n\n", 1);

    Indentation.append(result, "for i in range(len(raw_testcases)):\n", 1);
    Indentation.append(result, "test_case = raw_testcases[i]\n", 2);
    for (int i = 0; i < parameters.length; ++i) {
      final Function.Parameter parameter = parameters[i];
      Indentation.append(result,
          "algohub_" + parameter.getName() + " = from_json(" + "test_case['input'][" + i + "], "
              + parameter.getName() + "_type)\n", 2);
    }

    Indentation
        .append(result, "expected_output = from_json(" + "test_case['output'], output_type)\n\n",
            2);

    Indentation.append(result, "actual_output = solution." + function.getName() + "(", 2);
    for (final Function.Parameter parameter : parameters) {
      result.append("algohub_").append(parameter.getName()).append(", ");
    }
    if (parameters.length > 0) {
      result.delete(result.length() - 2, result.length());
    }
    result.append(")\n\n");

    Indentation.append(result, "if actual_output != expected_output:\n", 2);
    Indentation.append(result, "print(JudgeResult(StatusCode.WRONG_ANSWER).to_json())\n", 3);
    Indentation.append(result, "exit(0)\n\n", 3);


    Indentation.append(result, "print(JudgeResult(StatusCode.ACCEPTED).to_json())\n", 1);
    return result.toString();
  }

  /**
   * Generate an empty function with comments.
   * @param function Function prototype
   * @return source code of a empty function
   */
  public static String generateEmptyFunction(final Function function, final int indent) {
    final StringBuilder result = new StringBuilder();

    // function comment
    for (final Function.Parameter p : function.getParameters()) {
      Indentation.append(result,
          "# @param {" + FunctionGenerator.generateTypeDeclaration(p.getType(), LanguageType.PYTHON)
              + "} " + p.getName() + " " + p.getComment() + "\n", indent);
    }

    final Function.Return return_ = function.getReturn_();
    Indentation.append(result, "# @return {" + FunctionGenerator
        .generateTypeDeclaration(return_.getType(), LanguageType.PYTHON) + "} " + return_
        .getComment() + "\n", indent);

    // function body
    Indentation.append(result, "def " + function.getName() + "(", indent);

    for (final Function.Parameter p : function.getParameters()) {
      result.append(p.getName()).append(", ");
    }
    // delete the last unnecessary " ,"
    result.delete(result.length() - 2, result.length());
    result.append("):\n");
    Indentation.append(result, "# Write your code here\n", indent + 1);

    return result.toString();
  }
}
