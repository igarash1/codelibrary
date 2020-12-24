package linearalgebra

import java.util.Arrays

object Matrix {
    fun matrixAdd(a: Array<IntArray>, b: Array<IntArray>): Array<IntArray> {
        val n = a.size
        val m: Int = a[0].length
        val res = Array(n) { IntArray(m) }
        for (i in 0 until n) {
            for (j in 0 until m) {
                res[i][j] = a[i][j] + b[i][j]
            }
        }
        return res
    }

    fun matrixMul(a: Array<IntArray>, b: Array<IntArray>): Array<IntArray> {
        val n = a.size
        val m: Int = a[0].length
        val k: Int = b[0].length
        val res = Array(n) { IntArray(k) }
        for (i in 0 until n) {
            for (j in 0 until k) {
                for (p in 0 until m) {
                    res[i][j] = res[i][j] + a[i][p] * b[p][j]
                }
            }
        }
        return res
    }

    fun matrixPow(a: Array<IntArray>, p: Int): Array<IntArray> {
        return if (p == 0) {
            matrixUnit(a.size)
        } else if (p % 2 == 0) {
            matrixPow(matrixMul(a, a), p / 2)
        } else {
            matrixMul(a, matrixPow(a, p - 1))
        }
    }

    fun matrixSumPow(a: Array<IntArray>, p: Int): Array<IntArray> {
        val n = a.size
        if (p == 0) {
            return Array(n) { IntArray(n) }
        }
        return if (p % 2 == 0) {
            matrixMul(
                matrixSumPow(a, p / 2),
                matrixAdd(matrixUnit(n), matrixPow(a, p / 2))
            )
        } else {
            matrixAdd(
                a,
                matrixMul(matrixSumPow(a, p - 1), a)
            )
        }
    }

    fun matrixUnit(n: Int): Array<IntArray> {
        val res = Array(n) { IntArray(n) }
        for (i in 0 until n) {
            res[i][i] = 1
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val a = arrayOf(intArrayOf(1, 2), intArrayOf(3, 4))
        val b = matrixUnit(2)
        val c = matrixMul(a, b)
        val x = arrayOf(intArrayOf(2, 0), intArrayOf(0, 2))
        val y = matrixSumPow(x, 3)
        System.out.println(Arrays.deepToString(y))
    }
}
