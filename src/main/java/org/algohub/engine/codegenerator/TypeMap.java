package org.algohub.engine.codegenerator;

import com.google.common.collect.ImmutableMap;

import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.LanguageType;

/**
 * Map intermediate types to language related types.
 */
@SuppressWarnings({"PMD.AvoidDuplicateLiterals"})
final class TypeMap {
  /**
   * Map intermediate types to Java types.
   */
  public static final ImmutableMap<IntermediateType, String> JAVA_TYPE_MAP =
      ImmutableMap.<IntermediateType, String>builder().put(IntermediateType.BOOL, "boolean")
          .put(IntermediateType.CHAR, "char")
          .put(IntermediateType.STRING, "String")
          .put(IntermediateType.DOUBLE, "double")
          .put(IntermediateType.INT, "int")
          .put(IntermediateType.LONG, "long")

          //.put(IntermediateType.ARRAY, "[]")
          .put(IntermediateType.LIST, "ArrayList")
          .put(IntermediateType.SET, "HashSet")
          .put(IntermediateType.MAP, "HashMap")

          .put(IntermediateType.LINKED_LIST_NODE, "LinkedListNode")
          .put(IntermediateType.BINARY_TREE_NODE, "BinaryTreeNode")

          .build();

  /**
   * Map intermediate types to C++ types.
   */
  public static final ImmutableMap<IntermediateType, String> CPP_TYPE_MAP =
      ImmutableMap.<IntermediateType, String>builder().put(IntermediateType.BOOL, "bool")
          .put(IntermediateType.CHAR, "char")
          .put(IntermediateType.STRING, "string")
          .put(IntermediateType.DOUBLE, "double")
          .put(IntermediateType.INT, "int")
          .put(IntermediateType.LONG, "long long")

          .put(IntermediateType.ARRAY, "vector")
          .put(IntermediateType.LIST, "vector")
          .put(IntermediateType.SET, "unordered_set")
          .put(IntermediateType.MAP, "unordered_map")

          .put(IntermediateType.LINKED_LIST_NODE, "LinkedListNode")
          .put(IntermediateType.BINARY_TREE_NODE, "BinaryTreeNode")

          .build();

  /**
   * Map intermediate types to Python types.
   */
  public static final ImmutableMap<IntermediateType, String> PYTHON_TYPE_MAP =
      ImmutableMap.<IntermediateType, String>builder().put(IntermediateType.BOOL, "boolean")
          .put(IntermediateType.CHAR, "str")
          .put(IntermediateType.STRING, "str")
          .put(IntermediateType.DOUBLE, "float")
          .put(IntermediateType.INT, "int")
          .put(IntermediateType.LONG, "int")

          //.put(IntermediateType.ARRAY, "[]")
          //.put(IntermediateType.LIST, "[]")
          .put(IntermediateType.SET, "set")
          .put(IntermediateType.MAP, "dict")

          .put(IntermediateType.LINKED_LIST_NODE, "LinkedListNode")
          .put(IntermediateType.BINARY_TREE_NODE, "BinaryTreeNode")

          .build();

  /**
   * Map intermediate types to Ruby types.
   */
  public static final ImmutableMap<IntermediateType, String> RUBY_TYPE_MAP =
      ImmutableMap.<IntermediateType, String>builder().put(IntermediateType.BOOL, "Boolean")
          .put(IntermediateType.CHAR, "String")
          .put(IntermediateType.STRING, "String")
          .put(IntermediateType.DOUBLE, "Float")
          .put(IntermediateType.INT, "Fixnum")
          .put(IntermediateType.LONG, "Fixnum")

          //.put(IntermediateType.ARRAY, "[]")
          //.put(IntermediateType.LIST, "[]")
          .put(IntermediateType.SET, "Set")
          .put(IntermediateType.MAP, "Hash")

          .put(IntermediateType.LINKED_LIST_NODE, "LinkedListNode")
          .put(IntermediateType.BINARY_TREE_NODE, "BinaryTreeNode")

          .build();


  /**
   * LanguageType -> Type -> Specific Type.
   */
  public static final ImmutableMap<LanguageType, ImmutableMap<IntermediateType, String>> TYPE_MAP =
      ImmutableMap.<LanguageType, ImmutableMap<IntermediateType, String>>builder()
          .put(LanguageType.JAVA, JAVA_TYPE_MAP)
          .put(LanguageType.CPLUSPLUS, CPP_TYPE_MAP)
          .put(LanguageType.PYTHON, PYTHON_TYPE_MAP)
          .put(LanguageType.RUBY, RUBY_TYPE_MAP).build();

  private TypeMap() {}
}
