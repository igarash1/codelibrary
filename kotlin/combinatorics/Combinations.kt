package combinatorics

import java.util.Arrays

// https://en.wikipedia.org/wiki/Combination
object Combinations {
    fun nextCombination(comb: IntArray, n: Int): Boolean {
        val k = comb.size
        var i = k - 1
        while (i >= 0) {
            if (comb[i] < n - k + i) {
                ++comb[i]
                while (++i < k) {
                    comb[i] = comb[i - 1] + 1
                }
                return true
            }
            i--
        }
        return false
    }

    fun combinationByNumber(n: Int, k: Int, number: Long): IntArray {
        var number = number
        val c = IntArray(k)
        var cnt = n
        for (i in 0 until k) {
            var j = 1
            while (true) {
                val am = binomial((cnt - j).toLong(), (k - 1 - i).toLong())
                if (number < am) break
                number -= am
                ++j
            }
            c[i] = if (i > 0) c[i - 1] + j else j - 1
            cnt -= j
        }
        return c
    }

    fun numberByCombination(c: IntArray, n: Int): Long {
        val k = c.size
        var res: Long = 0
        var prev = -1
        for (i in 0 until k) {
            for (j in prev + 1 until c[i]) {
                res += binomial((n - 1 - j).toLong(), (k - 1 - i).toLong())
            }
            prev = c[i]
        }
        return res
    }

    fun binomial(n: Long, k: Long): Long {
        var k = k
        k = Math.min(k, n - k)
        var res: Long = 1
        for (i in 0 until k) {
            res = res * (n - i) / (i + 1)
        }
        return res
    }

    fun nextCombinationWithRepeats(p: IntArray, n: Int): Boolean {
        val k = p.size
        var i = k - 1
        while (i >= 0) {
            if (p[i] < n - 1) {
                ++p[i]
                while (++i < k) {
                    p[i] = p[i - 1]
                }
                return true
            }
            i--
        }
        return false
    }

    // Usage example
    fun main(args: Array<String?>?) {
        var p = intArrayOf(0, 1)
        System.out.println(!nextCombination(p, 2))
        System.out.println(Arrays.equals(intArrayOf(0, 1), p))
        p = IntArray(2)
        System.out.println(nextCombinationWithRepeats(p, 2))
        System.out.println(Arrays.equals(intArrayOf(0, 1), p))
        System.out.println(nextCombinationWithRepeats(p, 2))
        System.out.println(Arrays.equals(intArrayOf(1, 1), p))
        System.out.println(!nextCombinationWithRepeats(p, 2))
        System.out.println(78L == numberByCombination(intArrayOf(1, 2, 3, 6, 8), 9))
        System.out.println(Arrays.toString(combinationByNumber(9, 5, 78)))
        p = IntArray(3)
        do {
            System.out.println(Arrays.toString(p))
        } while (nextCombinationWithRepeats(p, 3))
    }
}
