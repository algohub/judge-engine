package org.algohub.engine.codegenerator;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.type.TypeNode;

/**
 * Generate C++ code.
 */
@SuppressWarnings({"PMD.InsufficientStringBufferDeclaration"})
public final class CppCodeGenerator {

  private CppCodeGenerator() {}

  /**
   * Generate a  main.cpp to call solution.h.
   *
   * @param function the function type info
   * @param fromFile read testcases from file or stdin
   * @return source code of main.cpp
   */
  public static String generateMain(final Function function, boolean fromFile) {
    final StringBuilder result = new StringBuilder();
    result.append("#include <algohub/serialize.h>\n#include <algohub/deserialize.h>\n#include "
        + "<algohub/judge_result.h>\nusing namespace std;\n#include \"solution.cpp\"\n\n\n");

    Indentation.append(result, "int main(int argc, char* argv[]) {\n", 0);
    Indentation.append(result, "signal(SIGSEGV, sigsegv_handler);\n", 1);
    Indentation.append(result, "signal(SIGABRT, sigabrt_handler);\n\n", 1);
    if(fromFile) {
      Indentation.append(result, "ifstream ifs(\"testcases.json\");\n", 1);
      Indentation.append(result, "string testcases_text((istreambuf_iterator<char>(ifs)), (istreambuf_iterator<char>()));\n", 1);
    } else {
      Indentation.append(result, "string testcases_text;\n", 1);
      Indentation.append(result, "getline(cin, testcases_text);\n", 1);
    }
    Indentation.append(result, "rapidjson::Document testcases_json;\n", 1);
    Indentation.append(result, "rapidjson::Document::AllocatorType& allocator = testcases_json.GetAllocator();\n", 1);
    Indentation.append(result, "testcases_json.Parse(testcases_text.c_str());\n", 1);
    Indentation.append(result, "assert (testcases_json.IsArray());\n\n", 1);

    Indentation
        .append(result, "for (rapidjson::SizeType i = 0; i < testcases_json.Size(); i++) {\n", 1);
    Indentation.append(result, "const rapidjson::Value& input = testcases_json[i][\"input\"];\n", 2);
    Indentation.append(result, "assert (input.IsArray());\n\n", 2);

    // deserialize expected output
    Indentation.append(result,
        generateTypeDeclaration(function.getReturn_().getType()) + " expected_output;\n", 2);
    Indentation.append(result, "from_json(testcases_json[i][\"output\"], expected_output);\n\n", 2);

    // Deserialize arguments
    final Function.Parameter[] parameters = function.getParameters();
    for (int i = 0; i < parameters.length; ++i) {
      Indentation.append(result,
          generateTypeDeclaration(parameters[i].getType()) + " algohub_" + parameters[i].getName()
              + ";\n", 2);
      Indentation
          .append(result, "from_json(input[" + i + "], algohub_" + parameters[i].getName() + ");\n",
              2);
    }
    result.append('\n');

    Indentation.append(result, "const auto result = " + function.getName() + "(", 2);

    for (final Function.Parameter parameter : parameters) {
      result.append("algohub_").append(parameter.getName()).append(", ");
    }

    // delete the last unnecessary " ,"
    if (parameters.length > 0) {
      result.delete(result.length() - 2, result.length());
    }
    result.append(");\n\n");

    Indentation.append(result, "if (expected_output == result) {\n", 2);
    Indentation.append(result, "judge_result.testcase_passed_count += 1;\n", 3);
    Indentation.append(result, "} else {\n", 2);
    Indentation.append(result, "judge_result.input = rapidjson::Value(input, allocator).Move();\n", 3);
    Indentation.append(result, "judge_result.expected_output = testcases_json[i][\"output\"];\n", 3);
    Indentation.append(result, "judge_result.output = to_json(result, allocator);\n", 3);
    Indentation.append(result, "break;\n", 3);
    Indentation.append(result, "}\n", 2);
    Indentation.append(result, "}\n\n", 1);

    Indentation
        .append(result, "if (judge_result.testcase_passed_count < testcases_json.Size()) {\n", 1);
    Indentation.append(result, "judge_result.status_code = StatusCode::WRONG_ANSWER;\n", 2);
    Indentation.append(result, "} else {\n", 1);
    Indentation.append(result, "judge_result.status_code = StatusCode::ACCEPTED;\n", 2);
    Indentation.append(result, "}\n\n", 1);

    Indentation.append(result, "std::cout << std::endl << judge_result.to_string();\n", 1);
    Indentation.append(result, "return 0;\n}\n", 1);
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
      return TypeMap.TYPE_MAP.get(LanguageType.CPLUSPLUS).get(type.getValue());
    }

    final String containerTypeStr =
        TypeMap.TYPE_MAP.get(LanguageType.CPLUSPLUS).get(type.getValue());
    if (type.getKeyType().isPresent()) {
      return containerTypeStr + "<" + generateTypeDeclaration(type.getKeyType().get()) + ", "
          + generateTypeDeclaration(type.getElementType().get()) + ">";
    } else {
      final String typeDeclaration =
          containerTypeStr + "<" + generateTypeDeclaration(type.getElementType().get()) + ">";
      return addSharedPtr(type, typeDeclaration);
    }
  }

  /**
   * Generate an empty function with comments.
   * @param function Function prototype
   * @return source code of a empty function
   */
  public static String generateEmptyFunction(final Function function) {
    return FunctionGenerator.generateFunction(function, LanguageType.CPLUSPLUS, 0);
  }

  private static String addSharedPtr(TypeNode type, String elemType) {
    if (type.getValue() == IntermediateType.BINARY_TREE_NODE
        || type.getValue() == IntermediateType.LINKED_LIST_NODE) {
      return "shared_ptr<" + elemType + ">";
    } else {
      return elemType;
    }
  }
}
