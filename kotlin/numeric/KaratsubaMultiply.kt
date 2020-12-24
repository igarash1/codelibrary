package numeric

import java.util.Arrays

// https://en.wikipedia.org/wiki/Karatsuba_algorithm
object KaratsubaMultiply {
    fun karatsubaMultiply(a: IntArray, b: IntArray): IntArray {
        var a = a
        var b = b
        if (a.size < b.size) a = Arrays.copyOf(a, b.size)
        if (a.size > b.size) b = Arrays.copyOf(b, a.size)
        val n = a.size
        val res = IntArray(n + n)
        if (n <= 10) {
            for (i in 0 until n) for (j in 0 until n) res[i + j] = res[i + j] + a[i] * b[j]
        } else {
            val k = n shr 1
            val a1: IntArray = Arrays.copyOfRange(a, 0, k)
            val a2: IntArray = Arrays.copyOfRange(a, k, n)
            val b1: IntArray = Arrays.copyOfRange(b, 0, k)
            val b2: IntArray = Arrays.copyOfRange(b, k, n)
            val a1b1 = karatsubaMultiply(a1, b1)
            val a2b2 = karatsubaMultiply(a2, b2)
            for (i in 0 until k) a2[i] = a2[i] + a1[i]
            for (i in 0 until k) b2[i] = b2[i] + b1[i]
            val r = karatsubaMultiply(a2, b2)
            for (i in a1b1.indices) r[i] = r[i] - a1b1[i]
            for (i in a2b2.indices) r[i] = r[i] - a2b2[i]
            System.arraycopy(r, 0, res, k, r.size)
            for (i in a1b1.indices) res[i] = res[i] + a1b1[i]
            for (i in a2b2.indices) res[i + n] = res[i + n] + a2b2[i]
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        // (3*x^2+2*x+1) * (4*x+3) = 12*x^3 + 17*x^2 + 10*x + 3
        System.out.println(
            Arrays.equals(intArrayOf(3, 10, 17, 12, 0, 0), karatsubaMultiply(intArrayOf(1, 2, 3), intArrayOf(3, 4)))
        )
    }
}
