package org.algohub.engine.serde;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import org.algohub.engine.collection.BinaryTreeNode;
import org.algohub.engine.collection.LinkedListNode;
import org.algohub.engine.util.ObjectMapperInstance;

final class SharedData {
  static final ArrayNode arrayIntJson = ObjectMapperInstance.INSTANCE.createArrayNode();
  static final int[] arrayInt =  {1, 2, 3, 4, 5};
  static final ArrayList<Integer> listInt = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5));
  static final HashSet<Integer> setInt = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
  static final LinkedListNode<Integer> linkedListInt = new LinkedListNode<>(1,
      new LinkedListNode<>(2,
          new LinkedListNode<>(3,
              new LinkedListNode<>(4,
                  new LinkedListNode<>(5)))));

  static final HashMap<String, Integer> mapStringInt = new HashMap<>();
  static final ObjectNode mapStringIntJson = ObjectMapperInstance.INSTANCE.createObjectNode();

  static final HashMap<Integer, Double> mapIntDouble = new HashMap<>();
  static final ObjectNode mapIntDoubleJson = ObjectMapperInstance.INSTANCE.createObjectNode();

  static final BinaryTreeNode<Integer> binaryTree = new BinaryTreeNode<>(2);
  static final ArrayNode binaryTreeJson = ObjectMapperInstance.INSTANCE.createArrayNode();

  private static final ArrayNode arrayIntJson2 = ObjectMapperInstance.INSTANCE.createArrayNode();
  private static final int[] arrayInt2 = {6, 7, 8, 9, 10};
  private static final ArrayList<Integer> listInt2 = new ArrayList<>(Arrays.asList(6, 7, 8, 9, 10));
  private static final LinkedListNode<Integer> linkedListInt2 = new LinkedListNode<>(6,
      new LinkedListNode<>(7,
          new LinkedListNode<>(8,
              new LinkedListNode<>(9,
                  new LinkedListNode<>(10)))));

  static final ArrayNode arrayArrayIntJson = ObjectMapperInstance.INSTANCE.createArrayNode();
  static final int[][] arrayArrayInt = new int[][] {arrayInt, arrayInt2};
  static final ArrayList<ArrayList<Integer>> listListInt = new ArrayList<>();
  static final LinkedListNode<LinkedListNode<Integer>> linkedListLinkedListInt =
      new LinkedListNode<>(linkedListInt, new LinkedListNode<>(linkedListInt2));
  static final LinkedListNode<Integer>[] arrayLinkedListInt =
      new LinkedListNode[] {linkedListInt, linkedListInt2};
  static final HashSet<LinkedListNode<Integer>> setLinkedListInt =
      new HashSet<>(Arrays.asList(linkedListInt, linkedListInt2));

  static final HashMap<String, LinkedListNode<Integer>> mapStringLinkedListInt = new HashMap<>();
  static final ObjectNode mapStringLinkedListIntJson =
      ObjectMapperInstance.INSTANCE.createObjectNode();

  // for empty linked list and empty binary tree
  static final ArrayNode emptyArrayJson = ObjectMapperInstance.INSTANCE.createArrayNode();



  static {
    arrayIntJson.addAll(Arrays.asList(IntNode.valueOf(1), IntNode.valueOf(2), IntNode.valueOf(3),
        IntNode.valueOf(4), IntNode.valueOf(5)));
    arrayIntJson2.addAll(Arrays.asList(IntNode.valueOf(6), IntNode.valueOf(7), IntNode.valueOf(8),
        IntNode.valueOf(9), IntNode.valueOf(10)));

    mapStringInt.put("hello", 1);
    mapStringInt.put("world", 2);
    mapStringIntJson.set("hello", IntNode.valueOf(1));
    mapStringIntJson.set("world", IntNode.valueOf(2));

    mapIntDouble.put(1, 1.0);
    mapIntDouble.put(2, 2.0);
    mapIntDoubleJson.set("1", DoubleNode.valueOf(1.0));
    mapIntDoubleJson.set("2", DoubleNode.valueOf(2.0));

    binaryTree.left = new BinaryTreeNode<>(1);
    binaryTree.right = new BinaryTreeNode<>(10);
    binaryTree.left.leftIsNull = true;
    binaryTree.left.rightIsNull = true;
    binaryTree.right.left = new BinaryTreeNode<>(5);
    binaryTreeJson.addAll(Arrays.asList(IntNode.valueOf(2), IntNode.valueOf(1), IntNode.valueOf(10),
        NullNode.getInstance(), NullNode.getInstance(), IntNode.valueOf(5)));

    arrayArrayIntJson.add(arrayIntJson);
    arrayArrayIntJson.add(arrayIntJson2);
    listListInt.add(listInt);
    listListInt.add(listInt2);

    mapStringLinkedListInt.put("hello", linkedListInt);
    mapStringLinkedListInt.put("world", linkedListInt2);
    mapStringLinkedListIntJson.set("hello", arrayIntJson);
    mapStringLinkedListIntJson.set("world", arrayIntJson2);
  }
}
