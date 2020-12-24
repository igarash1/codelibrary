package combinatorics

import numeric.FFT
import optimization.Simplex

object Permutations {
    fun nextPermutation(p: IntArray): Boolean {
        var a = p.size - 2
        while (a >= 0) {
            if (p[a] < p[a + 1]) {
                var b = p.size - 1
                while (true) {
                    if (p[b] > p[a]) {
                        var t = p[a]
                        p[a] = p[b]
                        p[b] = t
                        ++a
                        b = p.size - 1
                        while (a < b) {
                            t = p[a]
                            p[a] = p[b]
                            p[b] = t
                            ++a
                            --b
                        }
                        return true
                    }
                    --b
                }
            }
            --a
        }
        return false
    }

    fun permutationByNumber(n: Int, number: Long): IntArray {
        var number = number
        val fact = LongArray(n)
        fact[0] = 1
        for (i in 1 until n) {
            fact[i] = i * fact[i - 1]
        }
        val p = IntArray(n)
        val free = IntArray(n)
        for (i in 0 until n) {
            free[i] = i
        }
        for (i in 0 until n) {
            val pos = (number / fact[n - 1 - i]).toInt()
            p[i] = free[pos]
            System.arraycopy(free, pos + 1, free, pos, n - 1 - pos)
            number %= fact[n - 1 - i]
        }
        return p
    }

    fun numberByPermutation(p: IntArray): Long {
        val n = p.size
        val fact = LongArray(n)
        fact[0] = 1
        for (i in 1 until n) {
            fact[i] = i * fact[i - 1]
        }
        var res: Long = 0
        for (i in 0 until n) {
            var a = p[i]
            for (j in 0 until i) {
                if (p[j] < p[i]) {
                    --a
                }
            }
            res += a * fact[n - 1 - i]
        }
        return res
    }

    fun generatePermutations(p: IntArray, depth: Int) {
        val n = p.size
        if (depth == n) {
            System.out.println(Arrays.toString(p))
            return
        }
        for (i in 0 until n) {
            if (p[i] == 0) {
                p[i] = depth
                generatePermutations(p, depth + 1)
                p[i] = 0
            }
        }
    }

    fun nextPermutation(x: Long): Long {
        val s = x and -x
        val r = x + s
        var ones = x xor r
        ones = (ones shr 2) / s
        return r or ones
    }

    fun decomposeIntoCycles(p: IntArray): List<List<Integer>> {
        val n = p.size
        val vis = BooleanArray(n)
        val res: List<List<Integer>> = ArrayList()
        for (i in 0 until n) {
            if (vis[i]) continue
            var j: Int = i
            val cur: List<Integer> = ArrayList()
            do {
                cur.add(j)
                vis[j] = true
                j = p[j]
            } while (j != i)
            res.add(cur)
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        // print all permutations method 1
        generatePermutations(IntArray(2), 1)

        // print all permutations method 2
        val p = intArrayOf(0, 1, 2)
        var cnt = 0
        do {
            System.out.println(Arrays.toString(p))
            if (!Arrays.equals(p, permutationByNumber(p.size, numberByPermutation(p)))
                || cnt.toLong() != numberByPermutation(permutationByNumber(p.size, cnt.toLong()))
            ) throw RuntimeException()
            ++cnt
        } while (nextPermutation(p))
        System.out.println(5L == numberByPermutation(p))
        System.out.println(Arrays.equals(intArrayOf(1, 0, 2), permutationByNumber(3, 2)))
        System.out.println(13L == nextPermutation(11))
        System.out.println(decomposeIntoCycles(intArrayOf(0, 2, 1, 3)))
    }
}
