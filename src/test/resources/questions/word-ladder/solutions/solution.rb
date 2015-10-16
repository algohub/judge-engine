def ladderLength(start_word, end_word, dict)
    dict.add(end_word)
    wordLen = start_word.length
    queue = [[start_word,1]]
    while queue.any?
        curr = queue.shift
        currWord = curr[0]
        currLen = curr[1]
        if currWord == end_word
            return currLen
        end

        wordLen.times do |i|
            "abcdefghijklmnopqrstuvwxyz".each_byte do |j|
                if currWord[i] != j.chr
                    nextWord = currWord.clone
                    nextWord[i]=j.chr
                    if dict.include?(nextWord)
                        queue.push([nextWord, currLen + 1])
                        dict.delete(nextWord)
                    end
                end   
            end
        end
    end
    return 0
end
