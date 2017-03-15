package org.algohub.engine.type;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.algohub.engine.util.ObjectMapperInstance;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@SuppressWarnings({"PMD.CommentRequired"})
public class TypeNodeTest {

  @Test public void checkAngleBracketsTest() {
    assertFalse(TypeNode.checkAngleBrackets("<"));
    assertTrue(TypeNode.checkAngleBrackets("<>"));
    assertFalse(TypeNode.checkAngleBrackets("><"));
    assertFalse(TypeNode.checkAngleBrackets("<abcd<e>"));
    assertTrue(TypeNode.checkAngleBrackets("<a><b>"));
  }

  @Test public void findRightBracketTest() {
    assertEquals(10, TypeNode.findRightBracket("vector<int>", 6));
    assertEquals(22, TypeNode.findRightBracket("map<string,vector<int>>", 3));
    assertEquals(21, TypeNode.findRightBracket("map<string,vector<int>>", 17));
  }

  @Test public void fromStringTest() {
    final TypeNode node1 = TypeNode.fromString("int");
    assertEquals(IntermediateType.INT, node1.getValue());
    assertFalse(node1.getElementType().isPresent());
    assertFalse(node1.getKeyType().isPresent());
    assertFalse(node1.getParent().isPresent());
    assertEquals("int", node1.toString());

    final TypeNode node2 = TypeNode.fromString("list<int>");
    assertEquals(IntermediateType.LIST, node2.getValue());
    assertTrue(node2.getElementType().isPresent());
    assertFalse(node2.getKeyType().isPresent());
    assertFalse(node2.getParent().isPresent());
    assertEquals("list<int>", node2.toString());

    final TypeNode node2Child = node2.getElementType().get();
    assertEquals(IntermediateType.INT, node2Child.getValue());
    assertFalse(node2Child.getElementType().isPresent());
    assertFalse(node2Child.getKeyType().isPresent());
    assertTrue(node2Child.getParent().isPresent());
    assertEquals(IntermediateType.LIST, node2Child.getParent().get().getValue());

    final TypeNode mapNode = TypeNode.fromString("map<string, list<int>>");
    assertEquals(IntermediateType.MAP, mapNode.getValue());
    assertTrue(mapNode.getElementType().isPresent());
    assertTrue(mapNode.getKeyType().isPresent());
    assertEquals(IntermediateType.STRING, mapNode.getKeyType().get().getValue());
    assertEquals(IntermediateType.LIST, mapNode.getElementType().get().getValue());
    assertEquals(IntermediateType.INT,
        mapNode.getElementType().get().getElementType().get().getValue());
    assertEquals("map<string, list<int>>", mapNode.toString());
  }
}
