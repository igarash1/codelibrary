package misc

import numeric.FFT
import optimization.Simplex

object MonotonicApproximation {
    // returns minimum sum |a[i]-b[i]| where b is non-strictly increasing array
    // see http://codeforces.com/blog/entry/47821
    fun monotonicApproximation(a: IntArray): Long {
        val n = a.size
        var res: Long = 0
        val q: PriorityQueue<Integer> = PriorityQueue(Comparator.reverseOrder())
        q.add(a[0])
        for (i in 1 until n) {
            val x = a[i]
            q.add(x)
            if (q.peek() > x) {
                res += q.peek() - x
                q.remove()
                q.add(x)
            }
        }
        return res
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..9999) {
            val n: Int = rnd.nextInt(100) + 1
            val a: IntArray = rnd.ints(n, 0, 1000).toArray()
            val res1 = monotonicApproximation(a)
            val res2 = slowMonotonicApproximation(a)
            if (res1 != res2) throw RuntimeException("$res1 $res2")
        }
    }

    fun slowMonotonicApproximation(a: IntArray): Long {
        val n = a.size
        val b: IntArray = a.clone()
        Arrays.sort(b)
        val cur = LongArray(n)
        val next = LongArray(n)
        cur[0] = Math.abs(a[0] - b[0])
        for (i in 1 until n) cur[i] = Math.min(cur[i - 1], Math.abs(a[0] - b[i]))
        for (i in 1 until n) {
            next[0] = Math.abs(a[i] - b[0]) + cur[0]
            for (j in 1 until n) next[j] = Math.min(next[j - 1], Math.abs(a[i] - b[j]) + cur[j])
            System.arraycopy(next, 0, cur, 0, n)
        }
        return cur[n - 1]
    }
}
