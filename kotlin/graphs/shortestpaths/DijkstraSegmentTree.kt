package graphs.shortestpaths

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm
object DijkstraSegmentTree {
    // calculate shortest paths in O(E*log(V)) time and O(V) memory
    fun shortestPaths(edges: Array<List<Edge>>, s: Int, prio: LongArray, pred: IntArray) {
        Arrays.fill(pred, -1)
        Arrays.fill(prio, Long.MAX_VALUE)
        prio[s] = 0
        val t = LongArray(edges.size * 2)
        Arrays.fill(t, Long.MAX_VALUE)
        DijkstraSegmentTree[t, s] = 0
        while (true) {
            val cur = minIndex(t)
            if (t[cur + t.size / 2] == Long.MAX_VALUE) break
            DijkstraSegmentTree[t, cur] = Long.MAX_VALUE
            for (e in edges[cur]) {
                val v = e.t
                val nprio = prio[cur] + e.cost
                if (prio[v] > nprio) {
                    prio[v] = nprio
                    pred[v] = cur
                    DijkstraSegmentTree[t, v] = nprio
                }
            }
        }
    }

    operator fun set(t: LongArray, i: Int, value: Long) {
        var i = i
        i += t.size / 2
        if (t[i] < value && value != Long.MAX_VALUE) return
        t[i] = value
        while (i > 1) {
            t[i shr 1] = Math.min(t[i], t[i xor 1])
            i = i shr 1
        }
    }

    fun minIndex(t: LongArray): Int {
        var res = 1
        while (res < t.size / 2) res = res * 2 + if (t[res * 2] > t[1]) 1 else 0
        return res - t.size / 2
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val cost = arrayOf(intArrayOf(0, 3, 2), intArrayOf(0, 0, -2), intArrayOf(0, 0, 0))
        val n = cost.size
        val edges: Array<List<Edge>> = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (cost[i][j] != 0) {
                    edges[i].add(Edge(j, cost[i][j]))
                }
            }
        }
        val dist = LongArray(n)
        val pred = IntArray(n)
        shortestPaths(edges, 0, dist, pred)
        System.out.println(0 == dist[0])
        System.out.println(3 == dist[1])
        System.out.println(1 == dist[2])
        System.out.println(-1 == pred[0])
        System.out.println(0 == pred[1])
        System.out.println(1 == pred[2])
    }

    class Edge(var t: Int, var cost: Int)
}
