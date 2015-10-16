def twoSum(num, target):
    d = {}
    for i in range(len(num)):
        d.setdefault(num[i], []).append(i+1)
    for x in d:
        rest = target - x
        if rest == x:
            if len(d[x]) >= 2:
                return [min(d[x]), max(d[x])]
        else:
            if rest in d:
                return [min(d[x][0], d[rest][0]), max(d[x][0], d[rest][0])]
