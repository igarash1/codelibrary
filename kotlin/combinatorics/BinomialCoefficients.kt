package combinatorics

import java.math.BigInteger

object BinomialCoefficients {
    fun binomialTable(n: Int): Array<LongArray> {
        val c = Array(n + 1) { LongArray(n + 1) }
        for (i in 0..n) for (j in 0..i) c[i][j] = if (j == 0) 1 else c[i - 1][j - 1] + c[i - 1][j]
        return c
    }

    fun binomial(n: Long, m: Long): Long {
        var m = m
        m = Math.min(m, n - m)
        var res: Long = 1
        for (i in 0 until m) {
            res = res * (n - i) / (i + 1)
        }
        return res
    }

    // for (int i = 1; i < f.length; i++) f[i] = f[i - 1] + Math.log(i);
    fun binomial(n: Int, m: Int, f: DoubleArray): Double {
        return if (m < 0 || m > n) 0 else Math.exp(f[n] - f[m] - f[n - m])
    }

    // n! % mod
    fun factorial(n: Int, mod: Int): Int {
        var res: Long = 1
        for (i in 2..n) res = res * i % mod
        return (res % mod).toInt()
    }

    // n! mod p, p - prime, O(p*log(n)) complexity
    fun factorial2(n: Int, p: Int): Int {
        var n = n
        var res = 1
        while (n > 1) {
            res = res * if (n / p % 2 == 1) p - 1 else 1 % p
            for (i in 2..n % p) res = res * i % p
            n /= p
        }
        return res % p
    }

    // fact[0] = ifact[0] = fact[1] = ifact[1] = inv[1] = 1;
    // for (int i = 2; i < fact.length; i++)
    //   fact[i] = (int)c((long) fact[i - 1] * i % mod);
    //   inv[i] = (int) ((long) (p - p / i) * inv[p % i] % p);
    //   ifact[i] = (int)c((long) ifact[i - 1] * inv[i] % mod);
    fun binomial(n: Int, m: Int, fact: IntArray, ifact: IntArray, mod: Int): Int {
        return (fact[n].toLong() * ifact[m] % mod * ifact[n - m] % mod).toInt()
    }

    // Usage example
    fun main(args: Array<String?>?) {}
}
