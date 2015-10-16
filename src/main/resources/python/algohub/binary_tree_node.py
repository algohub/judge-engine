from .common_equality_mixin import CommonEqualityMixin
from collections import deque

class BinaryTreeNode(CommonEqualityMixin):
    def __init__(self, value=None, left=None, right=None, left_is_null=False,
                 right_is_null=False):
        self.value = value
        self.left = left # type: BinaryTreeNode
        self.right = right # type: BinaryTreeNode
        self.left_is_null = left_is_null
        self.right_is_null = right_is_null

    class NodeAndFather(object):
        def __init__(self, node, father, is_right):
            self.node = node # type: BinaryTreeNode
            self.father = father # type: BinaryTreeNode
            self.is_right = is_right

    def __tail(self):
        queue = deque()  # type: deque[BinaryTreeNode.NodeAndFather]
        if self.value is not None:
            queue.append(self.NodeAndFather(self, None, True))

        while len(queue) > 0:
            pos = queue.popleft()

            if pos.node is not None:
                if pos.node.left is not None or pos.node.left_is_null:
                    queue.append(self.NodeAndFather(pos.node.left, pos.node, False))
                else:
                    return self.NodeAndFather(pos.node.left, pos.node, False)
                if pos.node.right is not None or pos.node.right_is_null:
                    queue.append(self.NodeAndFather(pos.node.right, pos.node, True))
                else:
                    return self.NodeAndFather(pos.node.right, pos.node, True)
        return None

    def add(self, value):
        last = self.__tail()
        if last is None:
            self.value = value
            return

        if last.is_right:
            if value is not None:
                last.father.right = BinaryTreeNode(value)
            else:
                last.father.right_is_null = True
        else:
            if value is not None:
                last.father.left = BinaryTreeNode(value)
            else:
                last.father.left_is_null = True

    def __str__(self):
        result = '['
        current = deque()
        next = deque()
        if self.value is not None:
            current.append(self)

        while len(current) > 0:
            level = []

            while len(current) > 0:
                level.append(current.popleft())

            last_not_null_index = -1
            for i in reversed(range(len(level))):
                if level[i] is not None:
                    last_not_null_index = i
                    break

            for i in range(last_not_null_index + 1):
                node = level[i]
                if node is not None:
                    result.append(str(node.value) + ',')
                else:
                    result.append('null,')

                if node is not None:
                    next.append(node.left)
                    next.append(node.right)

            # swap current and next
            tmp = current
            current = next
            next = tmp

        return result[:-1] + ']'

