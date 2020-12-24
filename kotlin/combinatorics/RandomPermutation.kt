package combinatorics

import numeric.FFT
import optimization.Simplex

object RandomPermutation {
    fun shuffle(a: IntArray) {
        val rnd = Random()
        for (i in a.size - 1 downTo 1) {
            val j: Int = rnd.nextInt(i + 1)
            val t = a[i]
            a[i] = a[j]
            a[j] = t
        }
    }

    fun getRandomPermutation(n: Int): IntArray {
        val rnd = Random()
        val res = IntArray(n)
        for (i in 0 until n) {
            val j: Int = rnd.nextInt(i + 1)
            res[i] = res[j]
            res[j] = i
        }
        return res
    }

    fun getRandomArrangement(n: Int, m: Int): IntArray {
        val rnd = Random()
        val res = IntArray(n)
        for (i in 0 until n) res[i] = i
        for (i in 0 until m) {
            val j: Int = i + rnd.nextInt(n - i)
            val t = res[i]
            res[i] = res[j]
            res[j] = t
        }
        return Arrays.copyOf(res, m)
    }

    fun getRandomCombination(n: Int, m: Int): IntArray {
        val res = getRandomArrangement(n, m)
        Arrays.sort(res)
        return res
    }

    // for small m
    fun getRandomArrangement2(n: Int, m: Int): IntArray {
        val rnd = Random()
        val set: Set<Integer> = HashSet()
        val res = IntArray(m)
        while (set.size() < m) {
            val x: Int = rnd.nextInt(n)
            if (set.add(x)) res[set.size() - 1] = x
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        System.out.println(Arrays.toString(getRandomPermutation(5)))
    }
}
