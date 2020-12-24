package dp

import java.util.Arrays

object ShortestHamiltonianPath {
    fun getShortestHamiltonianPath(dist: Array<IntArray>): Int {
        val n = dist.size
        val dp = Array(1 shl n) { IntArray(n) }
        for (d in dp) Arrays.fill(d, Integer.MAX_VALUE / 2)
        for (i in 0 until n) dp[1 shl i][i] = 0
        for (mask in 0 until 1 shl n) {
            for (i in 0 until n) {
                if (mask and 1 shl i != 0) {
                    for (j in 0 until n) {
                        if (mask and 1 shl j != 0) {
                            dp[mask][i] = Math.min(dp[mask][i], dp[mask xor (1 shl i)][j] + dist[j][i])
                        }
                    }
                }
            }
        }
        var res: Int = Integer.MAX_VALUE
        for (i in 0 until n) {
            res = Math.min(res, dp[(1 shl n) - 1][i])
        }

        // reconstruct path
        var cur = (1 shl n) - 1
        val order = IntArray(n)
        var last = -1
        for (i in n - 1 downTo 0) {
            var bj = -1
            for (j in 0 until n) {
                if (cur and 1 shl j != 0
                    && (bj == -1
                            || dp[cur][bj] + (if (last == -1) 0 else dist[bj][last])
                            > dp[cur][j] + if (last == -1) 0 else dist[j][last])
                ) {
                    bj = j
                }
            }
            order[i] = bj
            cur = cur xor (1 shl bj)
            last = bj
        }
        System.out.println(Arrays.toString(order))
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val dist = arrayOf(intArrayOf(8, 1, 6), intArrayOf(3, 5, 7), intArrayOf(4, 9, 2))
        System.out.println(5 == getShortestHamiltonianPath(dist))
    }
}
