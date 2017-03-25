package org.algohub.engine.codegenerator;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.algohub.engine.judge.CppJudge;
import org.algohub.engine.bo.StatusCode;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.pojo.Problem;
import org.algohub.engine.pojo.JudgeResult;
import org.algohub.engine.type.TypeNode;
import org.algohub.engine.util.ObjectMapperInstance;
import org.junit.Test;

import java.io.IOException;

import static org.algohub.engine.codegenerator.DataTypes.*;
import static org.algohub.engine.codegenerator.DataTypes.BINARY_TREE_MAP_STRING_INT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@SuppressWarnings({"PMD.CommentRequired"})
public class CppCodeGeneratorTest {
  private static final ObjectMapper OBJECT_MAPPER = ObjectMapperInstance.INSTANCE;

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
      Indentation.append(sb, typeDeclaration +
          " serde(const " + typeDeclaration + " &input) {\n", 0);
      Indentation.append(sb, "rapidjson::Document doc;\n", 1);
      Indentation.append(sb,
          "const rapidjson::Value serialized = to_json(input, doc.GetAllocator());\n", 1);
      Indentation.append(sb, typeDeclaration + " deserialized;\n", 1);
      Indentation.append(sb, "from_json(serialized, deserialized);\n", 1);
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
    assertEquals("vector<int>", CppCodeGenerator.generateTypeDeclaration(ARRAY_INT));
    assertEquals("vector<int>", CppCodeGenerator.generateTypeDeclaration(LIST_INT));
    assertEquals("unordered_set<int>", CppCodeGenerator.generateTypeDeclaration(SET_INT));
    assertEquals("shared_ptr<LinkedListNode<int>>",
        CppCodeGenerator.generateTypeDeclaration(LINKED_LIST_INT));

    assertEquals("unordered_map<string, int>",
        CppCodeGenerator.generateTypeDeclaration(MAP_STRING_INT));
    assertEquals("unordered_map<int, double>",
        CppCodeGenerator.generateTypeDeclaration(MAP_INT_DOUBLE));

    assertEquals("shared_ptr<BinaryTreeNode<int>>",
        CppCodeGenerator.generateTypeDeclaration(BINARY_TREE_INT));

    assertEquals("vector<vector<int>>", CppCodeGenerator.generateTypeDeclaration(
        ARRAY_ARRAY_INT));
    assertEquals("vector<vector<int>>", CppCodeGenerator.generateTypeDeclaration(
        ARRAY_LIST_INT));
    assertEquals("vector<unordered_set<int>>", CppCodeGenerator.generateTypeDeclaration(
        ARRAY_SET_INT));
    assertEquals("vector<shared_ptr<LinkedListNode<int>>>",
        CppCodeGenerator.generateTypeDeclaration(ARRAY_LINKED_LIST_INT));
    assertEquals("vector<unordered_map<string, int>>",
        CppCodeGenerator.generateTypeDeclaration(ARRAY_MAP_STRING_INT));
    assertEquals("vector<unordered_map<int, double>>",
        CppCodeGenerator.generateTypeDeclaration(ARRAY_MAP_INT_DOUBLE));

    assertEquals("vector<vector<int>>", CppCodeGenerator.generateTypeDeclaration(
        LIST_ARRAY_INT));
    assertEquals("vector<vector<int>>", CppCodeGenerator.generateTypeDeclaration(
        LIST_LIST_INT));
    assertEquals("vector<unordered_set<int>>", CppCodeGenerator.generateTypeDeclaration(
        LIST_SET_INT));
    assertEquals("vector<shared_ptr<LinkedListNode<int>>>",
        CppCodeGenerator.generateTypeDeclaration(LIST_LINKED_LIST_INT));
    assertEquals("vector<unordered_map<string, int>>",
        CppCodeGenerator.generateTypeDeclaration(LIST_MAP_STRING_INT));
    assertEquals("vector<unordered_map<int, double>>",
        CppCodeGenerator.generateTypeDeclaration(LIST_MAP_INT_DOUBLE));

    assertEquals("unordered_set<vector<int>>", CppCodeGenerator.generateTypeDeclaration(
        SET_ARRAY_INT));
    assertEquals("unordered_set<vector<int>>", CppCodeGenerator.generateTypeDeclaration(
        SET_LIST_INT));
    assertEquals("unordered_set<unordered_set<int>>",
        CppCodeGenerator.generateTypeDeclaration(SET_SET_INT));
    assertEquals("unordered_set<shared_ptr<LinkedListNode<int>>>",
        CppCodeGenerator.generateTypeDeclaration(SET_LINKED_LIST_INT));
    assertEquals("unordered_set<unordered_map<string, int>>",
        CppCodeGenerator.generateTypeDeclaration(SET_MAP_STRING_INT));
    assertEquals("unordered_set<unordered_map<int, double>>",
        CppCodeGenerator.generateTypeDeclaration(SET_MAP_INT_DOUBLE));

