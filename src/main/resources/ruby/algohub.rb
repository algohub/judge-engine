require 'json'
require 'set'

module Algohub
  module IntermediateType
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
  end

  module StatusCode
    ACCEPTED = 4
    WRONG_ANSWER = 5
    RUNTIME_ERROR = 6
    TIME_LIMIT_EXCEEDED = 7
    MEMORY_LIMIT_EXCEEDED = 8
    OUTPUT_LIMIT_EXCEEDED = 9
    RESTRICTED_CALL = 10
  end


  class TypeNode
    attr_reader :value
    attr_reader :element_type
    attr_reader :key_type

    def initialize(value, element_type = nil, key_type = nil)
      @value = value
      @element_type = element_type
      @key_type = key_type
    end

    def ==(other)
      TypeNode.same_tree?(self, other)
    end

    def eql?(other)
      self == other
    end

    def self.same_tree?(p, q)
      if p.nil? && q.nil?
        return true
      end
      if p.nil? || q.nil?
        return false
      end
      return p.value == q.value && TypeNode.same_tree?(p.element_type, q.element_type) \
        && TypeNode.same_tree?(p.key_type, q.key_type)
    end

    def self.to_json(type_node)
      json_str = '{"value":"' + type_node.value.to_s + '"'
      json_str += ',"element_type":' + TypeNode.to_json(type_node.element_type) unless type_node.element_type.nil?
      json_str += ',"key_type":' + TypeNode.to_json(type_node.key_type) unless type_node.key_type.nil?
      json_str + '}'
    end

    def self.from_json(json_str)
      obj = JSON.parse(json_str)
      TypeNode.from_json_internal(obj)
    end

    def self.from_json_internal(obj)
      element_type = obj['element_type'].nil? ? nil : TypeNode.from_json_internal(obj['element_type'])
      key_type = obj['key_type'].nil? ? nil : TypeNode.from_json_internal(obj['key_type'])
      TypeNode.new(obj['value'], element_type, key_type)
    end

    def self.has_customized_type(type_node)
      until type_node.nil?
        if type_node.value == IntermediateType::LINKED_LIST_NODE  \
          or type_node.value == IntermediateType::BINARY_TREE_NODE
          return true
        end
        type_node = type_node.element_type
      end
      return false
    end

    def self.has_non_json_type(type_node)
      until type_node.nil?
        if type_node.value == IntermediateType::LINKED_LIST_NODE  \
          or type_node.value == IntermediateType::BINARY_TREE_NODE \
          or type_node.value == IntermediateType::SET
          return true
        end
        type_node = type_node.element_type
      end
      return false
    end
  end

  class JudgeResult
    def initialize(status_code, error_message=nil, input=nil,
                   output=nil, expected_output=nil, testcase_passed_count=0,
                   testcase_total_count=0, elapsed_time=0, consumed_memory=0)
      @status_code = status_code
      @error_message = error_message
      @input = input
      @output = output
      @expected_output = expected_output
      @testcase_passed_count = testcase_passed_count
      @testcase_total_count = testcase_total_count
      @elapsed_time = elapsed_time
      @consumed_memory = consumed_memory
    end

    def to_json
      result = '{'
      result += '"status_code":' + @status_code.to_s
      result += ', "error_message":' + json.dumps(@error_message) unless @error_message.nil?
      result += ', "input":' + JSON.generate(@input) unless @input.nil?
      result += ', "output":' + JSON.generate(@output) unless @output.nil?
      result += ', "expected_output":' + JSON.generate(@expected_output) unless @expected_output.nil?
      result += ', "testcase_passed_count":' + @testcase_passed_count.to_s unless @testcase_passed_count.nil? || @testcase_passed_count == 0
      result += ', "testcase_total_count":' + @testcase_total_count.to_s unless @testcase_total_count.nil? || @testcase_total_count == 0
      result += ', "elapsed_time":' + @elapsed_time.to_s unless @velapsed_time.nil? || @elapsed_time == 0
      result += ', "consumed_memory":' + @consumed_memory.to_s unless @consumed_memory.nil? || @consumed_memory == 0
      result += '}'
      result
    end
  end

  class LinkedListNode
    attr_accessor :value
    attr_accessor :next

    def initialize(value = nil, _next = nil)
      @value = value
      @next = _next
    end

    def add(e)
      if @value.nil?
        @value = e
        return
      end

      # find tail
      tail = self
      until tail.next.nil? do
        tail = tail.next
      end
      tail.next = LinkedListNode.new(e)
    end

    # just for debug
    def to_s
      if @value.nil?
        return '[]'
      end

      result = '['
      result += self.value.to_s
      cur = @next
      until cur.nil? do
        result += ', '
        result += cur.value.to_s
        cur = cur.next
      end
      result += ']'
      result
    end

    def ==(other)
      self.eql?(other)
    end

    def eql?(other)
      p = self
      q = other
      while !p.nil? and !q.nil?
        if p.value != q.value
          return false
        end
        p = p.next
        q = q.next
      end

      p.nil? and q.nil?
    end

    def hash
      hash_code = 1
      cur = self
      until cur.nil?
        hash_code = 31 * hash_code + (cur.value.nil? ? 0 : cur.value.hash)
        cur = cur.next
      end
      hash_code
    end
  end

  class BinaryTreeNode
    attr_accessor :value
    attr_accessor :left
    attr_accessor :right
    attr_accessor :left_is_null
    attr_accessor :right_is_null


    def initialize(value = nil, left = nil, right = nil, left_is_null=false, right_is_null=false)
      @value = value
      @left = left
      @right = right
      @left_is_null = left_is_null
      @right_is_null = right_is_null
    end

    class NodeAndFather
      attr_reader :node
      attr_reader :father
      attr_reader :is_right

      def initialize(node, father, is_right=false)
        @node = node
        @father = father
        @is_right = is_right
      end
    end

    def _tail
      queue = Queue.new
      queue << NodeAndFather.new(self, nil, true) unless @value.nil?

      until queue.empty?
        pos = queue.pop
        unless pos.node.nil?
          if !pos.node.left.nil? or pos.node.left_is_null
            queue << NodeAndFather.new(pos.node.left, pos.node, false)
          else
            return NodeAndFather.new(pos.node.left, pos.node, false)
          end
          if !pos.node.right.nil? or pos.node.right_is_null
            queue << NodeAndFather.new(pos.node.right, pos.node, true)
          else
            return NodeAndFather.new(pos.node.right, pos.node, true)
          end
        end
      end
      return nil
    end

    def add(value)
      last = _tail
      if last.nil?
        @value = value
        return
      end

      if last.is_right
        if value.nil?
          last.father.right_is_null = true
        else
          last.father.right = BinaryTreeNode.new(value)
        end
      else
        if value.nil?
          last.father.left_is_null = true
        else
          last.father.left = BinaryTreeNode.new(value)
        end
      end
    end

    def to_s
      result = '['
      current = Queue.new
      _next = Queue.new

      current << self unless @value.nil?

      until current.empty?
        level = []
        until current.empty?
          level << current.pop
        end

        last_not_nil_index = -1
        (0..level.size()-1).reverse_each.each do |i|
          unless level[i].nil?
            last_not_nil_index = i
            break
          end
        end
        (0..last_not_nil_index).each do |i|
          node = level[i]
          if node.nil?
            result += 'null,'
          else
            result += node.value.to_s + ','
          end

          unless node.nil?
            _next << node.left
            _next << node.right
          end
        end

        # swap current and _next
        tmp = current
        current = _next
        _next = current
      end

      result.chomp(',') + ']'
    end

    def ==(other)
      BinaryTreeNode.same_tree?(self, other)
    end

    def eql?(other)
      BinaryTreeNode.same_tree?(self, other)
    end

    def self.same_tree?(p, q)
      if p.nil? && q.nil?
        return true
      end
      if p.nil? || q.nil?
        return false
      end

      return p.value == q.value && p.left_is_null == q.left_is_null \
        && p.right_is_null == q.right_is_null && same_tree?(p.left, q.left) \
        && same_tree?(p.right, q.right)
    end

    def hash
      hash_code = 1
      queue = Queue.new

      queue << self unless @value.nil?

      until queue.empty?
        cur = queue.pop
        hash_code = 31 * hash_code + (cur.value.nil? ? 0 : cur.value.hash)
        queue << cur.left unless cur.left.nil?
        queue << cur.right unless cur.right.nil?
      end

      hash_code
    end

    private :_tail
  end

  def self.from_json(json_object, type_node)
    unless TypeNode.has_non_json_type(type_node) or \
      (type_node.value == IntermediateType::MAP and \
      type_node.key_type.value != IntermediateType::STRING)
      return json_object
    end

    result = case type_node.value
               when IntermediateType::ARRAY, IntermediateType::LIST
                 _result = []
                 for x in json_object
                   _result << from_json(x, type_node.element_type)
                 end
                 _result
               when IntermediateType::SET
                 _result = Set.new
                 for x in json_object
                   _result << from_json(x, type_node.element_type)
                 end
                 _result
               when IntermediateType::MAP
                 _result = {}
                 json_object.each do |k, v|
                   key = case type_node.key_type.value
                           when IntermediateType::BOOL
                             k == 'true' ? true: false
                           when IntermediateType::CHAR
                             k[0]
                           when IntermediateType::INT, IntermediateType::LONG
                             k.to_i
                           when IntermediateType::DOUBLE
                             k.to_f
                           when IntermediateType::STRING
                             k
                           else
                             raise 'unrecognized key type in map'
                         end
                   value = from_json(v, type_node.element_type)
                   _result[key] = value
                 end
                 _result
               when IntermediateType::LINKED_LIST_NODE
                 _result = LinkedListNode.new
                 for x in json_object
                   _result.add(from_json(x, type_node.element_type))
                 end
                 _result
               when IntermediateType::BINARY_TREE_NODE
                 _result = BinaryTreeNode.new
                 for x in json_object
                   _result.add(from_json(x, type_node.element_type))
                 end
                 _result
               else
                 raise 'Unsupported type: ' + type_node.value.to_s
             end
    result
  end

  def self.to_json(obj, type_node)
    unless TypeNode.has_customized_type(type_node)
      return obj
    end

    result = case type_node.value
               when IntermediateType::ARRAY, IntermediateType::LIST
                 _result = []
                 for x in obj
                   _result << to_json(x, type_node.element_type)
                 end
                 _result
               when IntermediateType::SET
                 _result = Set.new
                 for x in obj
                   _result << to_json(x, type_node.element_type)
                 end
                 _result
               when IntermediateType::MAP
                 _result = {}
                 obj.each do |k, v|
                   _result[k] = to_json(v, type_node.element_type)
                 end
                 _result
               when IntermediateType::LINKED_LIST_NODE
                 _result = []
                 cur = obj
                 until cur.nil?
                   _result << to_json(cur.value, type_node.element_type)
                   cur = cur.next
                 end
                 _result
               when IntermediateType::BINARY_TREE_NODE
                 _result = []
                 current = Queue.new
                 _next = Queue.new
                 current << obj unless obj.nil?

                 until current.empty?
                   level = []
                   until current.empty?
                     level << current.pop
                   end

                   last_not_nil_index = -1
                   (0..level.size()-1).reverse_each.each do |i|
                     unless level[i].nil?
                       last_not_nil_index = i
                       break
                     end
                   end
                   (0..last_not_nil_index).each do |i|
                     node = level[i]
                     if node.nil?
                       _result  << nil
                     else
                       _result << to_json(node.value, type_node.element_type)
                     end

                     unless node.nil?
                       _next << node.left
                       _next << node.right
                     end
                   end

                   # swap current and _next
                   tmp = current
                   current = _next
                   _next = current
                 end
                 _result
               else
                 raise 'Unsupported type: ' + type_node.value.to_s
             end
    result
  end
end


def my_print
  puts('hello')
  JSON.parse
end
