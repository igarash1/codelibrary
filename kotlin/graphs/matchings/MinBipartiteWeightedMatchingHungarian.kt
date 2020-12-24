package graphs.matchings

import java.util.Arrays

// https://en.wikipedia.org/wiki/Hungarian_algorithm in O(n^2 * m)
object MinBipartiteWeightedMatchingHungarian {
    // a[n][m], n <= m, sum(a[i][p[i]]) -> min
    fun minWeightPerfectMatching(a: Array<IntArray>): Int {
        val n = a.size
        val m: Int = a[0].length
        val u = IntArray(n)
        val v = IntArray(m)
        val p = IntArray(m)
        val way = IntArray(m)
        for (i in 1 until n) {
            val minv = IntArray(m)
            Arrays.fill(minv, Integer.MAX_VALUE)
            val used = BooleanArray(m)
            p[0] = i
            var j0 = 0
            while (p[j0] != 0) {
                used[j0] = true
                val i0 = p[j0]
                var delta: Int = Integer.MAX_VALUE
                var j1 = 0
                for (j in 1 until m) if (!used[j]) {
                    val d = a[i0][j] - u[i0] - v[j]
                    if (minv[j] > d) {
                        minv[j] = d
                        way[j] = j0
                    }
                    if (delta > minv[j]) {
                        delta = minv[j]
                        j1 = j
                    }
                }
                for (j in 0 until m) if (used[j]) {
                    u[p[j]] += delta
                    v[j] -= delta
                } else minv[j] -= delta
                j0 = j1
            }
            while (j0 != 0) {
                val j1 = way[j0]
                p[j0] = p[j1]
                j0 = j1
            }
        }
        val matching = IntArray(n)
        for (i in 1 until m) matching[p[i]] = i
        return -v[0]
    }

    // Usage example
    fun main(args: Array<String?>?) {
        // row1 and col1 should contain 0
        val a = arrayOf(intArrayOf(0, 0, 0), intArrayOf(0, 1, 2), intArrayOf(0, 1, 2))
        val res = minWeightPerfectMatching(a)
        System.out.println(3 == res)
    }
}
