package linearalgebra

import java.util.stream.IntStream

object Gauss {
    fun gauss(A: Array<DoubleArray>, B: DoubleArray): DoubleArray {
        val n = A.size
        val a: Array<DoubleArray> =
            IntStream.range(0, n).mapToObj { i -> A[i].clone() }.toArray { _Dummy_.__Array__() } // make a copy
        val b: DoubleArray = B.clone()
        for (row in 0 until n) {
            var best: Int = row
            for (i in row + 1 until n) if (Math.abs(a[best][row]) < Math.abs(a[i][row])) best = i
            val tt = a[row]
            a[row] = a[best]
            a[best] = tt
            val t = b[row]
            b[row] = b[best]
            b[best] = t
            for (i in row + 1 until n) a[row][i] /= a[row][row]
            b[row] /= a[row][row]
            // a[row][row] = 1;
            for (i in 0 until n) {
                val z = a[i][row]
                if (i != row && z != 0.0) {
                    // row + 1 instead of row is an optimization
                    for (j in row + 1 until n) a[i][j] -= a[row][j] * z
                    b[i] -= b[row] * z
                }
            }
        }
        return b
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val a = arrayOf(doubleArrayOf(4.0, 2.0, -1.0), doubleArrayOf(2.0, 4.0, 3.0), doubleArrayOf(-1.0, 3.0, 5.0))
        val b = doubleArrayOf(1.0, 0.0, 0.0)
        val x = gauss(a, b)
        for (i in a.indices) {
            var y = 0.0
            for (j in 0 until a[i].length) y += a[i][j] * x[j]
            if (Math.abs(b[i] - y) > 1e-9) throw RuntimeException()
        }
    }
}
