package graphs.matchings

import java.util.Random

object MaxGeneralMatchingRandomized {
    const val MOD = 29989
    fun pow(a: Int, b: Int): Int {
        var a = a
        var b = b
        var res = 1
        while (b > 0) {
            if (b and 1 != 0) res = res * a % MOD
            a = a * a % MOD
            b = b shr 1
        }
        return res
    }

    fun rank(a: Array<IntArray>): Int {
        var r = 0
        val n = a.size
        for (j in 0 until n) {
            var k: Int
            k = r
            while (k < n && a[k][j] == 0) {
                k++
            }
            if (k == n) continue
            val t = a[r]
            a[r] = a[k]
            a[k] = t
            val inv = pow(a[r][j], MOD - 2)
            for (i in j until n) a[r][i] = a[r][i] * inv % MOD
            for (u in r + 1 until n) for (v in j + 1 until n) a[u][v] = (a[u][v] - a[r][v] * a[u][j] % MOD + MOD) % MOD
            ++r
        }
        return r
    }

    fun maxMatching(d: Array<BooleanArray>): Int {
        val n = d.size
        val a = Array(n) { IntArray(n) }
        val rnd = Random(1)
        for (i in 0 until n) {
            for (j in 0 until i) {
                if (d[i][j]) {
                    a[i][j] = rnd.nextInt(MOD - 1) + 1
                    a[j][i] = MOD - a[i][j]
                }
            }
        }
        return rank(a) / 2
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val res = maxMatching(arrayOf(booleanArrayOf(false, true), booleanArrayOf(true, false)))
        System.out.println(1 == res)
    }
}
