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

import static org.algohub.engine.codegenerator.DataTypes.*;
import static org.algohub.engine.serde.SharedData.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;


@SuppressWarnings({"PMD.CommentRequired"})
public class DeserializerTest {

  @Test public void deserializePrimitiveTest() {
    assertEquals(Boolean.TRUE,
        Deserializer.fromJson(TypeNode.fromString("bool"), BooleanNode.TRUE));
    assertEquals(Boolean.FALSE,
        Deserializer.fromJson(TypeNode.fromString("bool"), BooleanNode.FALSE));

    assertEquals('a',
        Deserializer.fromJson(TypeNode.fromString("char"), TextNode.valueOf("a")));

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
    assertArrayEquals(arrayInt, (int[]) Deserializer.fromJson(ARRAY_INT, arrayIntJson));
    assertEquals(listInt, Deserializer.fromJson(LIST_INT, arrayIntJson));
    assertEquals(setInt, Deserializer.fromJson(SET_INT, arrayIntJson));
    assertEquals(linkedListInt, Deserializer.fromJson(LINKED_LIST_INT, arrayIntJson));

    // empty linked list
    assertEquals(null, Deserializer.fromJson(LINKED_LIST_INT, emptyArrayJson));

    assertEquals(mapStringInt, Deserializer.fromJson(MAP_STRING_INT, mapStringIntJson));
    assertEquals(mapIntDouble, Deserializer.fromJson(MAP_INT_DOUBLE, mapIntDoubleJson));

    // empty binary tree
    assertEquals(null, Deserializer.fromJson(BINARY_TREE_INT, emptyArrayJson));
    assertEquals(binaryTree, Deserializer.fromJson(BINARY_TREE_INT, binaryTreeJson));

    final int[][] arrayArrayIntActual = (int[][]) Deserializer.fromJson(
        ARRAY_ARRAY_INT, arrayArrayIntJson);
    assertArrayEquals(arrayArrayInt[0], arrayArrayIntActual[0]);
    assertArrayEquals(arrayArrayInt[1], arrayArrayIntActual[1]);

    final ArrayList<ArrayList<Integer>> listListIntActual =
        (ArrayList<ArrayList<Integer>>) Deserializer.fromJson(LIST_LIST_INT, arrayArrayIntJson);
    assertEquals(listListInt, listListIntActual);

    final LinkedListNode<LinkedListNode<Integer>> linkedListLinkedListIntActual =
        (LinkedListNode) Deserializer.fromJson(LINKED_LIST_LINKED_LIST_INT, arrayArrayIntJson);
    assertEquals(linkedListLinkedListInt, linkedListLinkedListIntActual);

    final LinkedListNode<Integer>[] arrayLinkedListIntActual = (LinkedListNode[]) Deserializer
        .fromJson(ARRAY_LINKED_LIST_INT, arrayArrayIntJson);
    assertArrayEquals(arrayLinkedListInt, arrayLinkedListIntActual);

    final HashSet<LinkedListNode<Integer>> setLinkedListIntActual = (HashSet) Deserializer.fromJson(
        SET_LINKED_LIST_INT, arrayArrayIntJson);
    assertEquals(setLinkedListInt, setLinkedListIntActual);

    final HashMap<String, LinkedListNode<Integer>> mapStringLinkedListIntActual =
        (HashMap<String, LinkedListNode<Integer>>) Deserializer.fromJson(
            MAP_STRING_LINKED_LIST_INT, mapStringLinkedListIntJson);
    assertEquals(mapStringLinkedListInt, mapStringLinkedListIntActual);
  }
}
