package misc

import numeric.FFT
import optimization.Simplex

// https://en.wikipedia.org/wiki/Exponentiation_by_squaring
object BinaryExponentiation {
    fun pow(x: Int, n: Int, mod: Int): Int {
        var n = n
        var res = 1
        var p = x.toLong()
        while (n > 0) {
            if (n and 1 != 0) {
                res = (res * p % mod).toInt()
            }
            n = n shr 1
            p = p * p % mod
        }
        return res
    }

    // usage example
    fun main(args: Array<String?>?) {
        System.out.println(8 == pow(2, 3, 100))
    }
}
