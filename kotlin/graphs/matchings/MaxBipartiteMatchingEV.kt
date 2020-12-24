package graphs.matchings

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Matching_(graph_theory)#In_unweighted_bipartite_graphs in O(V * E)
object MaxBipartiteMatchingEV {
    fun maxMatching(graph: Array<List<Integer>>): Int {
        val n1 = graph.size
        val n2: Int = Arrays.stream(graph).flatMap(Collection::stream).mapToInt(Integer::intValue).max().orElse(-1) + 1
        val matching = IntArray(n2)
        Arrays.fill(matching, -1)
        var matches = 0
        for (u in 0 until n1) {
            if (findPath(graph, u, matching, BooleanArray(n1))) ++matches
        }
        return matches
    }

    fun findPath(graph: Array<List<Integer>>, u1: Int, matching: IntArray, vis: BooleanArray): Boolean {
        vis[u1] = true
        for (v in graph[u1]) {
            val u2 = matching[v]
            if (u2 == -1 || !vis[u2] && findPath(graph, u2, matching, vis)) {
                matching[v] = u1
                return true
            }
        }
        return false
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val g: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(2).toArray { _Dummy_.__Array__() }
        g[0].add(0)
        g[0].add(1)
        g[1].add(1)
        System.out.println(2 == maxMatching(g))
    }
}
