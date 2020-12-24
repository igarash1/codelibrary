package graphs.matchings

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Hopcroft–Karp_algorithm
// time complexity: O(E * sqrt(V))
object MaxBipartiteMatchingHopcroftKarpEsqrtV {
    fun maxMatching(graph: Array<List<Integer>>): Int {
        val n1 = graph.size
        val n2: Int = Arrays.stream(graph).flatMap(Collection::stream).mapToInt(Integer::intValue).max().orElse(-1) + 1
        val dist = IntArray(n1)
        val matching = IntArray(n2)
        Arrays.fill(matching, -1)
        val used = BooleanArray(n1)
        var res = 0
        while (true) {
            bfs(graph, used, matching, dist)
            val vis = BooleanArray(n1)
            var f = 0
            for (u in 0 until n1) if (!used[u] && dfs(graph, vis, used, matching, dist, u)) ++f
            if (f == 0) return res
            res += f
        }
    }

    fun bfs(graph: Array<List<Integer>>, used: BooleanArray, matching: IntArray, dist: IntArray) {
        Arrays.fill(dist, -1)
        val n1 = graph.size
        val Q = IntArray(n1)
        var sizeQ = 0
        for (u in 0 until n1) {
            if (!used[u]) {
                Q[sizeQ++] = u
                dist[u] = 0
            }
        }
        for (i in 0 until sizeQ) {
            val u1 = Q[i]
            for (v in graph[u1]) {
                val u2 = matching[v]
                if (u2 >= 0 && dist[u2] < 0) {
                    dist[u2] = dist[u1] + 1
                    Q[sizeQ++] = u2
                }
            }
        }
    }

    fun dfs(
        graph: Array<List<Integer>>,
        vis: BooleanArray,
        used: BooleanArray,
        matching: IntArray,
        dist: IntArray,
        u1: Int
    ): Boolean {
        vis[u1] = true
        for (v in graph[u1]) {
            val u2 = matching[v]
            if (u2 < 0 || !vis[u2] && dist[u2] == dist[u1] + 1 && dfs(graph, vis, used, matching, dist, u2)) {
                matching[v] = u1
                used[u1] = true
                return true
            }
        }
        return false
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val graph: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(2).toArray { _Dummy_.__Array__() }
        graph[0].add(0)
        graph[0].add(1)
        graph[1].add(1)
        System.out.println(2 == maxMatching(graph))
    }
}
