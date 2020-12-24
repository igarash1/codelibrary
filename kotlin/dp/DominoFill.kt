package dp

import numeric.FFT
import optimization.Simplex

object DominoFill {
    /**
     * .....
     * ..234
     * 01
     */
    fun method1(R: Int, C: Int): Int {
        var prev = IntArray(1 shl C)
        prev[(1 shl C) - 1] = 1
        for (r in 0 until R) {
            for (c in 0 until C) {
                val cur = IntArray(1 shl C)
                for (mask in 0 until 1 shl C) {
                    if (mask and (1 shl c) != 0) {
                        cur[mask xor (1 shl c)] += prev[mask] // do nothing
                        if (c > 0 && mask and (1 shl c - 1) == 0) {
                            cur[mask or (1 shl c - 1)] += prev[mask] // horizontal
                        }
                    } else {
                        cur[mask or (1 shl c)] += prev[mask] // vertical
                    }
                }
                prev = cur
            }
        }
        return prev[(1 shl C) - 1]
    }

    /**
     * .....
     * ..012
     * 34
     */
    fun method2(R: Int, C: Int): Int {
        var prev = IntArray(1 shl C)
        prev[(1 shl C) - 1] = 1
        for (r in 0 until R) {
            for (c in 0 until C) {
                val cur = IntArray(1 shl C)
                for (mask in 0 until 1 shl C) {
                    val nmask: Int = mask shl 1 and (1 shl C) - 1
                    if (mask and (1 shl C - 1) != 0) {
                        cur[nmask] += prev[mask] // do nothing
                        if (c > 0 && mask and 1 == 0) {
                            cur[nmask or 3] += prev[mask] // horizontal
                        }
                    } else {
                        cur[nmask or 1] += prev[mask] // vertical
                    }
                }
                prev = cur
            }
        }
        return prev[(1 shl C) - 1]
    }

    // random test
    fun main(args: Array<String?>?) {
        for (r in 1..9) {
            for (c in 1..9) {
                val res1 = method1(r, c)
                val res2 = method2(r, c)
                if (res1 != res2) throw RuntimeException()
            }
        }
    }

    fun method0(R: Int, C: Int): Int {
        val dp = Array(R + 1) { Array(C) { IntArray(1 shl C) } }
        dp[0][C - 1][(1 shl C) - 1] = 1
        for (r in 1..R) {
            for (c in 0 until C) {
                val prev = if (c > 0) dp[r][c - 1] else dp[r - 1][C - 1]
                for (mask in 0 until 1 shl C) {
                    if (mask and (1 shl c) != 0) {
                        dp[r][c][mask xor (1 shl c)] += prev[mask] // do nothing
                        if (c > 0 && mask and (1 shl c - 1) == 0) {
                            dp[r][c][mask or (1 shl c - 1)] += prev[mask] // horizontal
                        }
                    } else {
                        dp[r][c][mask or (1 shl c)] += prev[mask] // vertical
                    }
                }
            }
        }
        return dp[R][C - 1][(1 shl C) - 1]
    }
}
