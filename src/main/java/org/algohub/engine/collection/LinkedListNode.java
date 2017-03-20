package org.algohub.engine.collection;

/** Singly linked list node. */
@SuppressWarnings({"PMD.BeanMembersShouldSerialize", "PMD.CommentRequired", "PMD.ShortVariable"})
public class LinkedListNode<E> {
  public E value;
  public LinkedListNode<E> next;

  /**
   * Constructs an empty list.
   */
  public LinkedListNode() {
    value = null;
    next = null;
  }

  public LinkedListNode(final E value) {
    this.value = value;
    this.next = null;
  }

  public LinkedListNode(final E value, final LinkedListNode<E> next) {
    this.value = value;
    this.next = next;
  }

  /**
   * Add a element.
   */
  public void add(final E value) {
    if (this.value == null) {
      this.value = value;
      return;
    }

    // find tail
    LinkedListNode<E> tail = this;
    while (tail.next != null) {
      tail = tail.next;
    }
    tail.next = new LinkedListNode<>(value);
  }

  @Override public boolean equals(final Object obj) {
    if (obj == this) {
      return true;
    }
    if (!(obj instanceof LinkedListNode)) {
      return false;
    }

    final LinkedListNode other = (LinkedListNode) obj;

    LinkedListNode p = this;
    LinkedListNode q = other;
    if(p.value == null && q.value == null) return true;
    if(p.value == null || q.value == null) return false;
    while (p != null && q != null) {
      if (!p.value.equals(q.value)) {
        return false;
      }
      p = p.next;
      q = q.next;
    }
    return p == null && q == null;
  }

  // just for debug
  @Override public String toString() {
    if (value == null) {
      return "[]";
    }

    final StringBuilder sb = new StringBuilder();
    sb.append('[').append(value);
    for (LinkedListNode<E> p = next; p != null; p = p.next) {
      sb.append(", ").append(p.value);
    }
    sb.append(']');
    return sb.toString();
  }

  @Override public int hashCode() {
    int hashCode = 1;
    for (LinkedListNode<E> p = this; p != null; p = p.next) {
      hashCode = 31 * hashCode + (value == null ? 0 : value.hashCode());
    }

    return hashCode;
  }
}
