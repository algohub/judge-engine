def deleteDuplicates(head):
    if head == None:
        return None
    node1 = head
    while node1.next != None:
        node2 = node1.next
        if node2.value == node1.value:
            node1.next = node2.next
        else:
            node1 = node1.next
    return head

