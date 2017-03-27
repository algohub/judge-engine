# return a array, [index1, index2]
def twoSum(numbers, target)
  # brute_force(numbers, target)
  hash_table(numbers, target).sort
end

def brute_force(numbers, target)
  [].tap do |result|
    numbers.each_with_index do |n, i|
      numbers[i+1..-1].each_with_index do |m, j|
        result << i + 1 << i + 1 + j + 1 if target - n == m
      end
    end
  end
end

def hash_table(numbers, target)
  [].tap do |result|
    hash = {}
    numbers.each_with_index do |n, i|
      if j = hash[target - n]
        return result << j + 1 << i + 1
      end
      hash[n] = i
    end
  end
end

