package org.algohub.engine.codegenerator;

import com.google.common.collect.ImmutableMap;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.type.TypeNode;

/**
 * Generate compilable and runnable Java code.
 */
@SuppressWarnings({"PMD.InsufficientStringBufferDeclaration"})
public final class JavaCodeGenerator {
  /**
   * Map intermediate types to real java classes.
   */
  public static final ImmutableMap<IntermediateType, String> JAVA_CLASS_MAP =
      ImmutableMap.<IntermediateType, String>builder().put(IntermediateType.BOOL, "Boolean")
          .put(IntermediateType.CHAR, "Character")
          .put(IntermediateType.STRING, "String")
          .put(IntermediateType.DOUBLE, "Double")
          .put(IntermediateType.INT, "Integer")
          .put(IntermediateType.LONG, "Long").build();

  private JavaCodeGenerator() {}

  /**
   * Convert a TypeNode to a Java type declaration.
   *
   * <p> post order, recursive.</p>
   */
  private static String generateTypeDeclaration(final TypeNode type,
      final IntermediateType parentType) {
    if (!type.isContainer()) {
      if (parentType == IntermediateType.ARRAY) {
        return TypeMap.JAVA_TYPE_MAP.get(type.getValue());
      } else {
        return JAVA_CLASS_MAP.get(type.getValue());
      }
    }
    if (type.getValue() == IntermediateType.ARRAY) {
      return generateTypeDeclaration(type.getElementType().get(), type.getValue()) + "[]";
    } else {
      final String containerTypeStr = TypeMap.JAVA_TYPE_MAP.get(type.getValue());
      if (type.getKeyType().isPresent()) {
        return containerTypeStr + "<" + generateTypeDeclaration(type.getKeyType().get(),
            type.getValue()) + ", " + generateTypeDeclaration(type.getElementType().get(),
            type.getValue()) + ">";
      } else {
        return containerTypeStr + "<" + generateTypeDeclaration(type.getElementType().get(),
            type.getValue()) + ">";
      }
    }
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
    // the parent type should be ARRAY by default
    return generateTypeDeclaration(type, IntermediateType.ARRAY);
  }

  /**
   * Generate an empty function with comments.
   * @param function Function prototype
   * @return source code of a empty function
   */
  public static String generateEmptyFunction(final Function function) {
    return "public class Solution {\n"
        + FunctionGenerator.generateFunction(function, LanguageType.JAVA, 1) + "}\n";
  }
}
