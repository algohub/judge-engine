package org.algohub.engine.codegenerator;

import org.algohub.engine.type.LanguageType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"PMD.CommentRequired"})
public class PythonCodeGeneratorTest {

  @Test public void generatePythonFunctiontTest() {
    final String twoSumExpected = "# @param {int[]} numbers An array of Integers\n"
        + "# @param {int} target target = numbers[index1] + numbers[index2]\n"
        + "# @return {int[]} [index1 + 1, index2 + 1] (index1 < index2)\n"
        + "def twoSum(numbers, target):\n    # Write your code here\n";
    final String twoSumGenerated =
        PythonCodeGenerator.generateEmptyFunction(FunctionGeneratorTest.TWO_SUM, 0);
    assertEquals(twoSumExpected, twoSumGenerated);

    final String wordLadderExpected = "# @param {string} begin_word the begin word\n"
        + "# @param {string} end_word the end word\n"
        + "# @param {set<string>} dict the dictionary\n" + "# @return {int} The shortest length\n"
        + "def ladderLength(begin_word, end_word, dict):\n    # Write your code here\n";
    final String wordLadderGenerated =
        PythonCodeGenerator.generateEmptyFunction(FunctionGeneratorTest.WORD_LADDER, 0);
    assertEquals(wordLadderExpected, wordLadderGenerated);
  }

  @Test public void generateTypeDeclarationTest() {
    final String typeStr1 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.ARRAY_INT, LanguageType.PYTHON);
    assertEquals("int[]", typeStr1);


    final String typeStr2 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.LIST_INT, LanguageType.PYTHON);
    assertEquals("int[]", typeStr2);

    final String typeStr3 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.LIST_ARRAY_INT, LanguageType.PYTHON);
    assertEquals("int[][]", typeStr3);


    final String typeStr4 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.LIST_LIST_INT, LanguageType.PYTHON);
    assertEquals("int[][]", typeStr4);


    final String typeStr5 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.ARRAY_LIST_SET_MAP, LanguageType.PYTHON);
    assertEquals("set<dict<string,LinkedListNode<int>>>[][]", typeStr5);


    final String typeStr6 =
        FunctionGenerator.generateTypeDeclaration(JavaCodeGeneratorTest.BINARY_TREE_NODE_MAP, LanguageType.PYTHON);
    assertEquals("BinaryTreeNode<dict<string,set<double[]>>>", typeStr6);
  }
}
