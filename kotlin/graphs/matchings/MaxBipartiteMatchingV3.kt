package graphs.matchings

import java.util.Arrays

// https://en.wikipedia.org/wiki/Matching_(graph_theory)#In_unweighted_bipartite_graphs in O(V^3)
object MaxBipartiteMatchingV3 {
    fun maxMatching(graph: Array<BooleanArray>): Int {
        val n1 = graph.size
        val n2 = if (n1 == 0) 0 else graph[0].length
        val matching = IntArray(n2)
        Arrays.fill(matching, -1)
        var matches = 0
        for (u in 0 until n1) if (findPath(graph, u, matching, BooleanArray(n1))) ++matches
        return matches
    }

    fun findPath(graph: Array<BooleanArray>, u1: Int, matching: IntArray, vis: BooleanArray): Boolean {
        vis[u1] = true
        for (v in matching.indices) {
            val u2 = matching[v]
            if (graph[u1][v] && (u2 == -1 || !vis[u2] && findPath(graph, u2, matching, vis))) {
                matching[v] = u1
                return true
            }
        }
        return false
    }

    // Usage example
    fun main(args: Array<String?>?) {
        System.out.println(2 == maxMatching(arrayOf(booleanArrayOf(true, true), booleanArrayOf(false, true))))
    }
}
