package graphs.shortestpaths

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm
object DijkstraSlow {
    // calculate shortest paths in O(V^2)
    fun shortestPaths(graph: Array<List<Edge>>, s: Int, prio: IntArray, pred: IntArray) {
        val n = graph.size
        Arrays.fill(pred, -1)
        Arrays.fill(prio, Integer.MAX_VALUE)
        prio[s] = 0
        val visited = BooleanArray(n)
        for (i in 0 until n) {
            var u = -1
            for (j in 0 until n) {
                if (!visited[j] && (u == -1 || prio[u] > prio[j])) u = j
            }
            if (prio[u] == Integer.MAX_VALUE) break
            visited[u] = true
            for (e in graph[u]) {
                val v = e.t
                val nprio = prio[u] + e.cost
                if (prio[v] > nprio) {
                    prio[v] = nprio
                    pred[v] = u
                }
            }
        }
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val cost = arrayOf(intArrayOf(0, 3, 2), intArrayOf(0, 0, -2), intArrayOf(0, 0, 0))
        val n = cost.size
        val graph: Array<List<Edge>> = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (cost[i][j] != 0) {
                    graph[i].add(Edge(j, cost[i][j]))
                }
            }
        }
        val dist = IntArray(n)
        val pred = IntArray(n)
        shortestPaths(graph, 0, dist, pred)
        System.out.println(0 == dist[0])
        System.out.println(3 == dist[1])
        System.out.println(1 == dist[2])
        System.out.println(-1 == pred[0])
        System.out.println(0 == pred[1])
        System.out.println(1 == pred[2])
    }

    class Edge(var t: Int, var cost: Int)
}
