package structures

import numeric.FFT
import optimization.Simplex

object FenwickTree {
    // T[i] += value
    fun add(t: IntArray, i: Int, value: Int) {
        var i = i
        while (i < t.size) {
            t[i] += value
            i = i or i + 1
        }
    }

    // sum[0..i]
    fun sum(t: IntArray, i: Int): Int {
        var i = i
        var res = 0
        while (i >= 0) {
            res += t[i]
            i = (i and i + 1) - 1
        }
        return res
    }

    ///////////////////////////////////////////////////
    // T[i] = max(T[i], value)
    operator fun set(t: IntArray, i: Int, value: Int) {
        var i = i
        while (i < t.size) {
            t[i] = Math.max(t[i], value)
            i = i or i + 1
        }
    }

    // max[0..i]
    fun max(t: IntArray, i: Int): Int {
        var i = i
        var res: Int = Integer.MIN_VALUE
        while (i >= 0) {
            res = Math.max(res, t[i])
            i = (i and i + 1) - 1
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val t = IntArray(10)
        add(t, 0, 1)
        add(t, 9, -2)
        System.out.println(-1 == sum(t, 9))
    }
}
