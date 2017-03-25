package org.algohub.engine.codegenerator;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.type.TypeNode;

import static org.algohub.engine.codegenerator.Indentation.append;


/**
 * Generate function declaration for display.
 */
@SuppressWarnings({"PMD.InsufficientStringBufferDeclaration", "PMD.ConsecutiveLiteralAppends",
    "PMD.InefficientStringBuffering", "PMD.UseUtilityClass", "PMD.ConsecutiveAppendsShouldReuse"})
public class FunctionGenerator {

  /**
   * Generate parameter declaration.
   *
   * @param type          type
   * @param languageType  programming language
   * @param parameterName parameter name
   * @return A block of code to declare a parameter
   */
  public static String generateParameterDeclaration(final TypeNode type,
      final LanguageType languageType, final String parameterName) {
    final StringBuilder result = new StringBuilder();
    final String typeDeclaration = generateTypeDeclaration(type, languageType);

    if (languageType == LanguageType.CPLUSPLUS) {
      if (type.isContainer() || type.getValue() == IntermediateType.STRING) {
        result.append(typeDeclaration).append("& ").append(parameterName);
      } else {
        result.append(typeDeclaration).append(' ').append(parameterName);
      }

    } else {
      result.append(typeDeclaration).append(' ').append(parameterName);
    }
    return result.toString();
  }

  /**
   * Convert a TypeNode to a specific language type declaration.
   *
   * <p> post order, recursive.</p>
   */
  public static String generateTypeDeclaration(final TypeNode type,
      final LanguageType languageType) {
    switch (languageType) {
      case JAVA:
        return JavaCodeGenerator.generateTypeDeclaration(type);
      case CPLUSPLUS:
        return CppCodeGenerator.generateTypeDeclaration(type);
      default:
        throw new IllegalArgumentException("Unsupported language: " + languageType);
    }
  }

  /**
   * Generate function declaration for Java, C++, C#.
   */
  public static String generateFunction(final Function function, final LanguageType languageType,
      final int indent) {
    final StringBuilder result = new StringBuilder();

    // function comment
    append(result, "/**\n", indent);
    for (final Function.Parameter p : function.getParameters()) {
      append(result, " * @param " + p.getName() + " " + p.getComment() + "\n", indent);
    }
    append(result, " * @return " + function.getReturn_().getComment() + "\n", indent);
    append(result, " */\n", indent);

    // function body
    if (languageType == LanguageType.JAVA) {
      append(result, "public ", indent);
      result.append(generateTypeDeclaration(function.getReturn_().getType(), languageType));
    } else {
      append(result, generateTypeDeclaration(function.getReturn_().getType(), languageType),
          indent);
    }
    result.append(" " + function.getName() + "(");
    for (final Function.Parameter p : function.getParameters()) {
      result.append(generateParameterDeclaration(p.getType(), languageType, p.getName()))
          .append(", ");
    }
    // delete the last unnecessary " ,"
    result.delete(result.length() - 2, result.length());
    result.append(") {\n");
    append(result, "// Write your code here\n", indent + 1);
    append(result, "}\n", indent);

    return result.toString();
  }
}
