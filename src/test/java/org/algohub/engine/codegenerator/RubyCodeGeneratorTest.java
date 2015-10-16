package org.algohub.engine.codegenerator;

import org.algohub.engine.type.LanguageType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"PMD.CommentRequired"})
public class RubyCodeGeneratorTest {

  @Test public void generateRubyFunctionTest() {

    final String twoSumExpected = "# @param {Fixnum[]} numbers An array of Integers\n"
        + "# @param {Fixnum} target target = numbers[index1] + numbers[index2]\n"
        + "# @return {Fixnum[]} [index1 + 1, index2 + 1] (index1 < index2)\n"
        + "def twoSum(numbers, target)\n" + "    # Write your code here\n" + "end";
    final String twoSumGenerated =
        RubyCodeGenerator.generateEmptyFunction(FunctionGeneratorTest.TWO_SUM);
    assertEquals(twoSumExpected, twoSumGenerated);

    final String wordLadderExpected = "# @param {String} begin_word the begin word\n"
        + "# @param {String} end_word the end word\n"
        + "# @param {Set<String>} dict the dictionary\n"
        + "# @return {Fixnum} The shortest length\n"
        + "def ladderLength(begin_word, end_word, dict)\n" + "    # Write your code here\n" + "end";
    final String wordLadderGenerated =
        RubyCodeGenerator.generateEmptyFunction(FunctionGeneratorTest.WORD_LADDER);
    assertEquals(wordLadderExpected, wordLadderGenerated);
  }

  @Test public void generateTypeDeclarationTest() {
    final String typeStr1 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.ARRAY_INT, LanguageType.RUBY);
    assertEquals("Fixnum[]", typeStr1);


    final String typeStr2 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.LIST_INT, LanguageType.RUBY);
    assertEquals("Fixnum[]", typeStr2);

    final String typeStr3 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.LIST_ARRAY_INT, LanguageType.RUBY);
    assertEquals("Fixnum[][]", typeStr3);


    final String typeStr4 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.LIST_LIST_INT, LanguageType.RUBY);
    assertEquals("Fixnum[][]", typeStr4);


    final String typeStr5 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.ARRAY_LIST_SET_MAP, LanguageType.RUBY);
    assertEquals("Set<Hash<String,LinkedListNode<Fixnum>>>[][]", typeStr5);


    final String typeStr6 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.BINARY_TREE_NODE_MAP, LanguageType.RUBY);
    assertEquals("BinaryTreeNode<Hash<String,Set<Float[]>>>", typeStr6);
  }
}
