package dp

import java.util.Arrays

// https://en.wikipedia.org/wiki/Longest_increasing_subsequence in O(n*log(n))
object Lis {
    fun lis(a: IntArray): IntArray {
        val n = a.size
        val tail = IntArray(n)
        val prev = IntArray(n)
        var len = 0
        for (i in 0 until n) {
            val pos = lower_bound(a, tail, len, a[i])
            len = Math.max(len, pos + 1)
            prev[i] = if (pos > 0) tail[pos - 1] else -1
            tail[pos] = i
        }
        val res = IntArray(len)
        var i = tail[len - 1]
        while (i >= 0) {
            res[--len] = a[i]
            i = prev[i]
        }
        return res
    }

    fun lower_bound(a: IntArray, tail: IntArray, len: Int, key: Int): Int {
        var lo = -1
        var hi = len
        while (hi - lo > 1) {
            val mid = lo + hi ushr 1
            if (a[tail[mid]] < key) {
                lo = mid
            } else {
                hi = mid
            }
        }
        return hi
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val lis = lis(intArrayOf(1, 10, 2, 11, 3))
        System.out.println(Arrays.toString(lis))
    }
}
