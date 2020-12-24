package structures

object SegmentTreeWithoutRecursion {
    operator fun get(t: IntArray, i: Int): Int {
        return t[i + t.size / 2]
    }

    fun add(t: IntArray, i: Int, value: Int) {
        var i = i
        i += t.size / 2
        t[i] += value
        while (i > 1) {
            t[i shr 1] = Math.max(t[i], t[i xor 1])
            i = i shr 1
        }
    }

    fun max(t: IntArray, a: Int, b: Int): Int {
        var a = a
        var b = b
        var res: Int = Integer.MIN_VALUE
        a += t.size / 2
        b += t.size / 2
        while (a <= b) {
            if (a and 1 != 0) res = Math.max(res, t[a])
            if (b and 1 == 0) res = Math.max(res, t[b])
            a = a + 1 shr 1
            b = b - 1 shr 1
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val n = 10
        val t = IntArray(n + n)
        add(t, 0, 1)
        add(t, 9, 2)
        System.out.println(2 == max(t, 0, 9))
    }
}
