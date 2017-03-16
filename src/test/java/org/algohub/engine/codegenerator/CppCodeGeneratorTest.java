package org.algohub.engine.codegenerator;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.algohub.engine.judge.CppJudge;
import org.algohub.engine.bo.StatusCode;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.type.TypeNode;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SuppressWarnings({"PMD.CommentRequired"})
public class CppCodeGeneratorTest {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  private static void testSerde(final String typeStr, final String oneTestCase) {
    final TypeNode type = TypeNode.fromString(typeStr);
    final Problem.TestCase[] testCases = new Problem.TestCase[1];
    try {
      testCases[0] = OBJECT_MAPPER.readValue("{\"input\":[" + oneTestCase + "],\"output\":" +
          oneTestCase + "}", Problem.TestCase.class);
    } catch (IOException e) {
      fail(e.getMessage());
    }

    final String solution;
    {
      final StringBuilder sb = new StringBuilder();
      final String typeDeclaration = CppCodeGenerator.generateTypeDeclaration(type);
      Indentation
          .append(sb, typeDeclaration + " serde(const " + typeDeclaration + " &input) {\n", 0);
      Indentation.append(sb, "const string serialized = to_json(input);\n", 1);
      Indentation.append(sb, "rapidjson::Document doc;\n", 1);
      Indentation.append(sb, "doc.Parse(serialized.c_str());\n", 1);
      Indentation.append(sb, typeDeclaration + " deserialized;\n", 1);
      Indentation.append(sb, "from_json(doc, deserialized);\n", 1);
      Indentation.append(sb, "return std::move(deserialized);\n", 1);
      Indentation.append(sb, "}\n", 0);
      solution = sb.toString();
    }
    final Function function;
    {
      final Function.Return returnType = new Function.Return(type, "");
      final Function.Parameter[] parameters = new Function.Parameter[1];
      parameters[0] = new Function.Parameter("input", type, "");
      function = new Function("serde", returnType, parameters);
    }
    final CppJudge judge = new CppJudge();
    final JudgeResult result = judge.judge(function, testCases, solution);
    assertEquals(StatusCode.ACCEPTED, result.getStatusCode());
  }

  @Test public void generateTypeDeclarationTest() {
    final String typeStr1 = FunctionGenerator
        .generateTypeDeclaration(JavaCodeGeneratorTest.ARRAY_INT, LanguageType.CPLUSPLUS);
    assertEquals("vector<int>", typeStr1);


    final String typeStr2 = FunctionGenerator
        .generateTypeDeclaration(JavaCodeGeneratorTest.LIST_INT, LanguageType.CPLUSPLUS);
    assertEquals("vector<int>", typeStr2);

    final String typeStr3 = FunctionGenerator
        .generateTypeDeclaration(JavaCodeGeneratorTest.LIST_ARRAY_INT, LanguageType.CPLUSPLUS);
    assertEquals("vector<vector<int>>", typeStr3);


    final String typeStr4 = FunctionGenerator
        .generateTypeDeclaration(JavaCodeGeneratorTest.LIST_LIST_INT, LanguageType.CPLUSPLUS);
    assertEquals("vector<vector<int>>", typeStr4);


    final String typeStr5 = FunctionGenerator
        .generateTypeDeclaration(JavaCodeGeneratorTest.ARRAY_LIST_SET_MAP, LanguageType.CPLUSPLUS);
    assertEquals("vector<vector<unordered_set<unordered_map<string,LinkedListNode<int>>>>>",
        typeStr5);


    final String typeStr6 = FunctionGenerator
        .generateTypeDeclaration(JavaCodeGeneratorTest.BINARY_TREE_NODE_MAP, LanguageType.CPLUSPLUS);
    assertEquals("BinaryTreeNode<unordered_map<string,unordered_set<vector<double>>>>", typeStr6);
  }

  @Test public void generateEmptyFunctionTest() {
    final String twoSumGenerated =
        CppCodeGenerator.generateEmptyFunction(FunctionGeneratorTest.TWO_SUM);
    final String twoSumExpected = "/**\n" + " * @param numbers An array of Integers\n"
        + " * @param target target = numbers[index1] + numbers[index2]\n"
        + " * @return [index1 + 1, index2 + 1] (index1 < index2)\n" + " */\n"
        + "vector<int> twoSum(vector<int>& numbers, int target) {\n"
        + "    // Write your code here\n" + "}\n";
    assertEquals(twoSumExpected, twoSumGenerated);


    final String wordLadderGenerated =
        CppCodeGenerator.generateEmptyFunction(FunctionGeneratorTest.WORD_LADDER);
    final String wordLadderExpected =
        "/**\n" + " * @param begin_word the begin word\n" + " * @param end_word the end word\n"
            + " * @param dict the dictionary\n" + " * @return The shortest length\n" + " */\n"
            + "int ladderLength(string& begin_word, string& end_word, unordered_set<string>& "
            + "dict) {\n"
            + "    // Write your code here\n" + "}\n";
    assertEquals(wordLadderExpected, wordLadderGenerated);
  }

  @Test public void serDeTest() {
    final String arrayJson = "[1,2,3,4,5]";
    final String arrayArrayJson = "[[1,2,3,4,5],[6,7,8,9,10]]";

    testSerde("array<int>", arrayJson);
    testSerde("list<int>", arrayJson);
    testSerde("set<int>", arrayJson);
    testSerde("LinkedListNode<int>", arrayJson);
    testSerde("map<string,int>", "{\"hello\":1, \"world\":2}");
    testSerde("map<int,double>", "{\"1\":1.0, \"2\":2.0}");
    testSerde("BinaryTreeNode<int>", "[2,1,10,null,null,5]");
    testSerde("array<array<int>>", arrayArrayJson);
    testSerde("LinkedListNode<LinkedListNode<int>>", arrayArrayJson);
    testSerde("array<LinkedListNode<int>>", arrayArrayJson);
    testSerde("set<LinkedListNode<int>>", arrayArrayJson);
    testSerde("map<string, LinkedListNode<int>>", "{\"hello\":[4,5,6],\"world\":[1,2,3]}");
  }
}
