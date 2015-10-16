def ladderLength(start, end, dict):
    dict.add(end)
    wordLen = len(start)
    queue = collections.deque([(start, 1)])
    while queue:
        curr = queue.popleft()
        currWord = curr[0]; currLen = curr[1]
        if currWord == end: 
            return currLen
        for i in range(wordLen):
            part1 = currWord[:i]; part2 = currWord[i+1:]
            for j in 'abcdefghijklmnopqrstuvwxyz':
                if currWord[i] != j:
                    nextWord = part1 + j + part2
                    if nextWord in dict:
                        queue.append((nextWord, currLen + 1))
                        dict.remove(nextWord)
    return 0
