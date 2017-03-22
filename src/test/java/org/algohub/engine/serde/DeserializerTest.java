package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.ArrayList;
import org.algohub.engine.collection.LinkedListNode;
import org.algohub.engine.type.TypeNode;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.algohub.engine.serde.SharedData.*;
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
    assertArrayEquals(arrayInt,
        (int[]) Deserializer.fromJson(TypeNode.fromString("array<int>"), arrayIntJson));
    assertEquals(listInt,
        Deserializer.fromJson(TypeNode.fromString("list<int>"), arrayIntJson));
    assertEquals(setInt,
        Deserializer.fromJson(TypeNode.fromString("set<int>"), arrayIntJson));
    assertEquals(linkedListInt,
        Deserializer.fromJson(TypeNode.fromString("LinkedListNode<int>"), arrayIntJson));

    // empty linked list
    assertEquals(null,
        Deserializer.fromJson(TypeNode.fromString("LinkedListNode<int>"), emptyArrayJson));

    assertEquals(mapStringInt,
        Deserializer.fromJson(TypeNode.fromString("map<string, int>"), mapStringIntJson));
    assertEquals(mapIntDouble,
        Deserializer.fromJson(TypeNode.fromString("map<int, double>"), mapIntDoubleJson));

    // empty binary tree
    assertEquals(null,
        Deserializer.fromJson(TypeNode.fromString("BinaryTreeNode<int>"), emptyArrayJson));
    assertEquals(binaryTree,
        Deserializer.fromJson(TypeNode.fromString("BinaryTreeNode<int>"), binaryTreeJson));

    final int[][] arrayArrayIntActual = (int[][]) Deserializer.fromJson(
        TypeNode.fromString("array<array<int>>"), arrayArrayIntJson);
    assertArrayEquals(arrayArrayInt[0], arrayArrayIntActual[0]);
    assertArrayEquals(arrayArrayInt[1], arrayArrayIntActual[1]);

    final ArrayList<ArrayList<Integer>> listListIntActual =
        (ArrayList<ArrayList<Integer>>) Deserializer.fromJson(
            TypeNode.fromString("list<list<int>>"), arrayArrayIntJson);
    assertEquals(listListInt, listListIntActual);

    final LinkedListNode<LinkedListNode<Integer>> linkedListLinkedListIntActual =
        (LinkedListNode) Deserializer.fromJson(
            TypeNode.fromString("LinkedListNode<linkedListNode<int>>"), arrayArrayIntJson);
    assertEquals(linkedListLinkedListInt, linkedListLinkedListIntActual);

    final LinkedListNode<Integer>[] arrayLinkedListIntActual = (LinkedListNode[]) Deserializer
        .fromJson(TypeNode.fromString("array<linkedListNode<int>>"), arrayArrayIntJson);
    assertArrayEquals(arrayLinkedListInt, arrayLinkedListIntActual);

    final HashSet<LinkedListNode<Integer>> setLinkedListIntActual = (HashSet) Deserializer.fromJson(
        TypeNode.fromString("set<LinkedListNode<int>>"), arrayArrayIntJson);
    assertEquals(setLinkedListInt, setLinkedListIntActual);

    final HashMap<String, LinkedListNode<Integer>> mapStringLinkedListIntActual =
        (HashMap<String, LinkedListNode<Integer>>) Deserializer.fromJson(
            TypeNode.fromString("map<string, LinkedListNode<int>>"), mapStringLinkedListIntJson);
    assertEquals(mapStringLinkedListInt, mapStringLinkedListIntActual);
  }
}
