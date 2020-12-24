package graphs.dfs

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm
object StronglyConnectedComponents {
    fun scc(graph: Array<List<Integer>>): List<List<Integer>> {
        val n = graph.size
        val used = BooleanArray(n)
        val order: List<Integer> = ArrayList()
        for (i in 0 until n) if (!used[i]) dfs(graph, used, order, i)
        val reverseGraph: Array<List<Integer>> =
            Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        for (i in 0 until n) for (j in graph[i]) reverseGraph[j].add(i)
        val components: List<List<Integer>> = ArrayList()
        Arrays.fill(used, false)
        Collections.reverse(order)
        for (u in order) if (!used[u]) {
            val component: List<Integer> = ArrayList()
            dfs(reverseGraph, used, component, u)
            components.add(component)
        }
        return components
    }

    fun dfs(graph: Array<List<Integer>>, used: BooleanArray, res: List<Integer?>, u: Int) {
        used[u] = true
        for (v in graph[u]) if (!used[v]) dfs(graph, used, res, v)
        res.add(u)
    }

    // DAG of strongly connected components
    fun sccGraph(graph: Array<List<Integer>>, components: List<List<Integer>>): Array<List<Integer>> {
        val comp = IntArray(graph.size)
        for (i in 0 until components.size()) for (u in components[i]) comp[u] = i
        val g: Array<List<Integer>> =
            Stream.generate { ArrayList() }.limit(components.size()).toArray { _Dummy_.__Array__() }
        val edges: Set<Long> = HashSet()
        for (u in graph.indices) for (v in graph[u]) if (comp[u] != comp[v] && edges.add(
                (comp[u]
                    .toLong() shl 32) + comp[v]
            )
        ) g[comp[u]].add(comp[v])
        return g
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val g: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(3).toArray { _Dummy_.__Array__() }
        g[2].add(0)
        g[2].add(1)
        g[0].add(1)
        g[1].add(0)
        val components: List<List<Integer>> = scc(g)
        System.out.println(components)
        System.out.println(Arrays.toString(sccGraph(g, components)))
    }
}
