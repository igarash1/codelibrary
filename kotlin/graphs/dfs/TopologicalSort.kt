package graphs.dfs

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Topological_sorting
object TopologicalSort {
    fun dfs(graph: Array<List<Integer>>, used: BooleanArray, order: List<Integer?>, u: Int) {
        used[u] = true
        for (v in graph[u]) if (!used[v]) dfs(graph, used, order, v)
        order.add(u)
    }

    fun topologicalSort(graph: Array<List<Integer>>): List<Integer> {
        val n = graph.size
        val used = BooleanArray(n)
        val order: List<Integer> = ArrayList()
        for (i in 0 until n) if (!used[i]) dfs(graph, used, order, i)
        Collections.reverse(order)
        return order
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val g: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(3).toArray { _Dummy_.__Array__() }
        g[2].add(0)
        g[2].add(1)
        g[0].add(1)
        val order: List<Integer> = topologicalSort(g)
        System.out.println(order)
    }
}
