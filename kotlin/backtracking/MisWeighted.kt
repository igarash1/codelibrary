package backtracking

import java.util.Random

object MisWeighted {
    // maximum independent weighted set in O(3^(n/3))
    // prerequisite: g[i] has i'th bit set
    fun mis(g: LongArray, unused: Long, weights: IntArray): Int {
        if (unused == 0L) return 0
        var v = -1
        var u: Int = Long.numberOfTrailingZeros(unused)
        while (u < g.size) {
            if (v == -1 || Long.bitCount(g[v] and unused) > Long.bitCount(g[u] and unused)) v = u
            u += Long.numberOfTrailingZeros(unused shr u + 1) + 1
        }
        var res = 0
        val nv = g[v] and unused
        var y: Int = Long.numberOfTrailingZeros(nv)
        while (y < g.size) {
            res = Math.max(res, weights[y] + mis(g, unused and g[y].inv(), weights))
            y += Long.numberOfTrailingZeros(nv shr y + 1) + 1
        }
        return res
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999) {
            val n: Int = rnd.nextInt(16) + 1
            val g = LongArray(n)
            val weights = IntArray(n)
            for (i in 0 until n) weights[i] = rnd.nextInt(1000)
            for (i in 0 until n) for (j in 0 until i) if (rnd.nextBoolean()) {
                g[i] = g[i] or (1L shl j)
                g[j] = g[j] or (1L shl i)
            }
            for (i in 0 until n) g[i] = g[i] or (1 shl i).toLong()
            val res1 = mis(g, (1L shl n) - 1, weights)
            val res2 = misSlow(g, weights)
            if (res1 != res2) throw RuntimeException()
        }
    }

    fun misSlow(g: LongArray, weights: IntArray): Int {
        var res = 0
        val n = g.size
        for (set in 0 until 1 shl n) {
            var ok = true
            for (i in 0 until n) for (j in 0 until i) ok =
                ok and (set and (1 shl i) == 0 || set and (1 shl j) == 0 || g[i] and (1L shl j) == 0L)
            if (ok) {
                var cur = 0
                for (i in 0 until n) if (set and (1 shl i) != 0) cur += weights[i]
                res = Math.max(res, cur)
            }
        }
        return res
    }
}
