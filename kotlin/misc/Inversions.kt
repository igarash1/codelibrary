package misc

import numeric.FFT
import optimization.Simplex

object Inversions {
    // warning: a is modified during processing
    fun inversions(a: IntArray, low: Int, high: Int): Long {
        if (high - low < 2) return 0
        val mid = low + high ushr 1
        var res = inversions(a, low, mid) + inversions(a, mid, high)
        val b: IntArray = Arrays.copyOfRange(a, low, mid)
        var i = low
        var j = mid
        var k = 0
        while (k < b.size) {
            if (j == high || b[k] <= a[j]) {
                a[i] = b[k++]
            } else {
                a[i] = a[j++]
                res += (b.size - k).toLong()
            }
            i++
        }
        return res
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999) {
            val n: Int = rnd.nextInt(100) + 1
            val p: IntArray = rnd.ints(n, 0, n).toArray()
            val res1 = inversions(p.clone(), 0, p.size)
            val res2 = slowInversions(p)
            if (res1 != res2) throw RuntimeException()
        }
    }

    fun slowInversions(p: IntArray): Long {
        var res: Long = 0
        for (i in p.indices) for (j in 0 until i) if (p[j] > p[i]) ++res
        return res
    }
}
