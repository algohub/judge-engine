package org.algohub.engine.codegenerator;

import org.junit.Test;

import static org.algohub.engine.codegenerator.DataTypes.*;
import static org.junit.Assert.assertEquals;


@SuppressWarnings({"PMD.CommentRequired"})
public class PythonCodeGeneratorTest {

  @Test public void generatePythonFunctiontTest() {
    final String twoSumExpected = "# @param {int[]} numbers An array of Integers\n"
        + "# @param {int} target target = numbers[index1] + numbers[index2]\n"
        + "# @return {int[]} [index1 + 1, index2 + 1] (index1 < index2)\n"
        + "def twoSum(numbers, target):\n    # Write your code here\n";
    final String twoSumGenerated = PythonCodeGenerator.generateEmptyFunction(TWO_SUM);
    assertEquals(twoSumExpected, twoSumGenerated);

    final String wordLadderExpected = "# @param {str} begin_word the begin word\n"
        + "# @param {str} end_word the end word\n"
        + "# @param {set<str>} dict the dictionary\n" + "# @return {int} The shortest length\n"
        + "def ladderLength(begin_word, end_word, dict):\n    # Write your code here\n";
    final String wordLadderGenerated = PythonCodeGenerator.generateEmptyFunction(WORD_LADDER);
    assertEquals(wordLadderExpected, wordLadderGenerated);
  }

  @Test public void generateTypeDeclarationTest() {
    assertEquals("int[]", PythonCodeGenerator.generateTypeDeclaration(ARRAY_INT));
    assertEquals("int[]", PythonCodeGenerator.generateTypeDeclaration(LIST_INT));
    assertEquals("set<int>", PythonCodeGenerator.generateTypeDeclaration(SET_INT));
    assertEquals("LinkedListNode<int>",
        PythonCodeGenerator.generateTypeDeclaration(LINKED_LIST_INT));

    assertEquals("dict<str, int>",
        PythonCodeGenerator.generateTypeDeclaration(MAP_STRING_INT));
    assertEquals("dict<int, float>",
        PythonCodeGenerator.generateTypeDeclaration(MAP_INT_DOUBLE));

    assertEquals("BinaryTreeNode<int>",
        PythonCodeGenerator.generateTypeDeclaration(BINARY_TREE_INT));

    assertEquals("int[][]", PythonCodeGenerator.generateTypeDeclaration(
        ARRAY_ARRAY_INT));
    assertEquals("int[][]", PythonCodeGenerator.generateTypeDeclaration(
        ARRAY_LIST_INT));
    assertEquals("set<int>[]", PythonCodeGenerator.generateTypeDeclaration(
        ARRAY_SET_INT));
    assertEquals("LinkedListNode<int>[]",
        PythonCodeGenerator.generateTypeDeclaration(ARRAY_LINKED_LIST_INT));
    assertEquals("dict<str, int>[]",
        PythonCodeGenerator.generateTypeDeclaration(ARRAY_MAP_STRING_INT));
    assertEquals("dict<int, float>[]",
        PythonCodeGenerator.generateTypeDeclaration(ARRAY_MAP_INT_DOUBLE));

    assertEquals("int[][]", PythonCodeGenerator.generateTypeDeclaration(
        LIST_ARRAY_INT));
    assertEquals("int[][]", PythonCodeGenerator.generateTypeDeclaration(
        LIST_LIST_INT));
    assertEquals("set<int>[]", PythonCodeGenerator.generateTypeDeclaration(
        LIST_SET_INT));
    assertEquals("LinkedListNode<int>[]",
        PythonCodeGenerator.generateTypeDeclaration(LIST_LINKED_LIST_INT));
    assertEquals("dict<str, int>[]",
        PythonCodeGenerator.generateTypeDeclaration(LIST_MAP_STRING_INT));
    assertEquals("dict<int, float>[]",
        PythonCodeGenerator.generateTypeDeclaration(LIST_MAP_INT_DOUBLE));

    assertEquals("set<int[]>",
        PythonCodeGenerator.generateTypeDeclaration(SET_ARRAY_INT));
    assertEquals("set<int[]>",
        PythonCodeGenerator.generateTypeDeclaration(SET_LIST_INT));
    assertEquals("set<set<int>>",
        PythonCodeGenerator.generateTypeDeclaration(SET_SET_INT));
    assertEquals("set<LinkedListNode<int>>",
        PythonCodeGenerator.generateTypeDeclaration(SET_LINKED_LIST_INT));
    assertEquals("set<dict<str, int>>",
        PythonCodeGenerator.generateTypeDeclaration(SET_MAP_STRING_INT));
    assertEquals("set<dict<int, float>>",
        PythonCodeGenerator.generateTypeDeclaration(SET_MAP_INT_DOUBLE));

    assertEquals("LinkedListNode<int[]>",
        PythonCodeGenerator.generateTypeDeclaration(LINKED_LIST_ARRAY_INT));
    assertEquals("LinkedListNode<int[]>",
        PythonCodeGenerator.generateTypeDeclaration(LINKED_LIST_LIST_INT));
    assertEquals("LinkedListNode<set<int>>",
        PythonCodeGenerator.generateTypeDeclaration(LINKED_LIST_SET_INT));
    assertEquals("LinkedListNode<LinkedListNode<int>>",
        PythonCodeGenerator.generateTypeDeclaration(LINKED_LIST_LINKED_LIST_INT));
    assertEquals("LinkedListNode<dict<str, int>>",
        PythonCodeGenerator.generateTypeDeclaration(LINKED_LIST_MAP_STRING_INT));
    assertEquals("LinkedListNode<dict<int, float>>",
        PythonCodeGenerator.generateTypeDeclaration(LINKED_LIST_MAP_INT_DOUBLE));

    assertEquals("BinaryTreeNode<int[]>",
        PythonCodeGenerator.generateTypeDeclaration(BINARY_TREE_ARRAY_INT));
    assertEquals("BinaryTreeNode<int[]>",
        PythonCodeGenerator.generateTypeDeclaration(BINARY_TREE_LIST_INT));
    assertEquals("BinaryTreeNode<set<int>>",
        PythonCodeGenerator.generateTypeDeclaration(BINARY_TREE_SET_INT));
    assertEquals("BinaryTreeNode<LinkedListNode<int>>",
        PythonCodeGenerator.generateTypeDeclaration(BINARY_TREE_LINKED_LIST_INT));
    assertEquals("BinaryTreeNode<dict<str, int>>",
        PythonCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_STRING_INT));
    assertEquals("BinaryTreeNode<dict<int, float>>",
        PythonCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_INT_DOUBLE));

    assertEquals("set<dict<str, LinkedListNode<int>>>[][]",
        PythonCodeGenerator.generateTypeDeclaration(ARRAY_LIST_SET_MAP_STRING_LINKED_LIST_INT));

    assertEquals("BinaryTreeNode<dict<str, set<float[]>>>",
        PythonCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_STRING_SET_LIST_DOUBLE));
  }
}
