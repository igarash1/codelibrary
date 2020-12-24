package backtracking

import java.util.Random

class Coloring {
    var minColors = 0
    var bestColoring: IntArray
    fun minColors(graph: Array<BooleanArray>): Int {
        val n = graph.size
        bestColoring = IntArray(n)
        val id = IntArray(n + 1)
        val deg = IntArray(n + 1)
        for (i in 0..n) id[i] = i
        bestColoring = IntArray(n)
        var res = 1
        var from = 0
        var to = 1
        while (to <= n) {
            var best = to
            for (i in to until n) {
                if (graph[id[to - 1]][id[i]]) ++deg[id[i]]
                if (deg[id[best]] < deg[id[i]]) best = i
            }
            val t = id[to]
            id[to] = id[best]
            id[best] = t
            if (deg[id[to]] == 0) {
                minColors = n + 1
                dfs(graph, id, IntArray(n), from, to, from, 0)
                from = to
                res = Math.max(res, minColors)
            }
            to++
        }
        return res
    }

    fun dfs(
        graph: Array<BooleanArray>,
        id: IntArray,
        coloring: IntArray,
        from: Int,
        to: Int,
        cur: Int,
        usedColors: Int
    ) {
        if (usedColors >= minColors) return
        if (cur == to) {
            for (i in from until to) bestColoring[id[i]] = coloring[i]
            minColors = usedColors
            return
        }
        val used = BooleanArray(usedColors + 1)
        for (i in 0 until cur) if (graph[id[cur]][id[i]]) used[coloring[i]] = true
        for (i in 0..usedColors) {
            if (!used[i]) {
                val tmp = coloring[cur]
                coloring[cur] = i
                dfs(graph, id, coloring, from, to, cur + 1, Math.max(usedColors, i + 1))
                coloring[cur] = tmp
            }
        }
    }

    companion object {
        // random test
        fun main(args: Array<String?>?) {
            val rnd = Random(1)
            for (step in 0..999) {
                val n: Int = rnd.nextInt(10) + 1
                val g = Array(n) { BooleanArray(n) }
                for (i in 0 until n) for (j in 0 until i) if (rnd.nextBoolean()) {
                    g[i][j] = true
                    g[j][i] = true
                }
                val res1 = Coloring().minColors(g)
                val res2 = colorSlow(g)
                if (res1 != res2) throw RuntimeException()
            }
        }

        fun colorSlow(g: Array<BooleanArray>): Int {
            val n = g.size
            var allowedColors = 1
            while (true) {
                var colors: Long = 1
                for (i in 0 until n) colors *= allowedColors.toLong()
                m1@ for (c in 0 until colors) {
                    val col = IntArray(n)
                    var cur: Long = c
                    for (i in 0 until n) {
                        col[i] = (cur % allowedColors).toInt()
                        cur /= allowedColors.toLong()
                    }
                    for (i in 0 until n) for (j in 0 until i) if (g[i][j] && col[i] == col[j]) continue@m1
                    return allowedColors
                }
                allowedColors++
            }
        }
    }
}
