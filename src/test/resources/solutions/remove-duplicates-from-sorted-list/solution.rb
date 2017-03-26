def deleteDuplicates(head)
  return head if head.nil? || head.next.nil?
  prev = head
  current = head.next

  until current.nil?
    if current.value == prev.value
      prev.next = current.next
    else
      prev = prev.next
    end
    current = current.next
  end
  head
end

