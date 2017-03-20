public class Solution {
    public LinkedListNode<Integer> reverseLinkedList(LinkedListNode<Integer> head) {
        if (head == null || head.next == null) return head;

        LinkedListNode<Integer> tail = null;
        LinkedListNode<Integer> p = head;
        LinkedListNode<Integer> q = p.next;

        while (q != null) {
            LinkedListNode old = q.next;
            p.next = tail;
            q.next = p;

            tail = p;
            p = q;
            q = old;
        }
        return p;
    }
}

