package org.algohub.engine.codegenerator;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.type.TypeNode;
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

    Indentation.append(result, "judge_result = JudgeResult(StatusCode.ACCEPTED)\n\n", 1);

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

    Indentation.append(result, "if actual_output == expected_output:\n", 2);
    Indentation.append(result, "judge_result.testcase_passed_count += 1\n", 3);
    Indentation.append(result, "else:\n", 2);
    Indentation.append(result, "judge_result.input = test_case['input']\n", 3);
    Indentation.append(result, "judge_result.expected_output = test_case['output']\n", 3);
    Indentation.append(result, "judge_result.output = to_json(actual_output, output_type);\n", 3);
    Indentation.append(result, "break\n\n", 3);

    Indentation.append(result,
        "if judge_result.testcase_passed_count < len(raw_testcases):\n", 1);
    Indentation.append(result, "judge_result.status_code = StatusCode.WRONG_ANSWER\n", 2);
    Indentation.append(result, "else:\n", 1);
    Indentation.append(result, "judge_result.status_code = StatusCode.ACCEPTED\n\n", 2);

    Indentation.append(result, "print(judge_result)\n", 1);
    return result.toString();
  }

  /**
   * Generate a type declaration.
   *
   * <p>post order, recursive.</p>
   *
   * @param type the type
   * @return type declaration
   */
  static String generateTypeDeclaration(final TypeNode type) {
    if (!type.isContainer()) {
      return TypeMap.PYTHON_TYPE_MAP.get(type.getValue());
    }

    if (type.getValue() ==  IntermediateType.ARRAY || type.getValue() ==  IntermediateType.LIST) {
      return generateTypeDeclaration(type.getElementType().get()) + "[]";
    } else {
      final String containerTypeStr = TypeMap.PYTHON_TYPE_MAP.get(type.getValue());
      if (type.getKeyType().isPresent()) {
        return containerTypeStr + "<" + generateTypeDeclaration(type.getKeyType().get())
            + ", " + generateTypeDeclaration(type.getElementType().get())
            + ">";
      } else {
        return containerTypeStr + "<" + generateTypeDeclaration(type.getElementType().get()) + ">";
      }
    }
  }

  /**
   * Generate an empty function with comments.
   * @param function Function prototype
   * @return source code of a empty function
   */
  public static String generateEmptyFunction(final Function function) {
    return generateEmptyFunction(function, 0);
  }

  static String generateEmptyFunction(final Function function, final int indent) {
    final StringBuilder result = new StringBuilder();

    // function comment
    for (final Function.Parameter p : function.getParameters()) {
      Indentation.append(result,
          "# @param {" + generateTypeDeclaration(p.getType())
              + "} " + p.getName() + " " + p.getComment() + "\n", indent);
    }

    final Function.Return return_ = function.getReturn_();
    Indentation.append(result, "# @return {" +
        generateTypeDeclaration(return_.getType()) + "} " + return_.getComment() + "\n", indent);

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
