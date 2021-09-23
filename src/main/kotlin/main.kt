val numberCache = mutableMapOf<Long, Boolean>()

fun main() {
    while (true) {
        println("1 = Manual")
        println("2 = Brute Force")
        println("3 = Cache Test")
        println()
        print("Option: ")

        val option = readLine()?.toIntOrNull() ?: error("could not parse option!")

        when (option) {
            1 -> manual()
            2 -> bruteForce()
            3 -> cacheTest()
        }

        println()
        println()
    }
}

fun manual() {
    print("Enter Number: ")
    val num = readLine()?.toLongOrNull() ?: error("could not parse number!")

    println("Completed: ${getNumberDynamic(num, true, true)}")
    println()
}

fun bruteForce() {
    for (i in 1..Long.MAX_VALUE) {
        println("Calculating: $i....")
        getNumberDynamic(i, false, true)
    }
    println("Brute forced every number up to the 64 bit integer limit!")
}

fun cacheTest() {
    println("Testing 20K numbers with cache...")
    val startCache = System.currentTimeMillis()
    for (i in 1..20_000L) getNumberDynamic(i, false, true)
    println("Took ${System.currentTimeMillis() - startCache} ms!")

    println("Testing 20K numbers without cache...")
    val startNoCache = System.currentTimeMillis()
    for (i in 1..20_000L) getNumberDynamic(i, false, false)
    println("Took ${System.currentTimeMillis() - startNoCache} ms!")
}

fun getNumberRecursion(num: Long, verbose: Boolean): Boolean =
    numberCache.computeIfAbsentExt(num) {
        val next =
            if (num % 2 != 0L) {
                if (verbose) println("$num * 3 + 1")
                num * 3 + 1
            } else {
                if (verbose) println("$num / 2")
                num / 2
            }

        if (next == 1L) return@computeIfAbsentExt true

        return@computeIfAbsentExt getNumberRecursion(next, verbose)
    }

fun getNumberDynamic(_num: Long, verbose: Boolean, cache: Boolean): Boolean {
    var num = _num
    while (num != 1L) {
        if (cache) {
            val cached = numberCache[num]
            if (cached != null) {
                numberCache[_num] = true
                return cached
            }
        }

        if (num % 2 != 0L) {
            if (verbose) println("$num * 3 + 1")
            num = num * 3 + 1
        } else {
            if (verbose) println("$num / 2")
            num /= 2
        }
    }

    if (cache) numberCache[_num] = true
    return true
}

fun <K, V> MutableMap<K, V>.computeIfAbsentExt(key: K, value: () -> V): V {
    val current = this[key]
    if (current != null) return current

    val new = value()
    this[key] = new

    return new
}