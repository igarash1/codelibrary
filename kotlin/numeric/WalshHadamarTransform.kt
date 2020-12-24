package numeric

import java.util.Arrays

object WalshHadamarTransform {
    // calculates c[k] = sum(a[i]*b[j] | i op j == k), where op = XOR | OR | AND
    // complexity: O(n*log(n))
    fun convolution(a: IntArray, b: IntArray, op: Operation): IntArray {
        transform(a, op, false)
        transform(b, op, false)
        for (i in a.indices) {
            a[i] *= b[i]
        }
        transform(a, op, true)
        return a
    }

    fun transform(a: IntArray, op: Operation, inverse: Boolean) {
        val n = a.size
        var step = 1
        while (step < n) {
            var i = 0
            while (i < n) {
                for (j in i until i + step) {
                    val u = a[j]
                    val v = a[j + step]
                    when (op) {
                        Operation.XOR -> {
                            a[j] = u + v
                            a[j + step] = u - v
                        }
                        Operation.OR -> {
                            a[j] = if (inverse) v else u + v
                            a[j + step] = if (inverse) u - v else u
                        }
                        Operation.AND -> {
                            a[j] = if (inverse) v - u else v
                            a[j + step] = if (inverse) u else v + u
                        }
                    }
                }
                i += 2 * step
            }
            step *= 2
        }
        if (op == Operation.XOR && inverse) {
            for (i in 0 until n) {
                a[i] /= n
            }
        }
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val a = intArrayOf(3, 2, 1, 5)
        val b = intArrayOf(6, 3, 4, 8)
        System.out.println(Arrays.toString(convolution(a, b, Operation.AND)))
    }

    enum class Operation {
        XOR, OR, AND
    }
}
