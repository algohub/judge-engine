package org.algohub.engine.collection;

import java.util.LinkedList;
import java.util.Queue;

/** Binary Tree Node. */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.CommentRequired", "PMD.ShortVariable"})
public class BinaryTreeNode<E> {
  public E value;
  public BinaryTreeNode<E> left;
  public BinaryTreeNode<E> right;

  /**
   * has a left child, but it's a null node.
   */
  public transient boolean leftIsNull;
  /**
   * has a right child, but it's a null node.
   */
  public transient boolean rightIsNull;
  //public transient BinaryTreeNode<E> next;

  /**
   * Constructs an empty binary tree.
   */
  public BinaryTreeNode() {
    value = null;
    left = null;
    right = null;
  }

  /**
   * Constructs an binary tree with one element.
   */
  public BinaryTreeNode(final E value) {
    this.value = value;
    left = null;
    right = null;
  }

  private static boolean isSameTree(final BinaryTreeNode p, final BinaryTreeNode q) {
    if (p == null && q == null) {
      return true;
    }
    if (p == null || q == null) {
      return false;
    }

    return p.value.equals(q.value) && isSameTree(p.left, q.left) && isSameTree(p.right, q.right);
  }

  // get the insertion position
  @SuppressWarnings("PMD.AvoidInstantiatingObjectsInLoops")
  private NodeAndFather tail() {
    final Queue<NodeAndFather> queue = new LinkedList<>();
    if (value != null) {
      queue.add(new NodeAndFather(this, null, true));
    }

    while (!queue.isEmpty()) {
      final NodeAndFather nodeAndFather = queue.poll();

      if (nodeAndFather.node != null) {
        if (nodeAndFather.node.left != null || nodeAndFather.node.leftIsNull) {
          queue.add(new NodeAndFather(nodeAndFather.node.left, nodeAndFather.node, false));
        } else {
          return new NodeAndFather(nodeAndFather.node.left, nodeAndFather.node, false);
        }
        if (nodeAndFather.node.right != null || nodeAndFather.node.rightIsNull) {
          queue.add(new NodeAndFather(nodeAndFather.node.right, nodeAndFather.node, true));
        } else {
          return new NodeAndFather(nodeAndFather.node.right, nodeAndFather.node, true);
        }
      }
    }
    return null;
  }

  /**
   * return next insert point.
   */
  public void add(final E value) {
    final NodeAndFather last = tail();
    if (last == null) {
      this.value = value;
      return;
    }

    if (last.isRight) {
      if (value == null) {
        last.father.rightIsNull = true;
      } else {
        last.father.right = new BinaryTreeNode<>(value);
      }
    } else {
      if (value == null) {
        last.father.leftIsNull = true;
      } else {
        last.father.left = new BinaryTreeNode<>(value);
      }
    }
  }

  @Override public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof BinaryTreeNode)) {
      return false;
    }

    final BinaryTreeNode<E> p = this;
    final BinaryTreeNode<E> q = (BinaryTreeNode<E>) obj;
    return isSameTree(p, q);
  }

  @Override public String toString() {
    if (value == null) {
      return "[]";
    }

    final StringBuilder sb = new StringBuilder();
    sb.append('[');
    final Queue<BinaryTreeNode<E>> queue = new LinkedList<>();
    queue.add(this);
    while (!queue.isEmpty()) {
      final BinaryTreeNode<E> node = queue.poll();
      sb.append(',').append(node.value == null ? "null" : node.value.toString());
    }

    return sb.toString();
  }

  @Override public int hashCode() {
    int hashCode = 1;
    final Queue<BinaryTreeNode<E>> queue = new LinkedList<>();
    queue.add(this);

    while (!queue.isEmpty()) {
      final BinaryTreeNode<E> node = queue.poll();
      hashCode = 31 * hashCode + (node.value == null ? 0 : node.value.hashCode());

      if (node.left != null) {
        queue.offer(node.left);
      }
      if (node.right != null) {
        queue.offer(node.right);
      }
    }

    return hashCode;
  }


  private static class NodeAndFather {
    final private BinaryTreeNode node;
    final private BinaryTreeNode father;
    final private boolean isRight;  // node is the right child

    public NodeAndFather(final BinaryTreeNode node, final BinaryTreeNode father, final boolean isRight) {
      this.node = node;
      this.father = father;
      this.isRight = isRight;
    }
  }
}
