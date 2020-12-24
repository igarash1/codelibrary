package misc

import java.util.Random

// https://en.wikipedia.org/wiki/Ternary_search
// Finds the smallest i in [a, b] that maximizes f(i), assuming that f(a) < ... < f(i) ≥ ··· ≥ f(b)
object TernarySearch {
    fun ternarySearch(f: IntUnaryOperator, fromInclusive: Int, toInclusive: Int): Int {
        var lo = fromInclusive - 1
        var hi = toInclusive
        while (hi - lo > 1) {
            val mid = lo + hi ushr 1
            if (f.applyAsInt(mid) < f.applyAsInt(mid + 1)) {
                lo = mid
            } else {
                hi = mid
            }
        }
        return hi
    }

    fun ternarySearch2(f: IntUnaryOperator, fromInclusive: Int, toInclusive: Int): Int {
        var lo = fromInclusive
        var hi = toInclusive
        while (hi > lo + 2) {
            val m1 = lo + (hi - lo) / 3
            val m2 = hi - (hi - lo) / 3
            if (f.applyAsInt(m1) < f.applyAsInt(m2)) lo = m1 else hi = m2
        }
        var res = lo
        for (i in lo + 1..hi) if (f.applyAsInt(res) < f.applyAsInt(i)) res = i
        return res
    }

    fun ternarySearchDouble(f: DoubleUnaryOperator, lo: Double, hi: Double): Double {
        var lo = lo
        var hi = hi
        for (step in 0..999) {
            val m1 = lo + (hi - lo) / 3
            val m2 = hi - (hi - lo) / 3
            if (f.applyAsDouble(m1) < f.applyAsDouble(m2)) lo = m1 else hi = m2
        }
        return (lo + hi) / 2
    }

    // random tests
    fun main(args: Array<String?>?) {
        System.out.println(ternarySearchDouble(DoubleUnaryOperator { x -> -(x - 2) * (x - 2) }, -10.0, 10.0))
        val rnd = Random(1)
        for (step in 0..9999) {
            val n: Int = rnd.nextInt(20) + 1
            val p: Int = rnd.nextInt(n)
            val a = IntArray(n)
            val range = 10
            a[p] = rnd.nextInt(range)
            for (i in p - 1 downTo 0) a[i] = a[i + 1] - rnd.nextInt(range) - 1
            for (i in p + 1 until n) a[i] = a[i - 1] - rnd.nextInt(range)
            val res = ternarySearch(IntUnaryOperator { i -> a[i] }, 0, a.size - 1)
            val res2 = ternarySearch2(IntUnaryOperator { i -> a[i] }, 0, a.size - 1)
            if (p != res || p != res2) throw RuntimeException()
        }
    }
}
