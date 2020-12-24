package graphs.shortestpaths

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm
object DijkstraHeap {
    // calculate shortest paths in O(E*log(V)) time and O(E) memory
    fun shortestPaths(edges: Array<List<Edge>>, s: Int, prio: IntArray, pred: IntArray) {
        Arrays.fill(pred, -1)
        Arrays.fill(prio, Integer.MAX_VALUE)
        prio[s] = 0
        val q: PriorityQueue<Long> = PriorityQueue()
        q.add(s.toLong())
        while (!q.isEmpty()) {
            val cur: Long = q.remove()
            val curu = cur.toInt()
            if (cur ushr 32 != prio[curu]) continue
            for (e in edges[curu]) {
                val v = e.t
                val nprio = prio[curu] + e.cost
                if (prio[v] > nprio) {
                    prio[v] = nprio
                    pred[v] = curu
                    q.add((nprio.toLong() shl 32) + v)
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
