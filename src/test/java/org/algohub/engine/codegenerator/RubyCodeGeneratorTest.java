package org.algohub.engine.codegenerator;

import org.junit.Test;

import static org.algohub.engine.codegenerator.DataTypes.*;
import static org.junit.Assert.assertEquals;


@SuppressWarnings({"PMD.CommentRequired"})
public class RubyCodeGeneratorTest {

  @Test public void generateRubyFunctionTest() {

    final String twoSumExpected = "# @param {Fixnum[]} numbers An array of Integers\n"
        + "# @param {Fixnum} target target = numbers[index1] + numbers[index2]\n"
        + "# @return {Fixnum[]} [index1 + 1, index2 + 1] (index1 < index2)\n"
        + "def twoSum(numbers, target)\n" + "    # Write your code here\n" + "end";
    final String twoSumGenerated = RubyCodeGenerator.generateEmptyFunction(TWO_SUM);
    assertEquals(twoSumExpected, twoSumGenerated);

    final String wordLadderExpected = "# @param {String} begin_word the begin word\n"
        + "# @param {String} end_word the end word\n"
        + "# @param {Set<String>} dict the dictionary\n"
        + "# @return {Fixnum} The shortest length\n"
        + "def ladderLength(begin_word, end_word, dict)\n" + "    # Write your code here\n" + "end";
    final String wordLadderGenerated = RubyCodeGenerator.generateEmptyFunction(WORD_LADDER);
    assertEquals(wordLadderExpected, wordLadderGenerated);
  }

  @Test public void generateTypeDeclarationTest() {
    assertEquals("Fixnum[]", RubyCodeGenerator.generateTypeDeclaration(ARRAY_INT));
    assertEquals("Fixnum[]", RubyCodeGenerator.generateTypeDeclaration(LIST_INT));
    assertEquals("Set<Fixnum>", RubyCodeGenerator.generateTypeDeclaration(SET_INT));
    assertEquals("LinkedListNode<Fixnum>",
        RubyCodeGenerator.generateTypeDeclaration(LINKED_LIST_INT));

    assertEquals("Hash<String, Fixnum>",
        RubyCodeGenerator.generateTypeDeclaration(MAP_STRING_INT));
    assertEquals("Hash<Fixnum, Float>",
        RubyCodeGenerator.generateTypeDeclaration(MAP_INT_DOUBLE));

    assertEquals("BinaryTreeNode<Fixnum>",
        RubyCodeGenerator.generateTypeDeclaration(BINARY_TREE_INT));

    assertEquals("Fixnum[][]", RubyCodeGenerator.generateTypeDeclaration(
        ARRAY_ARRAY_INT));
    assertEquals("Fixnum[][]", RubyCodeGenerator.generateTypeDeclaration(
        ARRAY_LIST_INT));
    assertEquals("Set<Fixnum>[]", RubyCodeGenerator.generateTypeDeclaration(
        ARRAY_SET_INT));
    assertEquals("LinkedListNode<Fixnum>[]",
        RubyCodeGenerator.generateTypeDeclaration(ARRAY_LINKED_LIST_INT));
    assertEquals("Hash<String, Fixnum>[]",
        RubyCodeGenerator.generateTypeDeclaration(ARRAY_MAP_STRING_INT));
    assertEquals("Hash<Fixnum, Float>[]",
        RubyCodeGenerator.generateTypeDeclaration(ARRAY_MAP_INT_DOUBLE));

    assertEquals("Fixnum[][]", RubyCodeGenerator.generateTypeDeclaration(
        LIST_ARRAY_INT));
    assertEquals("Fixnum[][]", RubyCodeGenerator.generateTypeDeclaration(
        LIST_LIST_INT));
    assertEquals("Set<Fixnum>[]", RubyCodeGenerator.generateTypeDeclaration(
        LIST_SET_INT));
    assertEquals("LinkedListNode<Fixnum>[]",
        RubyCodeGenerator.generateTypeDeclaration(LIST_LINKED_LIST_INT));
    assertEquals("Hash<String, Fixnum>[]",
        RubyCodeGenerator.generateTypeDeclaration(LIST_MAP_STRING_INT));
    assertEquals("Hash<Fixnum, Float>[]",
        RubyCodeGenerator.generateTypeDeclaration(LIST_MAP_INT_DOUBLE));

    assertEquals("Set<Fixnum[]>",
        RubyCodeGenerator.generateTypeDeclaration(SET_ARRAY_INT));
    assertEquals("Set<Fixnum[]>",
        RubyCodeGenerator.generateTypeDeclaration(SET_LIST_INT));
    assertEquals("Set<Set<Fixnum>>",
        RubyCodeGenerator.generateTypeDeclaration(SET_SET_INT));
    assertEquals("Set<LinkedListNode<Fixnum>>",
        RubyCodeGenerator.generateTypeDeclaration(SET_LINKED_LIST_INT));
    assertEquals("Set<Hash<String, Fixnum>>",
        RubyCodeGenerator.generateTypeDeclaration(SET_MAP_STRING_INT));
    assertEquals("Set<Hash<Fixnum, Float>>",
        RubyCodeGenerator.generateTypeDeclaration(SET_MAP_INT_DOUBLE));

    assertEquals("LinkedListNode<Fixnum[]>",
        RubyCodeGenerator.generateTypeDeclaration(LINKED_LIST_ARRAY_INT));
    assertEquals("LinkedListNode<Fixnum[]>",
        RubyCodeGenerator.generateTypeDeclaration(LINKED_LIST_LIST_INT));
    assertEquals("LinkedListNode<Set<Fixnum>>",
        RubyCodeGenerator.generateTypeDeclaration(LINKED_LIST_SET_INT));
    assertEquals("LinkedListNode<LinkedListNode<Fixnum>>",
        RubyCodeGenerator.generateTypeDeclaration(LINKED_LIST_LINKED_LIST_INT));
    assertEquals("LinkedListNode<Hash<String, Fixnum>>",
        RubyCodeGenerator.generateTypeDeclaration(LINKED_LIST_MAP_STRING_INT));
    assertEquals("LinkedListNode<Hash<Fixnum, Float>>",
        RubyCodeGenerator.generateTypeDeclaration(LINKED_LIST_MAP_INT_DOUBLE));

    assertEquals("BinaryTreeNode<Fixnum[]>",
        RubyCodeGenerator.generateTypeDeclaration(BINARY_TREE_ARRAY_INT));
    assertEquals("BinaryTreeNode<Fixnum[]>",
        RubyCodeGenerator.generateTypeDeclaration(BINARY_TREE_LIST_INT));
    assertEquals("BinaryTreeNode<Set<Fixnum>>",
        RubyCodeGenerator.generateTypeDeclaration(BINARY_TREE_SET_INT));
    assertEquals("BinaryTreeNode<LinkedListNode<Fixnum>>",
        RubyCodeGenerator.generateTypeDeclaration(BINARY_TREE_LINKED_LIST_INT));
    assertEquals("BinaryTreeNode<Hash<String, Fixnum>>",
        RubyCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_STRING_INT));
    assertEquals("BinaryTreeNode<Hash<Fixnum, Float>>",
        RubyCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_INT_DOUBLE));

    assertEquals("Set<Hash<String, LinkedListNode<Fixnum>>>[][]",
        RubyCodeGenerator.generateTypeDeclaration(ARRAY_LIST_SET_MAP_STRING_LINKED_LIST_INT));

    assertEquals("BinaryTreeNode<Hash<String, Set<Float[]>>>",
        RubyCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_STRING_SET_LIST_DOUBLE));
  }
}
