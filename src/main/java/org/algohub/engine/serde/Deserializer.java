package org.algohub.engine.serde;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

import org.algohub.engine.collection.BinaryTreeNode;
import org.algohub.engine.collection.LinkedListNode;
import org.algohub.engine.pojo.Function;
import org.algohub.engine.type.IntermediateType;
import org.algohub.engine.type.TypeNode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Convert JSON to Java Object.
 */
@SuppressWarnings({"PMD.CommentRequired"}) public interface Deserializer {
  ImmutableMap<IntermediateType, Class> JAVA_CLASS_MAP =
      ImmutableMap.<IntermediateType, Class>builder().put(IntermediateType.BOOL, boolean.class)
          .put(IntermediateType.STRING, String.class).put(IntermediateType.DOUBLE, double.class)
          .put(IntermediateType.INT, int.class).put(IntermediateType.LONG, long.class)
          .put(IntermediateType.LIST, ArrayList.class).put(IntermediateType.SET, HashSet.class)
          .put(IntermediateType.MAP, HashMap.class)
          .put(IntermediateType.LINKED_LIST_NODE, LinkedListNode.class)
          .put(IntermediateType.BINARY_TREE_NODE, BinaryTreeNode.class).build();

  /**
   * Get type of array element.
   */
  static Class getArrayElementType(final TypeNode typeNode) {
    TypeNode node = typeNode;
    while (node.getValue() == IntermediateType.ARRAY) {
      node = node.getElementType().get();
    }
    return JAVA_CLASS_MAP.get(node.getValue());
  }

  /**
   * Get all dimensions.
   */
  static int[] getDimension(final ArrayNode arrayNode, final TypeNode typeNode) {
    final ArrayList<Integer> list = new ArrayList<>();

    JsonNode cur = arrayNode;
    TypeNode currentType = typeNode;
    while (cur.isArray() && currentType.getValue() == IntermediateType.ARRAY) {
      list.add(cur.size());
      cur = cur.get(0);
      currentType = currentType.getElementType().get();
    }
    return Ints.toArray(list);
  }


  /**
   * Convert primitive values to JsonNode.
   */
  static Object jsonToJavaPrimitiveNew(final TypeNode type, final JsonNode jsonNode) {
    final Object object;
    // for BinaryTreeNode
    if (jsonNode.isNull()) {
      return null;
    }

    switch (type.getValue()) {
      case BOOL:
        object = jsonNode.asBoolean();
        break;
      case STRING:
        object = jsonNode.asText();
        break;
      case DOUBLE:
        object = jsonNode.asDouble();
        break;
      case INT:
        object = jsonNode.asInt();
        break;
      case LONG:
        object = jsonNode.asLong();
        break;
      default:
        throw new IllegalArgumentException("Unrecognized primitive type: " + type);
    }
    return object;
  }

  /**
   * Deserialize an object using JSON.
   */
  // Post order
  static Object fromJson(final TypeNode type, final JsonNode jsonNode) {
    if (!type.isContainer()) {
      return jsonToJavaPrimitiveNew(type, jsonNode);
    }

    // Deal with two children first
    final Object javaNode;
    switch (type.getValue()) {
      case ARRAY: {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final TypeNode elementType = type.getElementType().get();
        switch (elementType.getValue()) {
          case BOOL: {
            final boolean[] javaArray = new boolean[elements.size()];
            for (int i = 0; i < elements.size(); ++i) {
              javaArray[i] = (Boolean) fromJson(elementType, elements.get(i));
            }
            javaNode = javaArray;
            break;
          }
          case INT: {
            final int[] javaArray = new int[elements.size()];
            for (int i = 0; i < elements.size(); ++i) {
              javaArray[i] = (Integer) fromJson(elementType, elements.get(i));
            }
            javaNode = javaArray;
            break;
          }
          case LONG: {
            final long[] javaArray = new long[elements.size()];
            for (int i = 0; i < elements.size(); ++i) {
              javaArray[i] = (Long) fromJson(elementType, elements.get(i));
            }
            javaNode = javaArray;
            break;
          }
          case DOUBLE: {
            final double[] javaArray = new double[elements.size()];
            for (int i = 0; i < elements.size(); ++i) {
              javaArray[i] = (Double) fromJson(elementType, elements.get(i));
            }
            javaNode = javaArray;
            break;
          }
          default: {
            final Class innerestType = getArrayElementType(type);
            final int[] dimension = getDimension(elements, type);
            final Object javaArray = Array.newInstance(innerestType, dimension);
            for (int i = 0; i < elements.size(); ++i) {
              Array.set(javaArray, i, fromJson(elementType, elements.get(i)));
            }
            javaNode = javaArray;
          }
        }
        break;
      }
      case LIST: {
        final ArrayNode elements = (ArrayNode) jsonNode;

        final List javaList = new ArrayList<>();
        for (final JsonNode e : elements) {
          javaList.add(fromJson(type.getElementType().get(), e));
        }
        javaNode = javaList;
        break;
      }
      case SET: {
        final ArrayNode elements = (ArrayNode) jsonNode;

        final Set javaSet = new HashSet<>();
        for (final JsonNode e : elements) {
          javaSet.add(fromJson(type.getElementType().get(), e));
        }
        javaNode = javaSet;
        break;
      }
      case MAP: {
        final Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields();
        final Map<Object, Object> javaMap = new HashMap<>();

        while (iterator.hasNext()) {
          final Map.Entry<String, JsonNode> entry = iterator.next();
          //NOTE: Since JSON only allows string as key, so all hashmap's key has a
          // single level
          final String keyStr = entry.getKey();
          final Object key;
          switch (type.getKeyType().get().getValue()) {
            case BOOL:
              key = Boolean.valueOf(keyStr);
              break;
            case STRING:
              key = keyStr;
              break;
            case DOUBLE:
              key = Double.valueOf(keyStr);
              break;
            case INT:
              key = Integer.valueOf(keyStr);
              break;
            case LONG:
              key = Long.valueOf(keyStr);
              break;
            default:
              throw new IllegalArgumentException("map keys can only be primitive type: " + type);
          }
          final Object value = fromJson(type.getElementType().get(), entry.getValue());
          javaMap.put(key, value);
        }
        javaNode = javaMap;
        break;
      }
      case LINKED_LIST_NODE: {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final LinkedListNode javaLinkedList = new LinkedListNode<>();

        for (final JsonNode e : elements) {
          javaLinkedList.add(fromJson(type.getElementType().get(), e));
        }

        javaNode = javaLinkedList;
        break;
      }
      case BINARY_TREE_NODE: {
        final ArrayNode elements = (ArrayNode) jsonNode;
        final BinaryTreeNode javaBinaryTree = new BinaryTreeNode<>();

        for (final JsonNode e : elements) {
          javaBinaryTree.add(fromJson(type.getElementType().get(), e));
        }

        javaNode = javaBinaryTree;
        break;
      }
      default:
        throw new IllegalArgumentException("Unrecognized collection type: " + type.getValue());
    }

    return javaNode;
  }

  /**
   * Convert input to array.
   */
  static Object[] inputToJavaArray(ArrayNode input, Function.Parameter[] parameters) {
    final Object[] arguments = new Object[parameters.length];

    for (int i = 0; i < parameters.length; i++) {
      arguments[i] = fromJson(parameters[i].getType(), input.get(i));
    }
    return arguments;
  }
}
