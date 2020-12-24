package numeric

import java.math.BigInteger

object NewtonInterpolation {
    // https://en.wikipedia.org/wiki/Divided_differences#Example
    fun getDividedDifferences(x: IntArray, y: IntArray, mod: Int): IntArray {
        val MOD: BigInteger = BigInteger.valueOf(mod)
        val n = x.size
        var z: IntArray = y.clone()
        val res = IntArray(n)
        res[0] = z[0]
        for (i in 0 until n - 1) {
            val nz = IntArray(n - 1 - i)
            for (j in 0 until n - 1 - i) {
                val div: Int = BigInteger.valueOf(x[j + i + 1] - x[j]).modInverse(MOD).intValue()
                nz[j] = ((z[j + 1].toLong() - z[j]) * div % mod + mod).toInt() % mod
            }
            z = nz
            res[i + 1] = z[0]
        }
        return res
    }

    // https://en.wikipedia.org/wiki/Newton_polynomial#Definition
    fun interpolate(X: IntArray, dd: IntArray, mod: Int, x: Int): Int {
        var res = 0
        var m = 1
        for (i in X.indices) {
            res = ((res + dd[i].toLong() * m) % mod).toInt()
            m = ((m * (x.toLong() - X[i]) % mod + mod) % mod).toInt()
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val x = intArrayOf(7, 2, 1, 3, 5, 6)
        val n = x.size
        val y = IntArray(n)
        for (i in 0 until n) {
            y[i] = f(x[i])
        }
        val mod = 1000000007
        val dd = getDividedDifferences(x, y, mod)
        System.out.println(Arrays.toString(dd))
        for (i in 0 until n) {
            val v = interpolate(x, dd, mod, x[i])
            System.out.println(v == y[i])
        }
    }

    fun f(x: Int): Int {
        return x * x * x + 5 * x * x + x + 3
    }
}
