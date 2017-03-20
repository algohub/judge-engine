def addTwoNumbers(l1, l2):
    carry = 0
    root = n = LinkedListNode(0)
    while l1 or l2 or carry:
        v1 = v2 = 0
        if l1:
            v1 = l1.value
            l1 = l1.next
        if l2:
            v2 = l2.value
            l2 = l2.next
        carry, value = divmod(v1+v2+carry, 10)
        n.next = LinkedListNode(value)
        n = n.next
    return root.next

