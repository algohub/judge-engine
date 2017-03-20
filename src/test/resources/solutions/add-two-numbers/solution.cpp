shared_ptr<LinkedListNode<int>> addTwoNumbers(shared_ptr<LinkedListNode<int>> l1, shared_ptr<LinkedListNode<int>> l2) {
    auto dummy = make_shared<LinkedListNode<int>>(-1); // 头节点
    int carry = 0;
    auto prev = dummy;
    for (auto pa = l1, pb = l2;
        pa != nullptr || pb != nullptr;
        pa = pa == nullptr ? nullptr : pa->next,
        pb = pb == nullptr ? nullptr : pb->next,
        prev = prev->next) {
        const int ai = pa == nullptr ? 0 : pa->value;
        const int bi = pb == nullptr ? 0 : pb->value;
        const int value = (ai + bi + carry) % 10;
        carry = (ai + bi + carry) / 10;
        prev->next = make_shared<LinkedListNode<int>>(value); // 尾插法
    }
    if (carry > 0)
        prev->next = make_shared<LinkedListNode<int>>(carry);
    return dummy->next;
}

