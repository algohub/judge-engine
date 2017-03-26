public class Solution {
public LinkedListNode<Integer> deleteDuplicates(LinkedListNode<Integer> head) {
        if (head == null) return null;

        for (LinkedListNode<Integer> prev = head, cur = head.next; cur != null; cur = prev.next) {
            if (prev.value == cur.value) {
                prev.next = cur.next;
            } else {
                prev = cur;
            }
        }
        return head;
    }
};

