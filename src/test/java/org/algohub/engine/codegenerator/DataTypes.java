package org.algohub.engine.codegenerator;

import org.algohub.engine.pojo.Function;
import org.algohub.engine.type.TypeNode;


public interface DataTypes {
  TypeNode ARRAY_INT = TypeNode.fromString("array<int>");
  TypeNode LIST_INT = TypeNode.fromString("list<int>");
  TypeNode SET_INT = TypeNode.fromString("set<int>");
  TypeNode LINKED_LIST_INT = TypeNode.fromString("LinkedListNode<int>");

  TypeNode MAP_STRING_INT = TypeNode.fromString("map<string, int>");
  TypeNode MAP_INT_DOUBLE = TypeNode.fromString("map<int, double>");

  TypeNode BINARY_TREE_INT = TypeNode.fromString("BinaryTreeNode<int>");

  TypeNode ARRAY_ARRAY_INT = TypeNode.fromString("array<array<int>>");
  TypeNode ARRAY_LIST_INT = TypeNode.fromString("array<list<int>>");
  TypeNode ARRAY_SET_INT = TypeNode.fromString("array<set<int>>");
  TypeNode ARRAY_LINKED_LIST_INT = TypeNode.fromString("array<LinkedListNode<int>>");
  TypeNode ARRAY_MAP_STRING_INT = TypeNode.fromString("array<map<string, int>>");
  TypeNode ARRAY_MAP_INT_DOUBLE = TypeNode.fromString("array<map<int, double>>");

  TypeNode LIST_ARRAY_INT = TypeNode.fromString("list<array<int>>");
  TypeNode LIST_LIST_INT = TypeNode.fromString("list<list<int>>");
  TypeNode LIST_SET_INT = TypeNode.fromString("list<set<int>>");
  TypeNode LIST_LINKED_LIST_INT = TypeNode.fromString("list<LinkedListNode<int>>");
  TypeNode LIST_MAP_STRING_INT = TypeNode.fromString("list<map<string, int>>");
  TypeNode LIST_MAP_INT_DOUBLE = TypeNode.fromString("list<map<int, double>>");

  TypeNode SET_ARRAY_INT = TypeNode.fromString("set<array<int>>");
  TypeNode SET_LIST_INT = TypeNode.fromString("set<list<int>>");
  TypeNode SET_LINKED_LIST_INT = TypeNode.fromString("set<LinkedListNode<int>>");
  TypeNode SET_SET_INT = TypeNode.fromString("set<set<int>>");
  TypeNode SET_MAP_STRING_INT = TypeNode.fromString("set<map<string, int>>");
  TypeNode SET_MAP_INT_DOUBLE = TypeNode.fromString("set<map<int, double>>");

  TypeNode LINKED_LIST_ARRAY_INT = TypeNode.fromString("LinkedListNode<array<int>>");
  TypeNode LINKED_LIST_LIST_INT = TypeNode.fromString("LinkedListNode<list<int>>");
  TypeNode LINKED_LIST_SET_INT = TypeNode.fromString("LinkedListNode<set<int>>");
  TypeNode LINKED_LIST_LINKED_LIST_INT = TypeNode.fromString("LinkedListNode<LinkedListNode<int>>");
  TypeNode LINKED_LIST_MAP_STRING_INT = TypeNode.fromString("LinkedListNode<map<string, int>>");
  TypeNode LINKED_LIST_MAP_INT_DOUBLE = TypeNode.fromString("LinkedListNode<map<int, double>>");

  TypeNode BINARY_TREE_ARRAY_INT = TypeNode.fromString("BinaryTreeNode<array<int>>");
  TypeNode BINARY_TREE_LIST_INT = TypeNode.fromString("BinaryTreeNode<list<int>>");
  TypeNode BINARY_TREE_SET_INT = TypeNode.fromString("BinaryTreeNode<set<int>>");
  TypeNode BINARY_TREE_LINKED_LIST_INT = TypeNode.fromString("BinaryTreeNode<LinkedListNode<int>>");
  TypeNode BINARY_TREE_MAP_STRING_INT = TypeNode.fromString("BinaryTreeNode<map<string, int>>");
  TypeNode BINARY_TREE_MAP_INT_DOUBLE = TypeNode.fromString("BinaryTreeNode<map<int, double>>");

  TypeNode MAP_STRING_LINKED_LIST_INT = TypeNode.fromString("map<string, LinkedListNode<int>>");
  TypeNode ARRAY_LIST_SET_MAP_STRING_LINKED_LIST_INT = TypeNode.fromString(
      "array<list<set<map<string, LinkedListNode<int>>>>>");
  TypeNode BINARY_TREE_MAP_STRING_SET_LIST_DOUBLE = TypeNode.fromString(
      "BinaryTreeNode<map<string, set<list<double>>>>");

  // two functions
  Function TWO_SUM = new Function("twoSum",
      new Function.Return(TypeNode.fromString("array<int>"),
          "[index1 + 1, index2 + 1] (index1 < index2)"), new Function.Parameter[] {
      new Function.Parameter("numbers", TypeNode.fromString("array<int>"), "An array of Integers"),
      new Function.Parameter("target", TypeNode.fromString("int"),
          "target = numbers[index1] + numbers[index2]")});

  Function WORD_LADDER = new Function("ladderLength",
      new Function.Return(TypeNode.fromString("int"), "The shortest length"),
      new Function.Parameter[] {
          new Function.Parameter("begin_word", TypeNode.fromString("string"), "the begin word"),
          new Function.Parameter("end_word", TypeNode.fromString("string"), "the end word"),
          new Function.Parameter("dict", TypeNode.fromString("set<string>"), "the dictionary")});
}
