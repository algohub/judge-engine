def addTwoNumbers(l1, l2)
  list = LinkedListNode.new(0)
  feed = 0
  pin = list
  until l1.nil? && l2.nil? && feed == 0
    if l1.nil?
      v1 = 0
    else
      v1 = l1.value
    end

    if l2.nil?
      v2 = 0
    else
      v2 = l2.value
    end

    list.value = v1 + v2 + feed
    feed = 0
    if list.value >= 10
      list.value = list.value - 10
      feed = 1
    end
    unless l1.nil?
      l1 = l1.next
    end

    unless l2.nil?
      l2 = l2.next
    end
    unless l1.nil? && l2.nil? && feed == 0
      list.next = LinkedListNode.new(0)
      list = list.next
    end
  end
  pin
end

