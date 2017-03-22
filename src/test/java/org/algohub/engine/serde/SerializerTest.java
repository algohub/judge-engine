package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.algohub.engine.type.TypeNode;
import org.junit.Test;

import static org.algohub.engine.serde.SharedData.*;
import static org.junit.Assert.assertEquals;


@SuppressWarnings({"PMD.CommentRequired"})
public class SerializerTest {

  @Test public void primitiveToJsonTest() {
    assertEquals(BooleanNode.TRUE, Serializer.toJson(true, TypeNode.fromString("bool")));
    assertEquals(BooleanNode.FALSE, Serializer.toJson(false, TypeNode.fromString("bool")));

    assertEquals(TextNode.valueOf("a"),
        Serializer.toJson('a', TypeNode.fromString("char")));

    assertEquals(IntNode.valueOf(123), Serializer.toJson(123, TypeNode.fromString("int")));

    assertEquals(LongNode.valueOf(123), Serializer.toJson(123L, TypeNode.fromString("long")));

    assertEquals(DoubleNode.valueOf(123.0),
        Serializer.toJson(123.0, TypeNode.fromString("double")));

    assertEquals(TextNode.valueOf("123"),
        Serializer.toJson("123", TypeNode.fromString("string")));
  }

  @Test public void collectionToJsonTest() {
    assertEquals(arrayIntJson, Serializer.toJson(arrayInt, TypeNode.fromString("array<int>")));
    assertEquals(arrayIntJson, Serializer.toJson(listInt, TypeNode.fromString("list<int>")));
    assertEquals(arrayIntJson, Serializer.toJson(setInt, TypeNode.fromString("set<int>")));
    assertEquals(arrayIntJson,
        Serializer.toJson(linkedListInt, TypeNode.fromString("LinkedListNode<int>")));

    // empty linked list
    assertEquals(emptyArrayJson,
        Serializer.toJson(null, TypeNode.fromString("LinkedListNode<int>")));

    final ObjectNode mapStringIntActual =
        (ObjectNode) Serializer.toJson(mapStringInt, TypeNode.fromString("map<string, int>"));
    assertEquals(mapStringIntJson, mapStringIntActual);
    final ObjectNode mapIntDoubleActual =
        (ObjectNode) Serializer.toJson(mapIntDouble, TypeNode.fromString("map<int,double>"));
    assertEquals(mapIntDoubleJson, mapIntDoubleActual);

    // empty binary tree
    assertEquals(emptyArrayJson,
        Serializer.toJson(null, TypeNode.fromString("BinaryTreeNode<int>")));
    assertEquals(binaryTreeJson,
        Serializer.toJson(binaryTree, TypeNode.fromString("BinaryTreeNode<int>")));

    assertEquals(arrayArrayIntJson,
        Serializer.toJson(arrayArrayInt, TypeNode.fromString("array<array<int>>")));
    assertEquals(arrayArrayIntJson,
        Serializer.toJson(listListInt, TypeNode.fromString("list<list<int>>")));
    assertEquals(arrayArrayIntJson, Serializer.toJson(linkedListLinkedListInt,
        TypeNode.fromString("LinkedListNode<LinkedListNode<int>>")));
    assertEquals(arrayArrayIntJson, Serializer.toJson(arrayLinkedListInt,
        TypeNode.fromString("array<LinkedListNode<int>>")));
    assertEquals(arrayArrayIntJson, Serializer.toJson(setLinkedListInt,
        TypeNode.fromString("set<LinkedListNode<int>>")));

    final ObjectNode mapStringLinkedListJsonActual = (ObjectNode) Serializer
        .toJson(mapStringLinkedListInt, TypeNode.fromString("map<string, LinkedListNode<int>>"));
    assertEquals(mapStringLinkedListIntJson, mapStringLinkedListJsonActual);
  }
}
