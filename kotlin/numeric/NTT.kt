package numeric

import java.math.BigInteger

object NTT {
    fun pow(x: Int, n: Int, mod: Int): Int {
        var n = n
        var res = 1
        var p = x.toLong()
        while (n > 0) {
            if (n and 1 != 0) res = (res * p % mod).toInt()
            n = n shr 1
            p = p * p % mod
        }
        return res
    }

    // a.length == b.length == 2^x
    fun ntt(a: IntArray, invert: Boolean, mod: Int, root: Int) {
        val n = a.size
        val shift: Int = 32 - Integer.numberOfTrailingZeros(n)
        for (i in 1 until n) {
            val j: Int = Integer.reverse(i shl shift)
            if (i < j) {
                val temp = a[i]
                a[i] = a[j]
                a[j] = temp
            }
        }
        val root_inv = pow(root, mod - 2, mod)
        var len = 1
        while (len < n) {
            val wlen = pow(if (invert) root_inv else root, (mod - 1) / (2 * len), mod)
            var i = 0
            while (i < n) {
                var j = 0
                var w = 1
                while (j < len) {
                    val u = a[i + j]
                    val v = (a[i + j + len].toLong() * w % mod).toInt()
                    a[i + j] = (u + v) % mod
                    a[i + j + len] = (u - v + mod) % mod
                    w = (w.toLong() * wlen % mod).toInt()
                    ++j
                }
                i += 2 * len
            }
            len = len shl 1
        }
        if (invert) {
            val nrev = pow(n, mod - 2, mod)
            for (i in 0 until n) a[i] = (a[i].toLong() * nrev % mod).toInt()
        }
    }

    fun multiply(a: IntArray, b: IntArray): IntArray {
        val need = a.size + b.size
        val n: Int = Integer.highestOneBit(need - 1) shl 1
        val A = IntArray(n)
        val B = IntArray(n)
        for (i in a.indices) A[i] = a[i]
        for (i in b.indices) B[i] = b[i]
        val mod = 998244353 // 2^23 * 119 + 1
        val root = 3
        ntt(A, false, mod, root)
        ntt(B, false, mod, root)
        for (i in 0 until n) A[i] = (A[i].toLong() * B[i] % mod).toInt()
        ntt(A, true, mod, root)
        var carry = 0
        for (i in 0 until need) {
            A[i] += carry
            carry = A[i] / 10
            A[i] %= 10
        }
        return A
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999) {
            val n1: Int = rnd.nextInt(50) + 1
            var s1 = ""
            val a = IntArray(n1)
            for (i in 0 until n1) {
                val x: Int = rnd.nextInt(10)
                s1 = x + s1
                a[i] = x
            }
            val n2: Int = rnd.nextInt(50) + 1
            var s2 = ""
            val b = IntArray(n2)
            for (i in 0 until n2) {
                val x: Int = rnd.nextInt(10)
                s2 = x + s2
                b[i] = x
            }
            val res = multiply(a, b)
            var s = ""
            for (v in res) {
                s = v + s
            }
            val mul: BigInteger = BigInteger(s1).multiply(BigInteger(s2))
            if (!mul.equals(BigInteger(s))) throw RuntimeException()
        }

        //        generatePrimitiveRootsOfUnity(1 << 20);
    }

    fun generatePrimitiveRootsOfUnity(N: Int) {
        for (i in 900..999) {
            val mod = N * i + 1
            if (!BigInteger.valueOf(mod).isProbablePrime(100)) continue
            for (root in 2..999) {
                if (pow(root, N, mod) == 1 && pow(root, N / 2, mod) != 1) {
                    System.out.println("$i $mod $root")
                    break
                }
            }
        }
    }
}
