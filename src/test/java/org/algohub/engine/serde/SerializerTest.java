package org.algohub.engine.serde;


import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.algohub.engine.collection.BinaryTreeNode;
import org.algohub.engine.collection.LinkedListNode;
import org.algohub.engine.type.TypeNode;
import org.algohub.engine.util.ObjectMapperInstance;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertEquals;

@SuppressWarnings({"PMD.CommentRequired"})
public class SerializerTest {

  public static final LinkedListNode<Integer> LINKED_LIST_EXPECTED = new LinkedListNode<>(1,
      new LinkedListNode<>(2,
          new LinkedListNode<>(3, new LinkedListNode<>(4, new LinkedListNode<>(5, null)))));

  public static final LinkedListNode<Integer> LINKED_LIST_EXPECTED2 = new LinkedListNode<>(6,
      new LinkedListNode<>(7,
          new LinkedListNode<>(8, new LinkedListNode<>(9, new LinkedListNode<>(10, null)))));

  public static final LinkedListNode<LinkedListNode<Integer>> LINKED_LIST_LINKED_LIST_EXPECTED =
      new LinkedListNode<>(LINKED_LIST_EXPECTED, new LinkedListNode<>(LINKED_LIST_EXPECTED2));

  public static final BinaryTreeNode<Integer> BINARY_TREE_EXPECTED = new BinaryTreeNode<>(2);

  static {
    BINARY_TREE_EXPECTED.left = new BinaryTreeNode<>(1);
    BINARY_TREE_EXPECTED.right = new BinaryTreeNode<>(10);
    BINARY_TREE_EXPECTED.left.leftIsNull = true;
    BINARY_TREE_EXPECTED.left.rightIsNull = true;
    BINARY_TREE_EXPECTED.right.left = new BinaryTreeNode<>(5);
  }

  @Test public void primitiveToJsonTest() {
    assertEquals(BooleanNode.TRUE, Serializer.toJson(true, TypeNode.fromString("bool")));
    assertEquals(IntNode.valueOf(123), Serializer.toJson(123, TypeNode.fromString("int")));
    assertEquals(LongNode.valueOf(123), Serializer.toJson(123L, TypeNode.fromString("long")));
    assertEquals(DoubleNode.valueOf(123.0),
        Serializer.toJson(123.0, TypeNode.fromString("double")));
    assertEquals(TextNode.valueOf("123"), Serializer.toJson("123", TypeNode.fromString("string")));
  }

  @Test public void collectionToJsonTest() {
    final ArrayNode intArrayNode = ObjectMapperInstance.INSTANCE.createArrayNode();
    intArrayNode.addAll(Arrays
        .asList(IntNode.valueOf(1), IntNode.valueOf(2), IntNode.valueOf(3), IntNode.valueOf(4),
            IntNode.valueOf(5)));
    final ArrayNode intArrayNode1 = ObjectMapperInstance.INSTANCE.createArrayNode();
    intArrayNode1.addAll(Arrays
        .asList(IntNode.valueOf(6), IntNode.valueOf(7), IntNode.valueOf(8), IntNode.valueOf(9),
            IntNode.valueOf(10)));

    assertEquals(intArrayNode,
        Serializer.toJson(new int[] {1, 2, 3, 4, 5}, TypeNode.fromString("array<int>")));

    assertEquals(intArrayNode, Serializer
        .toJson(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)), TypeNode.fromString("list<int>")));

    assertEquals(intArrayNode, Serializer
        .toJson(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)), TypeNode.fromString("set<int>")));

    assertEquals(intArrayNode,
        Serializer.toJson(LINKED_LIST_EXPECTED, TypeNode.fromString("LinkedListNode<int>")));

    final HashMap<String, Integer> mapStringInt = new HashMap<>();
    mapStringInt.put("hello", 1);
    mapStringInt.put("world", 2);
    final ObjectNode mapStringIntExpected = ObjectMapperInstance.INSTANCE.createObjectNode();
    mapStringIntExpected.set("hello", IntNode.valueOf(1));
    mapStringIntExpected.set("world", IntNode.valueOf(2));
    final ObjectNode mapStringIntActual =
        (ObjectNode) Serializer.toJson(mapStringInt, TypeNode.fromString("map<string, int>"));
    assertEquals(mapStringIntExpected, mapStringIntActual);

    final ObjectNode mapIntDoubleExpected = ObjectMapperInstance.INSTANCE.createObjectNode();
    mapIntDoubleExpected.set("1", DoubleNode.valueOf(1.0));
    mapIntDoubleExpected.set("2", DoubleNode.valueOf(2.0));
    final HashMap<Integer, Double> mapIntDouble = new HashMap<>();
    mapIntDouble.put(1, 1.0);
    mapIntDouble.put(2, 2.0);
    final ObjectNode mapIntDoubleActual =
        (ObjectNode) Serializer.toJson(mapIntDouble, TypeNode.fromString("map<int,double>"));
    assertEquals(mapIntDoubleExpected, mapIntDoubleActual);

    assertEquals("[2,1,10,null,null,5]",
        Serializer.toJson(BINARY_TREE_EXPECTED, TypeNode.fromString("BinaryTreeNode<int>")).toString());

    final ArrayNode arrayArrayNode = ObjectMapperInstance.INSTANCE.createArrayNode();
    arrayArrayNode.add(intArrayNode);
    arrayArrayNode.add(intArrayNode1);

    final ArrayList<ArrayList<Integer>> listListInt = new ArrayList<>();
    listListInt.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)));
    listListInt.add(new ArrayList<>(Arrays.asList(6, 7, 8, 9, 10)));

    assertEquals(arrayArrayNode, Serializer.toJson(new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}},
            TypeNode.fromString("array<array<int>>")));

    assertEquals(arrayArrayNode,
        Serializer.toJson(listListInt, TypeNode.fromString("list<list<int>>")));

    assertEquals(arrayArrayNode, Serializer
        .toJson(LINKED_LIST_LINKED_LIST_EXPECTED, TypeNode.fromString("LinkedListNode<LinkedListNode<int>>")));

    assertEquals(arrayArrayNode, Serializer
        .toJson(new LinkedListNode[] {LINKED_LIST_EXPECTED, LINKED_LIST_EXPECTED2},
            TypeNode.fromString("array<LinkedListNode<int>>")));

    final HashSet<LinkedListNode<Integer>> setLinkedList =
        new HashSet<>(Arrays.asList(LINKED_LIST_EXPECTED, LINKED_LIST_EXPECTED2));
    assertEquals(arrayArrayNode,
        Serializer.toJson(setLinkedList, TypeNode.fromString("set<LinkedListNode<int>>")));

    final ObjectNode mapStringLinkedListJsonExpected =
        ObjectMapperInstance.INSTANCE.createObjectNode();
    mapStringLinkedListJsonExpected.set("hello", intArrayNode);
    mapStringLinkedListJsonExpected.set("world", intArrayNode1);
    final HashMap<String, LinkedListNode<Integer>> mapStringLinkedList = new HashMap<>();
    mapStringLinkedList.put("hello", LINKED_LIST_EXPECTED);
    mapStringLinkedList.put("world", LINKED_LIST_EXPECTED2);
    final ObjectNode mapStringLinkedListJsonActual = (ObjectNode) Serializer
        .toJson(mapStringLinkedList, TypeNode.fromString("map<string, LinkedListNode<int>>"));
    assertEquals(mapStringLinkedListJsonExpected, mapStringLinkedListJsonActual);
  }
}
