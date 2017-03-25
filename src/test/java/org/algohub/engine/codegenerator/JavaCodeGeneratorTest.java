package org.algohub.engine.codegenerator;

import org.junit.Test;

import static org.algohub.engine.codegenerator.DataTypes.*;
import static org.junit.Assert.assertEquals;


@SuppressWarnings({"PMD.CommentRequired"})
public class JavaCodeGeneratorTest {

  @Test public void generateJavaFunctionTest() {
    final String twoSumGenerated = JavaCodeGenerator.generateEmptyFunction(TWO_SUM);
    final String twoSumExpected =
        "public class Solution {\n" + "    /**\n" + "     * @param numbers An array of Integers\n"
            + "     * @param target target = numbers[index1] + numbers[index2]\n"
            + "     * @return [index1 + 1, index2 + 1] (index1 < index2)\n" + "     */\n"
            + "    public int[] twoSum(int[] numbers, int target) {\n"
            + "        // Write your code here\n" + "    }\n" + "}\n";
    assertEquals(twoSumExpected, twoSumGenerated);

    final String wordLadderGenerated = JavaCodeGenerator.generateEmptyFunction(WORD_LADDER);
    final String wordLadderExpected =
        "public class Solution {\n" + "    /**\n" + "     * @param begin_word the begin word\n"
            + "     * @param end_word the end word\n" + "     * @param dict the dictionary\n"
            + "     * @return The shortest length\n" + "     */\n"
            + "    public int ladderLength(String begin_word, String end_word, HashSet<String> "
            + "dict) {\n"
            + "        // Write your code here\n" + "    }\n" + "}\n";
    assertEquals(wordLadderExpected, wordLadderGenerated);
  }

  @Test public void generateTypeDeclarationTest() {
    assertEquals("int[]", JavaCodeGenerator.generateTypeDeclaration(ARRAY_INT));
    assertEquals("ArrayList<Integer>", JavaCodeGenerator.generateTypeDeclaration(LIST_INT));
    assertEquals("HashSet<Integer>", JavaCodeGenerator.generateTypeDeclaration(SET_INT));
    assertEquals("LinkedListNode<Integer>",
        JavaCodeGenerator.generateTypeDeclaration(LINKED_LIST_INT));

    assertEquals("HashMap<String, Integer>",
        JavaCodeGenerator.generateTypeDeclaration(MAP_STRING_INT));
    assertEquals("HashMap<Integer, Double>",
        JavaCodeGenerator.generateTypeDeclaration(MAP_INT_DOUBLE));

    assertEquals("BinaryTreeNode<Integer>",
        JavaCodeGenerator.generateTypeDeclaration(BINARY_TREE_INT));

    assertEquals("int[][]", JavaCodeGenerator.generateTypeDeclaration(
        ARRAY_ARRAY_INT));
    assertEquals("ArrayList<Integer>[]", JavaCodeGenerator.generateTypeDeclaration(
        ARRAY_LIST_INT));
    assertEquals("HashSet<Integer>[]", JavaCodeGenerator.generateTypeDeclaration(
        ARRAY_SET_INT));
    assertEquals("LinkedListNode<Integer>[]",
        JavaCodeGenerator.generateTypeDeclaration(ARRAY_LINKED_LIST_INT));
    assertEquals("HashMap<String, Integer>[]",
        JavaCodeGenerator.generateTypeDeclaration(ARRAY_MAP_STRING_INT));
    assertEquals("HashMap<Integer, Double>[]",
        JavaCodeGenerator.generateTypeDeclaration(ARRAY_MAP_INT_DOUBLE));

    assertEquals("ArrayList<int[]>", JavaCodeGenerator.generateTypeDeclaration(
        LIST_ARRAY_INT));
    assertEquals("ArrayList<ArrayList<Integer>>", JavaCodeGenerator.generateTypeDeclaration(
        LIST_LIST_INT));
    assertEquals("ArrayList<HashSet<Integer>>", JavaCodeGenerator.generateTypeDeclaration(
        LIST_SET_INT));
    assertEquals("ArrayList<LinkedListNode<Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(LIST_LINKED_LIST_INT));
    assertEquals("ArrayList<HashMap<String, Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(LIST_MAP_STRING_INT));
    assertEquals("ArrayList<HashMap<Integer, Double>>",
        JavaCodeGenerator.generateTypeDeclaration(LIST_MAP_INT_DOUBLE));

    assertEquals("HashSet<int[]>",
        JavaCodeGenerator.generateTypeDeclaration(SET_ARRAY_INT));
    assertEquals("HashSet<ArrayList<Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(SET_LIST_INT));
    assertEquals("HashSet<HashSet<Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(SET_SET_INT));
    assertEquals("HashSet<LinkedListNode<Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(SET_LINKED_LIST_INT));
    assertEquals("HashSet<HashMap<String, Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(SET_MAP_STRING_INT));
    assertEquals("HashSet<HashMap<Integer, Double>>",
        JavaCodeGenerator.generateTypeDeclaration(SET_MAP_INT_DOUBLE));

    assertEquals("LinkedListNode<int[]>",
        JavaCodeGenerator.generateTypeDeclaration(LINKED_LIST_ARRAY_INT));
    assertEquals("LinkedListNode<ArrayList<Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(LINKED_LIST_LIST_INT));
    assertEquals("LinkedListNode<HashSet<Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(LINKED_LIST_SET_INT));
    assertEquals("LinkedListNode<LinkedListNode<Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(LINKED_LIST_LINKED_LIST_INT));
    assertEquals("LinkedListNode<HashMap<String, Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(LINKED_LIST_MAP_STRING_INT));
    assertEquals("LinkedListNode<HashMap<Integer, Double>>",
        JavaCodeGenerator.generateTypeDeclaration(LINKED_LIST_MAP_INT_DOUBLE));

    assertEquals("BinaryTreeNode<int[]>",
        JavaCodeGenerator.generateTypeDeclaration(BINARY_TREE_ARRAY_INT));
    assertEquals("BinaryTreeNode<ArrayList<Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(BINARY_TREE_LIST_INT));
    assertEquals("BinaryTreeNode<HashSet<Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(BINARY_TREE_SET_INT));
    assertEquals("BinaryTreeNode<LinkedListNode<Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(BINARY_TREE_LINKED_LIST_INT));
    assertEquals("BinaryTreeNode<HashMap<String, Integer>>",
        JavaCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_STRING_INT));
    assertEquals("BinaryTreeNode<HashMap<Integer, Double>>",
        JavaCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_INT_DOUBLE));

    assertEquals("ArrayList<HashSet<HashMap<String, LinkedListNode<Integer>>>>[]",
        JavaCodeGenerator.generateTypeDeclaration(ARRAY_LIST_SET_MAP_STRING_LINKED_LIST_INT));

    assertEquals("BinaryTreeNode<HashMap<String, HashSet<ArrayList<Double>>>>",
        JavaCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_STRING_SET_LIST_DOUBLE));
  }
}