    assertEquals("shared_ptr<LinkedListNode<vector<int>>>",
        CppCodeGenerator.generateTypeDeclaration(LINKED_LIST_ARRAY_INT));
    assertEquals("shared_ptr<LinkedListNode<vector<int>>>",
        CppCodeGenerator.generateTypeDeclaration(LINKED_LIST_LIST_INT));
    assertEquals("shared_ptr<LinkedListNode<unordered_set<int>>>",
        CppCodeGenerator.generateTypeDeclaration(LINKED_LIST_SET_INT));
    assertEquals("shared_ptr<LinkedListNode<shared_ptr<LinkedListNode<int>>>>",
        CppCodeGenerator.generateTypeDeclaration(LINKED_LIST_LINKED_LIST_INT));
    assertEquals("shared_ptr<LinkedListNode<unordered_map<string, int>>>",
        CppCodeGenerator.generateTypeDeclaration(LINKED_LIST_MAP_STRING_INT));
    assertEquals("shared_ptr<LinkedListNode<unordered_map<int, double>>>",
        CppCodeGenerator.generateTypeDeclaration(LINKED_LIST_MAP_INT_DOUBLE));

    assertEquals("shared_ptr<BinaryTreeNode<vector<int>>>",
        CppCodeGenerator.generateTypeDeclaration(BINARY_TREE_ARRAY_INT));
    assertEquals("shared_ptr<BinaryTreeNode<vector<int>>>",
        CppCodeGenerator.generateTypeDeclaration(BINARY_TREE_LIST_INT));
    assertEquals("shared_ptr<BinaryTreeNode<unordered_set<int>>>",
        CppCodeGenerator.generateTypeDeclaration(BINARY_TREE_SET_INT));
    assertEquals("shared_ptr<BinaryTreeNode<shared_ptr<LinkedListNode<int>>>>",
        CppCodeGenerator.generateTypeDeclaration(BINARY_TREE_LINKED_LIST_INT));
    assertEquals("shared_ptr<BinaryTreeNode<unordered_map<string, int>>>",
        CppCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_STRING_INT));
    assertEquals("shared_ptr<BinaryTreeNode<unordered_map<int, double>>>",
        CppCodeGenerator.generateTypeDeclaration(BINARY_TREE_MAP_INT_DOUBLE));

    assertEquals("vector<vector<unordered_set<unordered_map<string, shared_ptr<LinkedListNode<int>>>>>>",
        CppCodeGenerator.generateTypeDeclaration(ARRAY_LIST_SET_MAP_STRING_LINKED_LIST_INT));

    assertEquals("shared_ptr<BinaryTreeNode<unordered_map<string, unordered_set<vector<double>>>>>",
        CppCodeGenerator
            .generateTypeDeclaration(BINARY_TREE_MAP_STRING_SET_LIST_DOUBLE));
  }

  @Test public void generateCppFunctionTest() {
    final String twoSumGenerated = CppCodeGenerator.generateEmptyFunction(TWO_SUM);
    final String twoSumExpected = "/**\n" + " * @param numbers An array of Integers\n"
        + " * @param target target = numbers[index1] + numbers[index2]\n"
        + " * @return [index1 + 1, index2 + 1] (index1 < index2)\n" + " */\n"
        + "vector<int> twoSum(vector<int>& numbers, int target) {\n"
        + "    // Write your code here\n" + "}\n";
    assertEquals(twoSumExpected, twoSumGenerated);


    final String wordLadderGenerated = CppCodeGenerator.generateEmptyFunction(WORD_LADDER);
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
    testSerde("map<string,int>", "{\"\\\"hello\\\"\":1, \"\\\"world\\\"\":2}");
    testSerde("map<int,double>", "{\"1\":1.0, \"2\":2.0}");
    testSerde("BinaryTreeNode<int>", "[2,1,10,null,null,5]");
    testSerde("array<array<int>>", arrayArrayJson);
    testSerde("LinkedListNode<LinkedListNode<int>>", arrayArrayJson);
    testSerde("array<LinkedListNode<int>>", arrayArrayJson);
    testSerde("set<LinkedListNode<int>>", arrayArrayJson);
    testSerde("map<string, LinkedListNode<int>>",
        "{\"\\\"hello\\\"\":[4,5,6],\"\\\"world\\\"\":[1,2,3]}");
  }
}
