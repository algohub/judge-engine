def reverseLinkedList(head)
  # no node or only one node
  return head if head.nil? || head.next.nil?

  current_n = head
  prev_n = nil
  next_n = current_n.next

  # only two node
  if current_n.next.next.nil?
      current_n.next.next = current_n
      current_n = current_n.next
      current_n.next.next = nil
      return current_n
  end

  # more than two node
  while !current_n.next.nil?
    current_n.next = prev_n
    prev_n = current_n
    current_n = next_n
    next_n = next_n.next
  end

  current_n.next=prev_n
  return current_n

end

