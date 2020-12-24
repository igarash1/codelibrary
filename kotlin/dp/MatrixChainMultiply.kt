package dp

object MatrixChainMultiply {
    fun solveIterative(s: IntArray): Int {
        val n = s.size - 1
        val p = Array(n) { IntArray(n) }
        val m = Array(n) { IntArray(n) }
        for (len in 2..n) {
            var a = 0
            while (a + len <= n) {
                val b = a + len - 1
                m[a][b] = Integer.MAX_VALUE
                for (c in a until b) {
                    val v = m[a][c] + m[c + 1][b] + s[a] * s[c + 1] * s[b + 1]
                    if (m[a][b] > v) {
                        m[a][b] = v
                        p[a][b] = c
                    }
                }
                a++
            }
        }
        return m[0][n - 1]
    }

    fun solveRecursive(s: IntArray): Int {
        val n = s.size - 1
        val cache = Array(n) { IntArray(n) }
        for (x in cache) Arrays.fill(x, INF)
        val p = Array(n) { IntArray(n) }
        return rec(0, n - 1, s, p, cache)
    }

    val INF: Int = Integer.MAX_VALUE / 3
    fun rec(i: Int, j: Int, s: IntArray, p: Array<IntArray>, cache: Array<IntArray>): Int {
        if (i == j) return 0
        var res = cache[i][j]
        if (res != INF) return res
        for (k in i until j) {
            val v = rec(i, k, s, p, cache) + rec(k + 1, j, s, p, cache) + s[i] * s[k + 1] * s[j + 1]
            if (res > v) {
                res = v
                p[i][j] = k
            }
        }
        return res.also { cache[i][j] = it }
    }

    // test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999) {
            val n: Int = rnd.nextInt(6) + 2
            val s: IntArray = rnd.ints(n, 1, 11).toArray()
            val res1 = solveIterative(s)
            val res2 = solveRecursive(s)
            if (res1 != res2) {
                System.out.println("$res1 $res2")
                System.out.println(Arrays.toString(s))
            }
        }
    }
}
