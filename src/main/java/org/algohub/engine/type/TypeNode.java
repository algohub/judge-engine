package org.algohub.engine.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Optional;


/**
 * Represent a type.
 *
 * <p> Actually you can view it as a binary tree. </p>
 *
 * <p> keyType only has meaning when parent is a HashMap. </p>
 *
 * <p> elementType will be empty if value is not a container type. </p>
 */
@SuppressWarnings({"PMD.UnusedPrivateField", "PMD.BeanMembersShouldSerialize", "PMD.SingularField",
    "PMD.ShortVariable"})
// @JsonSerialize(using = TypeNodeSerializer.class) Do NOT use it !!!
@JsonDeserialize(using = TypeNodeDeserializer.class)
public class TypeNode {
  /**
   * type.
   */
  private final IntermediateType value;
  /**
   * if parent is hashmap, keyType won't be empty.
   */
  @JsonProperty("key_type") private final Optional<TypeNode> keyType;  // left child
  /**
   * if parent is container, this is its element type.
   */
  @JsonProperty("element_type") private final Optional<TypeNode> elementType;  // right child
  /**
   * point to parent contaier.
   */
  @JsonIgnore @SuppressWarnings("PMD.ImmutableField") private Optional<TypeNode> parent;

  /**
   * Constructor.
   */
  public TypeNode(final IntermediateType value, final Optional<TypeNode> keyType,
      final Optional<TypeNode> elementType) {
    this.value = value;
    this.elementType = elementType;
    this.keyType = keyType;
    this.parent = Optional.empty();
  }

  /**
   * Create a TypeNode from a string.
   */
  public static TypeNode fromString(final String typeStr) {
    if (!checkAngleBrackets(typeStr)) {
      throw new IllegalArgumentException("Illegal type: " + typeStr);
    }

    // remove all whitespaces
    final String typeString = typeStr.replaceAll("\\s+", "");
    final Optional<TypeNode> result = fromStringRecursive(typeString);
    if (result.isPresent()) {
      initializeParent(result, Optional.<TypeNode>empty());
      return result.get();
    } else {
      throw new IllegalArgumentException("Type can not be empty!");
    }
  }

  @Override
  public String toString() {
    return toString(this);
  }

  private static String toString(TypeNode typeNode) {
    if(!typeNode.isContainer()) {
      return typeNode.value.toString();
    }
    final StringBuilder result = new StringBuilder(typeNode.value.toString() + "<");
    if(typeNode.keyType.isPresent()) {
      result.append(toString(typeNode.keyType.get()));
      result.append(", ");
    }
    if(typeNode.elementType.isPresent()) {
      result.append(toString(typeNode.elementType.get()));
    }
    return result + ">";
  }

  // construct a binary tre in post-order
  // A node has three conditions, type, type<type> or type<type,type>
  private static Optional<TypeNode> fromStringRecursive(final String typeStr) {
    final int firstLeftBracket = typeStr.indexOf('<');

    if (firstLeftBracket == -1) { // Not a container
      if (typeStr.isEmpty()) {
        return Optional.empty();
      }
      final IntermediateType currentValue = IntermediateType.fromString(typeStr);
      final TypeNode currentNode = new TypeNode(currentValue, Optional.empty(), Optional.empty());
      return Optional.of(currentNode);
    } else {  // is a container
      final IntermediateType containerType =
          IntermediateType.fromString(typeStr.substring(0, firstLeftBracket));
      final String childStr = typeStr.substring(firstLeftBracket + 1, typeStr.length() - 1);

      if (containerType == IntermediateType.MAP) {
        final int firstCommaOrBracket;
        if (childStr.indexOf('<') == -1 && childStr.indexOf(',') == -1) {
          throw new IllegalArgumentException("Illegal type: " + childStr);
        } else if (childStr.indexOf('<') == -1) {
          firstCommaOrBracket = childStr.indexOf(',');
        } else if (childStr.indexOf(',') == -1) {
          firstCommaOrBracket = childStr.indexOf('<');
        } else {
          firstCommaOrBracket = Math.min(childStr.indexOf('<'), childStr.indexOf(','));
        }

        final int commaPos;
        if (childStr.charAt(firstCommaOrBracket) == ',') {  // primitive, type
          commaPos = firstCommaOrBracket;
        } else {
          final int rightBracket = findRightBracket(childStr, firstCommaOrBracket);
          commaPos = rightBracket + 1;
        }
        final Optional<TypeNode> keyType = fromStringRecursive(childStr.substring(0, commaPos));
        final Optional<TypeNode> child = fromStringRecursive(childStr.substring(commaPos + 1));
        final TypeNode currentNode = new TypeNode(containerType, keyType, child);
        return Optional.of(currentNode);
      } else {
        final TypeNode currentNode =
            new TypeNode(containerType, Optional.empty(), fromStringRecursive(childStr));
        return Optional.of(currentNode);
      }
    }
  }

