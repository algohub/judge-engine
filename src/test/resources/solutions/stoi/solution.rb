def myStoi(str)
    arr = str.strip.split(/\s+/)
    if arr[0] =~ /^([+-]?[0-9]+)/
        i = $1.to_i
        return i >= 0 ? [2147483647, i].min : [-2147483648, i].max
    end
   0
end

