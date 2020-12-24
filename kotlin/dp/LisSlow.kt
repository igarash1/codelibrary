package dp

import java.util.Arrays

// https://en.wikipedia.org/wiki/Longest_increasing_subsequence in O(n^2)
object LisSlow {
    fun getLis(x: IntArray): IntArray {
        val n = x.size
        val len = IntArray(n)
        Arrays.fill(len, 1)
        val pred = IntArray(n)
        Arrays.fill(pred, -1)
        var bi = 0
        for (i in 1 until n) {
            for (j in 0 until i) {
                if (x[j] < x[i] && len[i] < len[j] + 1) {
                    len[i] = len[j] + 1
                    pred[i] = j
                }
            }
            if (len[bi] < len[i]) {
                bi = i
            }
        }
        var cnt = len[bi]
        val res = IntArray(cnt)
        var i = bi
        while (i != -1) {
            res[--cnt] = x[i]
            i = pred[i]
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val a = intArrayOf(1, 5, 4, 2, 3, 7, 6)
        val lis = getLis(a)
        System.out.println(Arrays.toString(lis))
    }
}