  /**
   * Check if the brackets match with each other.
   */
  public static boolean checkAngleBrackets(final String typeStr) {
    return checkAngleBrackets(typeStr, 0, 0);
  }

  private static boolean checkAngleBrackets(final String typeStr, final int cur, final int count) {
    if (typeStr.length() == cur) {
      return count == 0;
    } else {
      if (count >= 0) {
        final char ch = typeStr.charAt(cur);
        if (ch == '<') {
          return checkAngleBrackets(typeStr, cur + 1, count + 1);
        } else if (ch == '>') {
          return checkAngleBrackets(typeStr, cur + 1, count - 1);
        } else {
          return checkAngleBrackets(typeStr, cur + 1, count);
        }
      } else {
        return false;
      }
    }
  }

  /**
   * Find the right matched bracket.
   *
   * @param typeStr     the string
   * @param leftBracket a left bracket in the string
   * @return the index of right matched bracket, otherwise return -1
   */
  static int findRightBracket(final String typeStr, final int leftBracket) {
    if (!checkAngleBrackets(typeStr)) {
      throw new IllegalArgumentException("Found unmatched brackets!");
    }

    return findRightBracket(typeStr, leftBracket + 1, 1);
  }

  private static int findRightBracket(final String typeStr, final int cur, final int count) {
    if (count == 0) {
      return cur - 1;
    }

    final char ch = typeStr.charAt(cur);
    if (ch == '<') {
      return findRightBracket(typeStr, cur + 1, count + 1);
    } else if (ch == '>') {
      return findRightBracket(typeStr, cur + 1, count - 1);
    } else {
      return findRightBracket(typeStr, cur + 1, count);
    }
  }

  /**
   * Initialize all parent, pre-order.
   */
  private static void initializeParent(final Optional<TypeNode> node,
      final Optional<TypeNode> parent) {
    if (!node.isPresent()) {
      return;
    }
    final TypeNode realNode = node.get();
    realNode.setParent(parent);
    initializeParent(realNode.getKeyType(), node);
    initializeParent(realNode.getElementType(), node);
  }

  /**
   * if the elementType is not empty, which means it's a container.
   */
  @JsonIgnore public boolean isContainer() {
    return elementType.isPresent();
  }

  /** Singly linked list and binary tree are user-defined class. */
  @JsonIgnore public boolean isCustomizedType() {
    return value == IntermediateType.LINKED_LIST_NODE || value == IntermediateType.BINARY_TREE_NODE;
  }

  public boolean hasCustomizedType() {
    return hasCustomizedType(this);
  }
  
  private static boolean hasCustomizedType(final TypeNode type) {
    if (type.getValue() == IntermediateType.LINKED_LIST_NODE
        || type.getValue() == IntermediateType.BINARY_TREE_NODE) {
      return true;
    } else {
      if (type.getElementType().isPresent()) {
        return hasCustomizedType(type.getElementType().get());
      } else {
        return false;
      }
    }
  }

  // Generated automatically

  public IntermediateType getValue() {
    return value;
  }

  public Optional<TypeNode> getKeyType() {
    return keyType;
  }

  public Optional<TypeNode> getElementType() {
    return elementType;
  }

  public Optional<TypeNode> getParent() {
    return parent;
  }

  public void setParent(final Optional<TypeNode> parent) {
    this.parent = parent;
  }
}
