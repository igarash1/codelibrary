package structures

import numeric.FFT
import optimization.Simplex

object FenwickTree3D {
    fun add(t: Array<Array<IntArray>>, x: Int, y: Int, z: Int, value: Int) {
        var i = x
        while (i < t.size) {
            var j = y
            while (j < t[0].length) {
                var k = z
                while (k < t[0][0].length) {
                    t[i][j][k] += value
                    k = k or k + 1
                }
                j = j or j + 1
            }
            i = i or i + 1
        }
    }

    // sum[(0, 0, 0), (x, y, z)]
    fun sum(t: Array<Array<IntArray>>, x: Int, y: Int, z: Int): Int {
        var res = 0
        var i = x
        while (i >= 0) {
            var j = y
            while (j >= 0) {
                var k = z
                while (k >= 0) {
                    res += t[i][j][k]
                    k = (k and k + 1) - 1
                }
                j = (j and j + 1) - 1
            }
            i = (i and i + 1) - 1
        }
        return res
    }

    fun main(args: Array<String?>?) {
        val t = Array(10) { Array(20) { IntArray(30) } }
        add(t, 0, 0, 0, 1)
        add(t, 9, 19, 29, -2)
        System.out.println(-1 == sum(t, 9, 19, 29))
    }
}
