package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import org.algohub.engine.type.TypeNode;
import org.junit.Test;

import static org.algohub.engine.codegenerator.DataTypes.*;
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
    assertEquals(arrayIntJson, Serializer.toJson(arrayInt, ARRAY_INT));
    assertEquals(arrayIntJson, Serializer.toJson(listInt, LIST_INT));
    assertEquals(arrayIntJson, Serializer.toJson(setInt, SET_INT));
    assertEquals(arrayIntJson, Serializer.toJson(linkedListInt, LINKED_LIST_INT));
    // empty linked list
    assertEquals(emptyArrayJson, Serializer.toJson(null, LINKED_LIST_INT));

    final ObjectNode mapStringIntActual =
        (ObjectNode) Serializer.toJson(mapStringInt, MAP_STRING_INT);
    assertEquals(mapStringIntJson, mapStringIntActual);
    final ObjectNode mapIntDoubleActual =
        (ObjectNode) Serializer.toJson(mapIntDouble, MAP_INT_DOUBLE);
    assertEquals(mapIntDoubleJson, mapIntDoubleActual);

    // empty binary tree
    assertEquals(emptyArrayJson, Serializer.toJson(null, BINARY_TREE_INT));
    assertEquals(binaryTreeJson, Serializer.toJson(binaryTree, BINARY_TREE_INT));

    assertEquals(arrayArrayIntJson, Serializer.toJson(arrayArrayInt, ARRAY_ARRAY_INT));
    assertEquals(arrayArrayIntJson, Serializer.toJson(listListInt, LIST_LIST_INT));
    assertEquals(arrayArrayIntJson, Serializer.toJson(linkedListLinkedListInt,
        LINKED_LIST_LINKED_LIST_INT));
    assertEquals(arrayArrayIntJson, Serializer.toJson(arrayLinkedListInt, ARRAY_LINKED_LIST_INT));
    assertEquals(arrayArrayIntJson, Serializer.toJson(setLinkedListInt, SET_LINKED_LIST_INT));

    final ObjectNode mapStringLinkedListJsonActual = (ObjectNode) Serializer.toJson(
        mapStringLinkedListInt, MAP_STRING_LINKED_LIST_INT);
    assertEquals(mapStringLinkedListIntJson, mapStringLinkedListJsonActual);
  }
}
