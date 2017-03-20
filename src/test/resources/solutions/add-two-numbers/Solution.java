public class Solution {
    public LinkedListNode<Integer> addTwoNumbers(LinkedListNode<Integer> l1, LinkedListNode<Integer> l2) {
        LinkedListNode<Integer> dummy = new LinkedListNode<Integer>(-1); // 头节点
        int carry = 0;
        LinkedListNode<Integer> prev = dummy;
        for (LinkedListNode<Integer> pa = l1, pb = l2;
             pa != null || pb != null;
             pa = pa == null ? null : pa.next,
             pb = pb == null ? null : pb.next,
             prev = prev.next) {
            final int ai = pa == null ? 0 : pa.value;
            final int bi = pb == null ? 0 : pb.value;
            final int value = (ai + bi + carry) % 10;
            carry = (ai + bi + carry) / 10;
            prev.next = new LinkedListNode<Integer>(value); // 尾插法
        }
        if (carry > 0)
            prev.next = new LinkedListNode<Integer>(carry);
        return dummy.next;
    }
};

