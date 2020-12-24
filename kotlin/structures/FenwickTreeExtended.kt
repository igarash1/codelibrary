package structures

object FenwickTreeExtended {
    // T[i] += value
    fun add(t: IntArray, i: Int, value: Int) {
        var i = i
        while (i < t.size) {
            t[i] += value
            i = i or i + 1
        }
    }

    // sum[0..i]
    fun sum(t: IntArray, i: Int): Int {
        var i = i
        var res = 0
        while (i >= 0) {
            res += t[i]
            i = (i and i + 1) - 1
        }
        return res
    }

    fun createFromArray(a: IntArray): IntArray {
        val t: IntArray = a.clone()
        for (i in a.indices) {
            val j: Int = i or i + 1
            if (j < a.size) t[j] += t[i]
        }
        return t
    }

    // sum[a..b]
    fun sum(t: IntArray, a: Int, b: Int): Int {
        return sum(t, b) - sum(t, a - 1)
    }

    operator fun get(t: IntArray, i: Int): Int {
        var i = i
        var res = t[i]
        val lca = (i and i + 1) - 1
        --i
        while (i != lca) {
            res -= t[i]
            i = (i and i + 1) - 1
        }
        return res
    }

    operator fun set(t: IntArray, i: Int, value: Int) {
        add(t, i, -FenwickTreeExtended[t, i] + value)
    }

    ///////////////////////////////////////////////////////
    // interval add
    fun add(t: IntArray, a: Int, b: Int, value: Int) {
        add(t, a, value)
        add(t, b + 1, -value)
    }

    // point query
    fun get1(t: IntArray, i: Int): Int {
        return sum(t, i)
    }

    ///////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////
    // interval add
    fun add(t1: IntArray, t2: IntArray, a: Int, b: Int, value: Int) {
        add(t1, a, value)
        add(t1, b, -value)
        add(t2, a, -value * (a - 1))
        add(t2, b, value * b)
    }

    // interval query
    fun sum(t1: IntArray, t2: IntArray, i: Int): Int {
        return sum(t1, i) * i + sum(t2, i)
    }

    ///////////////////////////////////////////////////////
    // Returns min(p | sum[0,p] >= sum)
    fun lower_bound(t: IntArray, sum: Int): Int {
        var sum = sum
        var pos = 0
        var blockSize: Int = Integer.highestOneBit(t.size)
        while (blockSize != 0) {
            val p = pos + blockSize - 1
            if (p < t.size && t[p] < sum) {
                sum -= t[p]
                pos += blockSize
            }
            blockSize = blockSize shr 1
        }
        return pos
    }

    // Usage example
    fun main(args: Array<String?>?) {
        var t = IntArray(10)
        FenwickTreeExtended[t, 0] = 1
        add(t, 9, -2)
        System.out.println(-1 == sum(t, 0, 9))
        t = createFromArray(intArrayOf(1, 2, 3, 4, 5, 6))
        for (i in t.indices) System.out.print(FenwickTreeExtended[t, i].toString() + " ")
        System.out.println()
        t = createFromArray(intArrayOf(0, 0, 1, 0, 0, 1, 0, 0))
        System.out.println(5 == lower_bound(t, 2))
        val t1 = IntArray(10)
        val t2 = IntArray(10)
        add(t1, t2, 0, 9, 1)
        add(t1, t2, 0, 0, -2)
        System.out.println(sum(t1, t2, 9))
    }
}
