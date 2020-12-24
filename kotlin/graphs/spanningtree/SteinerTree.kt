package graphs.spanningtree

object SteinerTree {
    fun minLengthSteinerTree(g: Array<IntArray>, verticesToConnect: IntArray): Int {
        val n = g.size
        val m = verticesToConnect.size
        if (m <= 1) return 0
        for (k in 0 until n) for (i in 0 until n) for (j in 0 until n) g[i][j] = Math.min(g[i][j], g[i][k] + g[k][j])
        val dp = Array(1 shl m) { IntArray(n) }
        for (i in 0 until m) for (j in 0 until n) dp[1 shl i][j] = g[verticesToConnect[i]][j]
        for (i in 1 until 1 shl m) {
            if (i - 1 and i != 0) {
                for (j in 0 until n) {
                    dp[i][j] = Integer.MAX_VALUE / 2
                    var k: Int = i - 1 and i
                    while (k > 0) {
                        dp[i][j] = Math.min(dp[i][j], dp[k][j] + dp[i xor k][j])
                        k = k - 1 and i
                    }
                }
                for (j in 0 until n) {
                    for (k in 0 until n) {
                        dp[i][j] = Math.min(dp[i][j], dp[i][k] + g[k][j])
                    }
                }
            }
        }
        return dp[(1 shl m) - 1][verticesToConnect[0]]
    }
}
