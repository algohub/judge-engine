package org.algohub.engine.serde;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import org.algohub.engine.collection.BinaryTreeNode;
import org.algohub.engine.collection.LinkedListNode;
import org.algohub.engine.type.TypeNode;
import org.algohub.engine.util.ObjectMapperInstance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.node.LongNode;
import com.fasterxml.jackson.databind.node.NullNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

public interface Serializer {
  /**
   * Serialize primtive values to JSON .
   */
  static <T> JsonNode primitiveToJson(final T value, final TypeNode type) {
    final JsonNode result;
    // for BinaryTreeNode
    if (value == null) {
      return NullNode.instance;
    }

    switch (type.getValue()) {
      case BOOL:
        result = BooleanNode.valueOf((Boolean) value);
        break;
      case CHAR:
        result = TextNode.valueOf(value.toString());
        break;
      case STRING:
        result = TextNode.valueOf((String) value);
        break;
      case DOUBLE:
        result = DoubleNode.valueOf((Double) value);
        break;
      case INT:
        result = IntNode.valueOf((Integer) value);
        break;
      case LONG:
        result = LongNode.valueOf((Long) value);
        break;
      default:
        throw new IllegalArgumentException("Unrecognized primitive type: " + type);
    }
    return result;
  }

  /**
   * Convert objects to JSON serializable objects.
   */
  static <T> JsonNode toJson(final T value, final TypeNode type) {
    if (!type.isContainer()) {
      return primitiveToJson(value, type);
    }

    // Deal with two children first
    final JsonNode result;
    switch (type.getValue()) {
      case ARRAY: {
        final ArrayNode arrayNode = ObjectMapperInstance.INSTANCE.createArrayNode();
        final TypeNode elementType = type.getElementType().get();
        switch (elementType.getValue()) {
          case BOOL: {
            final boolean[] array = (boolean[]) value;
            for (final boolean e : array) {
              arrayNode.add(primitiveToJson(e, elementType));
            }
            break;
          }
          case CHAR: {
            final char[] array = (char[]) value;
            for (final char e : array) {
              arrayNode.add(primitiveToJson(e, elementType));
            }
            break;
          }
          case INT: {
            final int[] array = (int[]) value;
            for (final int e : array) {
              arrayNode.add(primitiveToJson(e, elementType));
            }
            break;
          }
          case LONG: {
            final long[] array = (long[]) value;
            for (final long e : array) {
              arrayNode.add(primitiveToJson(e, elementType));
            }
            break;
          }
          case DOUBLE: {
            final double[] array = (double[]) value;
            for (final double e : array) {
              arrayNode.add(primitiveToJson(e, elementType));
            }
            break;
          }
          default: {
            final Object[] array = (Object[]) value;
            for (final Object e : array) {
              arrayNode.add(toJson(e, type.getElementType().get()));
            }
          }
        }
        result = arrayNode;
        break;
      }
      case LIST: {
        final ArrayList arrayList = (ArrayList) value;
        final ArrayNode arrayNode = ObjectMapperInstance.INSTANCE.createArrayNode();

        for (final Object e : arrayList) {
          arrayNode.add(toJson(e, type.getElementType().get()));
        }
        result = arrayNode;
        break;
      }
      case SET: {
        final HashSet hashSet = (HashSet) value;
        final ArrayNode arrayNode = ObjectMapperInstance.INSTANCE.createArrayNode();

        for (final Object e : hashSet) {
          arrayNode.add(toJson(e, type.getElementType().get()));
        }
        result = arrayNode;
        break;
      }
      case MAP: {
        final HashMap<Object, Object> hashMap = (HashMap<Object, Object>) value;
        final ObjectNode objectNode = ObjectMapperInstance.INSTANCE.createObjectNode();

        for (Map.Entry<Object, Object> entry : hashMap.entrySet()) {
          final JsonNode keyNode = toJson(entry.getKey(), type.getKeyType().get());
          final JsonNode valueNode = toJson(entry.getValue(), type.getElementType().get());
          objectNode.set(keyNode.asText(), valueNode);
        }
        ;

        result = objectNode;
        break;
      }
      case LINKED_LIST_NODE: {
        final LinkedListNode linkedList = (LinkedListNode) value;
        final ArrayNode arrayNode = ObjectMapperInstance.INSTANCE.createArrayNode();

        for (LinkedListNode i = linkedList; i != null; i = i.next) {
          arrayNode.add(toJson(i.value, type.getElementType().get()));
        }

        result = arrayNode;
        break;
      }
      case BINARY_TREE_NODE: {
        final BinaryTreeNode root = (BinaryTreeNode) value;
        final ArrayNode arrayNode = ObjectMapperInstance.INSTANCE.createArrayNode();

        Queue<BinaryTreeNode> current = new LinkedList<>();
        Queue<BinaryTreeNode> next = new LinkedList<>();
        if (root != null) {
          current.offer(root);
        }

        while (!current.isEmpty()) {
          final ArrayList<BinaryTreeNode> level = new ArrayList<>();
          while (!current.isEmpty()) {
            level.add(current.poll());
          }

          int lastNotNullIndex = -1;
          for (int i = level.size() - 1; i >= 0; i--) {
            if (level.get(i) != null) {
              lastNotNullIndex = i;
              break;
            }
          }

          for (int i = 0; i <= lastNotNullIndex; ++i) {
            final BinaryTreeNode node = level.get(i);
            if (node != null) {
              arrayNode.add(toJson(node.value, type.getElementType().get()));
            } else {
              arrayNode.add(NullNode.instance);
            }

            if (node != null) {
              next.offer(node.left);
              next.offer(node.right);
            }
          }
          // swap current and next
          final Queue<BinaryTreeNode> tmp = current;
          current = next;
          next = tmp;
        }

        result = arrayNode;
        break;
      }
      default:
        throw new IllegalArgumentException("Unrecognized collection type: " + type.getValue());
    }

    return result;
  }
}
