package numeric

import java.util.Arrays

object SubsetConvolution {
    // calculates c[k] = sum(a[i]*b[j] | i|j==k, i&j==0) in O(n * log^2(n))
    fun subsetConvolution(a: IntArray, b: IntArray): IntArray {
        val n = a.size
        val logn: Int = Integer.bitCount(n - 1) + 1
        val ta = Array(logn) { IntArray(n) }
        val tb = Array(logn) { IntArray(n) }
        for (i in 0 until n) {
            ta[Integer.bitCount(i)][i] = a[i]
            tb[Integer.bitCount(i)][i] = b[i]
        }
        for (i in 0 until logn) {
            for (j in 0 until logn) {
                for (k in 0 until n) {
                    if (k shr j and 1 != 0) {
                        ta[i][k] += ta[i][k xor (1 shl j)]
                        tb[i][k] += tb[i][k xor (1 shl j)]
                    }
                }
            }
        }
        val tc = Array(logn) { IntArray(n) }
        for (i in 0 until n) {
            for (j in 0 until logn) {
                for (k in 0..j) {
                    tc[j][i] += ta[k][i] * tb[j - k][i]
                }
            }
        }
        for (i in 0 until logn) {
            for (j in 0 until logn) {
                for (k in 0 until n) {
                    if (k shr j and 1 != 0) {
                        tc[i][k] -= tc[i][k xor (1 shl j)]
                    }
                }
            }
        }
        val result = IntArray(n)
        for (i in 0 until n) {
            result[i] = tc[Integer.bitCount(i)][i]
        }
        return result
    }

    // O(3^log(n))
    fun subsetConvolutionSlow(a: IntArray, b: IntArray): IntArray {
        val n = a.size
        val result = IntArray(n)
        for (i in n - 1 downTo 0) {
            var j: Int = i
            while (true) {
                result[i] += a[j] * b[i xor j]
                if (j == 0) break
                j = j - 1 and i
            }
        }
        return result
    }

    // usage example
    fun main(args: Array<String?>?) {
        val a = intArrayOf(3, 2, 1, 5)
        val b = intArrayOf(6, 3, 4, 8)
        System.out.println(Arrays.toString(subsetConvolution(a, b)))
        System.out.println(Arrays.toString(subsetConvolutionSlow(a, b)))
    }
}
