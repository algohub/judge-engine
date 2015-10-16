package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.algohub.engine.collection.LinkedListNode;
import org.algohub.engine.type.TypeNode;
import org.algohub.engine.util.ObjectMapperInstance;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@SuppressWarnings({"PMD.CommentRequired"})
public class DeserializerTest {

  @Test public void deserializePrimitiveTest() {
    assertEquals(Boolean.TRUE,
        Deserializer.fromJson(TypeNode.fromString("bool"), BooleanNode.TRUE));

    assertEquals(Integer.valueOf(123),
        Deserializer.fromJson(TypeNode.fromString("int"), IntNode.valueOf(123)));

    assertEquals(Long.valueOf(123),
        Deserializer.fromJson(TypeNode.fromString("long"), IntNode.valueOf(123)));

    assertEquals(Double.valueOf(123.0),
        Deserializer.fromJson(TypeNode.fromString("double"), DoubleNode.valueOf(123.0)));

    assertEquals("algohub",
        Deserializer.fromJson(TypeNode.fromString("string"), TextNode.valueOf("algohub")));
  }

  @Test public void deserializeCollectionTest() {
    final ArrayNode intArray = ObjectMapperInstance.INSTANCE.createArrayNode();
    intArray.add(1);
    intArray.add(2);
    intArray.add(3);
    intArray.add(4);
    intArray.add(5);

    assertArrayEquals(new int[] {1, 2, 3, 4, 5},
        (int[]) Deserializer.fromJson(TypeNode.fromString("array<int>"), intArray));

    assertEquals(Arrays.asList(1, 2, 3, 4, 5),
        Deserializer.fromJson(TypeNode.fromString("list<int>"), intArray));


    assertEquals(new HashSet<>(Arrays.asList(1, 2, 3, 4, 5)),
        Deserializer.fromJson(TypeNode.fromString("set<int>"), intArray));


    assertEquals(SerializerTest.LINKED_LIST_EXPECTED,
        Deserializer.fromJson(TypeNode.fromString("LinkedListNode<int>"), intArray));

    final HashMap<String, Integer> mapStringIntExpected = new HashMap<>();
    mapStringIntExpected.put("hello", 1);
    mapStringIntExpected.put("world", 2);
    final ObjectNode mapStringIntNode = ObjectMapperInstance.INSTANCE.createObjectNode();
    mapStringIntNode.set("hello", IntNode.valueOf(1));
    mapStringIntNode.set("world", IntNode.valueOf(2));
    assertEquals(mapStringIntExpected,
        Deserializer.fromJson(TypeNode.fromString("map<string, int>"), mapStringIntNode));

    final HashMap<Integer, Double> mapIntDoubleExpected = new HashMap<>();
    mapIntDoubleExpected.put(1, 1.0);
    mapIntDoubleExpected.put(2, 2.0);
    final ObjectNode mapIntDoubleNode = ObjectMapperInstance.INSTANCE.createObjectNode();
    mapIntDoubleNode.set("1", DoubleNode.valueOf(1.0));
    mapIntDoubleNode.set("2", DoubleNode.valueOf(2.0));
    assertEquals(mapIntDoubleExpected,
        Deserializer.fromJson(TypeNode.fromString("map<int,double>"), mapIntDoubleNode));

    final ArrayNode treeArray = ObjectMapperInstance.INSTANCE.createArrayNode();
    treeArray.add(2);
    treeArray.add(1);
    treeArray.add(10);
    treeArray.add(NullNode.instance);
    treeArray.add(NullNode.instance);
    treeArray.add(5);
    assertEquals(SerializerTest.BINARY_TREE_EXPECTED,
        Deserializer.fromJson(TypeNode.fromString("BinaryTreeNode<int>"), treeArray));

    final ArrayNode arrayArrayNode = ObjectMapperInstance.INSTANCE.createArrayNode();
    for (int i = 0; i < 2; ++i) {
      ArrayNode tmp = ObjectMapperInstance.INSTANCE.createArrayNode();
      for (int j = 1; j < 6; ++j) {
        tmp.add(i * 5 + j);
      }
      arrayArrayNode.add(tmp);
    }

    final int[][] arrayArrayIntExpected = new int[][] {{1, 2, 3, 4, 5}, {6, 7, 8, 9, 10}};
    final int[][] arrayArrayIntActual =
        (int[][]) Deserializer.fromJson(TypeNode.fromString("array<array<int>>"), arrayArrayNode);
    assertArrayEquals(arrayArrayIntExpected[0], arrayArrayIntActual[0]);
    assertArrayEquals(arrayArrayIntExpected[1], arrayArrayIntActual[1]);

    final LinkedListNode<LinkedListNode<Integer>> linkedList2Actual = (LinkedListNode) Deserializer
        .fromJson(TypeNode.fromString("LinkedListNode<linkedListNode<int>>"), arrayArrayNode);
    assertEquals(SerializerTest.LINKED_LIST_LINKED_LIST_EXPECTED, linkedList2Actual);

    final LinkedListNode<Integer>[] arrayLinkedListExpected =
        new LinkedListNode[] {SerializerTest.LINKED_LIST_EXPECTED,
            SerializerTest.LINKED_LIST_EXPECTED2};
    final LinkedListNode<Integer>[] arrayLinkedListActual = (LinkedListNode[]) Deserializer
        .fromJson(TypeNode.fromString("array<linkedListNode<int>>"), arrayArrayNode);
    assertArrayEquals(arrayLinkedListExpected, arrayLinkedListActual);

    final HashSet<LinkedListNode<Integer>> setLinkedListExpected = new HashSet<>(
        Arrays.asList(SerializerTest.LINKED_LIST_EXPECTED, SerializerTest.LINKED_LIST_EXPECTED2));
    final HashSet<LinkedListNode<Integer>> setLinkedListActual = (HashSet) Deserializer
        .fromJson(TypeNode.fromString("set<LinkedListNode<int>>"), arrayArrayNode);
    assertEquals(setLinkedListExpected, setLinkedListActual);

    final HashMap<String, LinkedListNode<Integer>> mapStringLinkedListExpected = new HashMap<>();
    mapStringLinkedListExpected.put("hello", SerializerTest.LINKED_LIST_EXPECTED);
    mapStringLinkedListExpected.put("world", SerializerTest.LINKED_LIST_EXPECTED2);
    final ObjectNode mapStringLinkedListNode = ObjectMapperInstance.INSTANCE.createObjectNode();
    mapStringLinkedListNode.set("hello", arrayArrayNode.get(0));
    mapStringLinkedListNode.set("world", arrayArrayNode.get(1));
    final HashMap<String, LinkedListNode<Integer>> mapStringLinkedListActual =
        (HashMap) Deserializer.fromJson(TypeNode.fromString("map<string, LinkedListNode<int>>"),
            mapStringLinkedListNode);
    assertEquals(mapStringLinkedListExpected, mapStringLinkedListActual);
  }
}
