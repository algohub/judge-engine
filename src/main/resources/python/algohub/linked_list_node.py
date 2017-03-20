from .common_equality_mixin import CommonEqualityMixin

class LinkedListNode(CommonEqualityMixin):
    def __init__(self, value=None, next=None):
        self.value = value
        self.next = next

    def __eq__(self, other):
        if not isinstance(other, self.__class__):
            return False
        p = self
        q = other
        while p is not None and q is not None:
            if p.value != q.value:
                return False
            p = p.next
            q = q.next
        return p is None and q is None;

    def add(self, e):
        if self.value is None:
            self.value = e
            return

        # find tail
        tail = self
        while tail.next is not None:
            tail = tail.next
        tail.next = LinkedListNode(e)

    # just for debug
    def __str__(self):
        if self.value is None:
            return '[]'

        result = '['
        result += str(self.value)
        cur = self.next
        while cur is not None:
            result += ', '
            result += str(cur.value)
            cur = cur.next
        result += ']'
        return result


