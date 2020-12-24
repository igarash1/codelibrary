package misc

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/2-satisfiability
object Sat2 {
    fun dfs1(graph: Array<List<Integer>>, used: BooleanArray, order: List<Integer?>, u: Int) {
        used[u] = true
        for (v in graph[u]) if (!used[v]) dfs1(graph, used, order, v)
        order.add(u)
    }

    fun dfs2(reverseGraph: Array<List<Integer>>, comp: IntArray, u: Int, color: Int) {
        comp[u] = color
        for (v in reverseGraph[u]) if (comp[v] == -1) dfs2(reverseGraph, comp, v, color)
    }

    fun solve2Sat(graph: Array<List<Integer>>): BooleanArray? {
        val n = graph.size
        val used = BooleanArray(n)
        val order: List<Integer> = ArrayList()
        for (i in 0 until n) if (!used[i]) dfs1(graph, used, order, i)
        val reverseGraph: Array<List<Integer>> =
            Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        for (i in 0 until n) for (j in graph[i]) reverseGraph[j].add(i)
        val comp = IntArray(n)
        Arrays.fill(comp, -1)
        run {
            var i = 0
            var color = 0
            while (i < n) {
                val u: Int = order[n - i - 1]
                if (comp[u] == -1) dfs2(reverseGraph, comp, u, color++)
                ++i
            }
        }
        for (i in 0 until n) if (comp[i] == comp[i xor 1]) return null
        val res = BooleanArray(n / 2)
        var i = 0
        while (i < n) {
            res[i / 2] = comp[i] > comp[i xor 1]
            i += 2
        }
        return res
    }

    fun main(args: Array<String?>?) {
        val n = 6
        val g: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        // (a || b) && (b || !c)
        // !a => b
        // !b => a
        // !b => !c
        // c => b
        val a = 0
        val na = 1
        val b = 2
        val nb = 3
        val c = 4
        val nc = 5
        g[na].add(b)
        g[nb].add(a)
        g[nb].add(nc)
        g[c].add(b)
        val solution = solve2Sat(g)
        System.out.println(Arrays.toString(solution))
    }
}
