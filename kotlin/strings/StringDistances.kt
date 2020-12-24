package strings

import java.util.Arrays

object StringDistances {
    // https://en.wikipedia.org/wiki/Longest_common_subsequence_problem
    fun getLCS(x: IntArray, y: IntArray): IntArray {
        val m = x.size
        val n = y.size
        val lcs = Array(m + 1) { IntArray(n + 1) }
        for (i in 0 until m) {
            for (j in 0 until n) {
                if (x[i] == y[j]) {
                    lcs[i + 1][j + 1] = lcs[i][j] + 1
                } else {
                    lcs[i + 1][j + 1] = Math.max(lcs[i + 1][j], lcs[i][j + 1])
                }
            }
        }
        var cnt = lcs[m][n]
        val res = IntArray(cnt)
        var i = m - 1
        var j = n - 1
        while (i >= 0 && j >= 0) {
            if (x[i] == y[j]) {
                res[--cnt] = x[i]
                --i
                --j
            } else if (lcs[i + 1][j] > lcs[i][j + 1]) {
                --j
            } else {
                --i
            }
        }
        return res
    }

    // https://en.wikipedia.org/wiki/Levenshtein_distance
    fun getLevensteinDistance(a: String, b: String): Int {
        val m: Int = a.length()
        val n: Int = b.length()
        val len = Array(m + 1) { IntArray(n + 1) }
        for (i in 0..m) {
            len[i][0] = i
        }
        for (j in 0..n) {
            len[0][j] = j
        }
        for (i in 0 until m) {
            for (j in 0 until n) {
                if (a.charAt(i) === b.charAt(j)) {
                    len[i + 1][j + 1] = len[i][j]
                } else {
                    len[i + 1][j + 1] = 1 + Math.min(len[i][j], Math.min(len[i + 1][j], len[i][j + 1]))
                }
            }
        }
        return len[m][n]
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val x = intArrayOf(1, 5, 4, 2, 3, 7, 6)
        val y = intArrayOf(2, 7, 1, 3, 5, 4, 6)
        val lcs = getLCS(x, y)
        System.out.println(Arrays.toString(lcs))
        val a = "abc"
        val b = "ac"
        System.out.println(getLevensteinDistance(a, b))
    }
}
