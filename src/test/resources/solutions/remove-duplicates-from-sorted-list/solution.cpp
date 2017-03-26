shared_ptr<LinkedListNode<int>> deleteDuplicates(shared_ptr<LinkedListNode<int>> head) {
    if (head == nullptr) return nullptr;

    for (shared_ptr<LinkedListNode<int>> prev = head, cur = head->next; cur != nullptr; cur = prev->next) {
        if (prev->value == cur->value) {
            prev->next = cur->next;
        } else {
            prev = cur;
        }
    }
    return head;
}

