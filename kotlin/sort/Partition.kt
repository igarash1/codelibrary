package sort

import java.util.Random

object Partition {
    // like http://www.cplusplus.com/reference/algorithm/partition/
    // but additionally places separator at the end of the first group
    fun partition(a: IntArray, fromInclusive: Int, toExclusive: Int, separatorIndex: Int): Int {
        var i = fromInclusive
        var j = toExclusive - 1
        val separator = a[separatorIndex]
        swap(a, i++, separatorIndex)
        while (i <= j) {
            while (i <= j && a[i] < separator) ++i
            while (i <= j && a[j] > separator) --j
            if (i >= j) break
            swap(a, i++, j--)
        }
        swap(a, j, fromInclusive)
        return j
    }

    fun swap(a: IntArray, i: Int, j: Int) {
        val t = a[j]
        a[j] = a[i]
        a[i] = t
    }

    // Random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..99999) {
            val n: Int = rnd.nextInt(10) + 1
            val a: IntArray = rnd.ints(n, 0, 10).toArray()
            for (i in 0 until n) {
                for (j in i until n) {
                    for (k in i..j) {
                        val b: IntArray = a.clone()
                        check(b, partition(b, i, j + 1, k), i, j)
                    }
                }
            }
        }
    }

    fun check(a: IntArray, k: Int, lo: Int, hi: Int) {
        if (k < lo || k > hi) throw RuntimeException()
        for (i in lo..k) for (j in k..hi) if (a[i] > a[j]) throw RuntimeException()
    }
}
