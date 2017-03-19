def isValidSudoku(board)
  rowbuckets = Array.new(9) { Hash.new }
  colbuckets = Array.new(9) { Hash.new }
  boxbuckets = Array.new(9) { Hash.new }

  0.upto(8) do |i|
    0.upto(8) do |j|
      char = board[i][j]
      next if char == '.'

      return false if rowbuckets[i][char]
      rowbuckets[i][char] = true

      return false if colbuckets[j][char]
      colbuckets[j][char] = true

      k = (i / 3) * 3 + (j / 3)
      return false if boxbuckets[k][char]
      boxbuckets[k][char] = true
    end
  end
  return true
end

