std::shared_ptr<LinkedListNode<int>> reverseLinkedList(std::shared_ptr<LinkedListNode<int>> head) {
    if (!head || !(head -> next)) return head;
    std::shared_ptr<LinkedListNode<int>> node = reverseLinkedList(head -> next);
    head -> next -> next = head;
    head -> next = NULL;
    return node; 
}

