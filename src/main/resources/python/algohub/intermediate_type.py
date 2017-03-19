from enum import Enum

class IntermediateType(Enum):
    BOOL = 'bool'
    CHAR = 'char'
    STRING = 'string'
    DOUBLE = 'double'
    INT = 'int'
    LONG = 'long'
    ARRAY = 'array'
    LIST = 'list'
    SET = 'set'
    MAP = 'map'
    LINKED_LIST_NODE = 'LinkedListNode'
    BINARY_TREE_NODE = 'BinaryTreeNode'

    def __str__(self):
        return str(self.value)
