package graphs.shortestpaths

import java.util.stream.Stream

object BellmanFord {
    val INF: Int = Integer.MAX_VALUE / 2
    fun bellmanFord(graph: Array<List<Edge>>, s: Int, dist: IntArray, pred: IntArray): Boolean {
        Arrays.fill(pred, -1)
        Arrays.fill(dist, INF)
        dist[s] = 0
        val n = graph.size
        for (step in 0 until n) {
            var updated = false
            for (u in 0 until n) {
                if (dist[u] == INF) continue
                for (e in graph[u]) {
                    if (dist[e.v] > dist[u] + e.cost) {
                        dist[e.v] = dist[u] + e.cost
                        dist[e.v] = Math.max(dist[e.v], -INF)
                        pred[e.v] = u
                        updated = true
                    }
                }
            }
            if (!updated) return true
        }
        // a negative cycle exists
        return false
    }

    fun findNegativeCycle(graph: Array<List<Edge>>): IntArray? {
        val n = graph.size
        val pred = IntArray(n)
        Arrays.fill(pred, -1)
        val dist = IntArray(n)
        var last = -1
        for (step in 0 until n) {
            last = -1
            for (u in 0 until n) {
                if (dist[u] == INF) continue
                for (e in graph[u]) {
                    if (dist[e.v] > dist[u] + e.cost) {
                        dist[e.v] = dist[u] + e.cost
                        dist[e.v] = Math.max(dist[e.v], -INF)
                        pred[e.v] = u
                        last = e.v
                    }
                }
            }
            if (last == -1) return null
        }
        for (i in 0 until n) {
            last = pred[last]
        }
        val p = IntArray(n)
        var cnt = 0
        var u = last
        while (u != last || cnt == 0) {
            p[cnt++] = u
            u = pred[u]
        }
        val cycle = IntArray(cnt)
        for (i in cycle.indices) {
            cycle[i] = p[--cnt]
        }
        return cycle
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val graph: Array<List<Edge>> = Stream.generate { ArrayList() }.limit(4).toArray { _Dummy_.__Array__() }
        graph[0].add(Edge(1, 1))
        graph[1].add(Edge(0, 1))
        graph[1].add(Edge(2, 1))
        graph[2].add(Edge(3, -10))
        graph[3].add(Edge(1, 1))
        val cycle = findNegativeCycle(graph)
        System.out.println(Arrays.toString(cycle))
    }

    class Edge(val v: Int, val cost: Int)
}
