package dp

import java.util.Arrays

object ShortestHamiltonianCycle {
    fun getShortestHamiltonianCycle(dist: Array<IntArray>): Int {
        val n = dist.size
        val dp = Array(1 shl n) { IntArray(n) }
        for (d in dp) Arrays.fill(d, Integer.MAX_VALUE / 2)
        dp[1][0] = 0
        var mask = 1
        while (mask < 1 shl n) {
            for (i in 1 until n) {
                if (mask and 1 shl i != 0) {
                    for (j in 0 until n) {
                        if (mask and 1 shl j != 0) {
                            dp[mask][i] = Math.min(dp[mask][i], dp[mask xor (1 shl i)][j] + dist[j][i])
                        }
                    }
                }
            }
            mask += 2
        }
        var res: Int = Integer.MAX_VALUE
        for (i in 1 until n) {
            res = Math.min(res, dp[(1 shl n) - 1][i] + dist[i][0])
        }

        // reconstruct path
        var cur = (1 shl n) - 1
        val order = IntArray(n)
        var last = 0
        for (i in n - 1 downTo 1) {
            var bj = -1
            for (j in 1 until n) {
                if (cur and 1 shl j != 0 && (bj == -1 || dp[cur][bj] + dist[bj][last] > dp[cur][j] + dist[j][last])) {
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
        val dist = arrayOf(
            intArrayOf(0, 1, 10, 1, 10),
            intArrayOf(1, 0, 10, 10, 1),
            intArrayOf(10, 10, 0, 1, 1),
            intArrayOf(1, 10, 1, 0, 10),
            intArrayOf(10, 1, 1, 10, 0)
        )
        System.out.println(5 == getShortestHamiltonianCycle(dist))
    }
}
