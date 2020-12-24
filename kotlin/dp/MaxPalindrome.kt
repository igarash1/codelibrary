package dp

import numeric.FFT
import optimization.Simplex

// Find maximum palindromic subsequence of the given string
object MaxPalindrome {
    fun maxPalindrome(p: String): String {
        val n: Int = p.length()
        val s: CharArray = p.toCharArray()
        val dp = Array(n + 1) { IntArray(n + 1) }
        for (i in 0..n) {
            dp[0][i] = i
            dp[i][0] = dp[0][i]
        }
        for (i in 0 until n) {
            for (j in 0 until n - 1 - i) {
                dp[i + 1][j + 1] = if (s[i] == s[n - 1 - j]) dp[i][j] else Math.min(dp[i][j + 1] + 1, dp[i + 1][j] + 1)
            }
        }
        var min = n
        var x = 0
        var y = n
        for (i in 0..n) {
            if (min > dp[i][n - i]) {
                min = dp[i][n - i]
                x = i
                y = n - i
            }
        }
        var middle = ""
        for (i in 0 until n) {
            if (min > dp[i][n - i - 1]) {
                min = dp[i][n - i - 1]
                x = i
                y = n - i - 1
                middle = "" + s[i]
            }
        }
        var res = ""
        while (x > 0 && y > 0) {
            val a = dp[x - 1][y - 1]
            val b = dp[x - 1][y]
            val c = dp[x][y - 1]
            val m: Int = Math.min(a, Math.min(b, c))
            if (a == m) {
                res += s[x - 1]
                --x
                --y
            } else if (b == m) {
                --x
            } else {
                --y
            }
        }
        return StringBuilder(res).reverse() + middle + res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val res = maxPalindrome("3213")
        System.out.println("323".equals(res))
    }
}
