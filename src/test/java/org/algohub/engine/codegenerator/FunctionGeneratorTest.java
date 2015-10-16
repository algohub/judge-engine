package org.algohub.engine.codegenerator;


import org.algohub.engine.pojo.Function;
import org.algohub.engine.type.LanguageType;
import org.algohub.engine.type.TypeNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"PMD.CommentRequired"})
public class FunctionGeneratorTest {

  public static final Function TWO_SUM = new Function("twoSum",
      new Function.Return(TypeNode.fromString("array<int>"),
          "[index1 + 1, index2 + 1] (index1 < index2)"), new Function.Parameter[] {
      new Function.Parameter("numbers", TypeNode.fromString("array<int>"), "An array of Integers"),
      new Function.Parameter("target", TypeNode.fromString("int"),
          "target = numbers[index1] + numbers[index2]")});

  public static final Function WORD_LADDER = new Function("ladderLength",
      new Function.Return(TypeNode.fromString("int"), "The shortest length"),
      new Function.Parameter[] {
          new Function.Parameter("begin_word", TypeNode.fromString("string"), "the begin word"),
          new Function.Parameter("end_word", TypeNode.fromString("string"), "the end word"),
          new Function.Parameter("dict", TypeNode.fromString("set<string>"), "the dictionary")});

  @Test public void generateTypeDeclarationTest() {
    final TypeNode arrayInt = TypeNode.fromString("array<int>");
    final String arrayIntJavaDeclaration =
        FunctionGenerator.generateTypeDeclaration(arrayInt, LanguageType.JAVA);
    final String arrayIntCppDeclaration =
        FunctionGenerator.generateTypeDeclaration(arrayInt, LanguageType.CPLUSPLUS);
    final String arrayIntPythonDeclaration =
        FunctionGenerator.generateTypeDeclaration(arrayInt, LanguageType.PYTHON);
    final String arrayIntRubyDeclaration =
        FunctionGenerator.generateTypeDeclaration(arrayInt, LanguageType.RUBY);
    assertEquals("int[]", arrayIntJavaDeclaration);
    assertEquals("vector<int>", arrayIntCppDeclaration);
    assertEquals("int[]", arrayIntPythonDeclaration);
    assertEquals("Fixnum[]", arrayIntRubyDeclaration);

    final TypeNode complexType = TypeNode.fromString("list<map<string, array<int>>>");
    final String complexJavaTypeDeclaration =
        FunctionGenerator.generateTypeDeclaration(complexType, LanguageType.JAVA);
    final String complexCppTypeDeclaration =
        FunctionGenerator.generateTypeDeclaration(complexType, LanguageType.CPLUSPLUS);
    final String complexPythonTypeDeclaration =
        FunctionGenerator.generateTypeDeclaration(complexType, LanguageType.PYTHON);
    final String complexRubyTypeDeclaration =
        FunctionGenerator.generateTypeDeclaration(complexType, LanguageType.RUBY);
    assertEquals("ArrayList<HashMap<String,int[]>>", complexJavaTypeDeclaration);
    assertEquals("vector<unordered_map<string,vector<int>>>", complexCppTypeDeclaration);
    assertEquals("dict<string,int[]>[]", complexPythonTypeDeclaration);
    assertEquals("Hash<String,Fixnum[]>[]", complexRubyTypeDeclaration);
  }
}
