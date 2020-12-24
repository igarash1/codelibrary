package structures

import numeric.FFT
import optimization.Simplex

object FenwickTree2D {
    fun add(t: Array<IntArray>, r: Int, c: Int, value: Int) {
        var i = r
        while (i < t.size) {
            var j = c
            while (j < t[0].length) {
                t[i][j] += value
                j = j or j + 1
            }
            i = i or i + 1
        }
    }

    // sum[(0, 0), (r, c)]
    fun sum(t: Array<IntArray>, r: Int, c: Int): Int {
        var res = 0
        var i = r
        while (i >= 0) {
            var j = c
            while (j >= 0) {
                res += t[i][j]
                j = (j and j + 1) - 1
            }
            i = (i and i + 1) - 1
        }
        return res
    }

    // sum[(r1, c1), (r2, c2)]
    fun sum(t: Array<IntArray>, r1: Int, c1: Int, r2: Int, c2: Int): Int {
        return sum(t, r2, c2) - sum(t, r1 - 1, c2) - sum(t, r2, c1 - 1) + sum(t, r1 - 1, c1 - 1)
    }

    operator fun get(t: Array<IntArray>, r: Int, c: Int): Int {
        return sum(t, r, c, r, c)
    }

    operator fun set(t: Array<IntArray>, r: Int, c: Int, value: Int) {
        add(t, r, c, -FenwickTree2D[t, r, c] + value)
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val t = Array(10) { IntArray(20) }
        add(t, 0, 0, 1)
        add(t, 9, 19, -2)
        System.out.println(-1 == sum(t, 0, 0, 9, 19))
    }
}
