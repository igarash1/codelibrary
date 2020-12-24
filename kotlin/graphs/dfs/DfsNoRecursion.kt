package graphs.dfs

import java.util.stream.Stream

object DfsNoRecursion {
    fun dfs(graph: Array<List<Integer>>, root: Int) {
        val n = graph.size
        val curEdge = IntArray(n)
        val stack = IntArray(n)
        stack[0] = root
        var top = 0
        while (top >= 0) {
            val u = stack[top]
            if (curEdge[u] == 0) {
                System.out.println(u)
            }
            if (curEdge[u] < graph[u].size()) {
                val v: Int = graph[u][curEdge[u]++]
                if (curEdge[v] == 0) {
                    stack[++top] = v
                }
            } else {
                --top
            }
        }
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val g: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(3).toArray { _Dummy_.__Array__() }
        g[0].add(1)
        g[1].add(0)
        g[0].add(2)
        dfs(g, 0)
        System.out.println()
        dfs(g, 1)
        System.out.println()
        dfs(g, 2)
    }
}
