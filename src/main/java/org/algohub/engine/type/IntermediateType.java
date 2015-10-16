package org.algohub.engine.type;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Supported intermediate types.
 */
public enum IntermediateType {
  BOOL("bool"),
  STRING("string"),
  DOUBLE("double"),
  INT("int"),
  LONG("long"),
  ARRAY("array"),
  LIST("list"),
  SET("set"),
  MAP("map"),
  LINKED_LIST_NODE("LinkedListNode"),
  BINARY_TREE_NODE("BinaryTreeNode");

  /**
   * Text for display.
   */
  private final String text;

  private IntermediateType(final String text) {
    this.text = text;
  }

  /**
   * Return real Enum from string name.
   */
  public static IntermediateType fromString(final String text) {
    if (text != null) {
      for (final IntermediateType v : IntermediateType.values()) {
        if (text.equalsIgnoreCase(v.text)) {
          return v;
        }
      }
    }
    throw new IllegalArgumentException("Unrecognized type: " + text);
  }

  /**
   * {@inheritDoc}.
   */
  @Override public String toString() {
    return text;
  }

  @JsonValue public String getText() {
    return text;
  }
}
