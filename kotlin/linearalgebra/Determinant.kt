package linearalgebra

import java.util.stream.IntStream

object Determinant {
    fun det(matrix: Array<DoubleArray>): Double {
        val EPS = 1e-10
        val n = matrix.size
        val a: Array<DoubleArray> =
            IntStream.range(0, n).mapToObj { i -> matrix[i].clone() }.toArray { _Dummy_.__Array__() } // make a copy
        var res = 1.0
        for (i in 0 until n) {
            var p: Int = i
            for (j in i + 1 until n) if (Math.abs(a[p][i]) < Math.abs(a[j][i])) p = j
            if (Math.abs(a[p][i]) < EPS) return 0
            if (i != p) {
                res = -res
                val t = a[i]
                a[i] = a[p]
                a[p] = t
            }
            res *= a[i][i]
            for (j in i + 1 until n) a[i][j] /= a[i][i]
            for (j in 0 until n) if (j != i && Math.abs(a[j][i]) > EPS /*optimizes overall complexity to O(n^2) for sparse matrices*/) for (k in i + 1 until n) a[j][k] -= a[i][k] * a[j][i]
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val d = det(arrayOf(doubleArrayOf(0.0, 1.0), doubleArrayOf(-1.0, 0.0)))
        System.out.println(Math.abs(d - 1) < 1e-10)
    }
}
