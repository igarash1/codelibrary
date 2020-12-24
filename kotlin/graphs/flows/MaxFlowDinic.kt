package graphs.flows

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Dinic%27s_algorithm in O(V^2 * E)
class MaxFlowDinic(nodes: Int) {
    var graph: Array<List<Edge>>
    var dist: IntArray

    inner class Edge(var t: Int, var rev: Int, var cap: Int) {
        var f = 0
    }

    fun addBidiEdge(s: Int, t: Int, cap: Int) {
        graph[s].add(Edge(t, graph[t].size(), cap))
        graph[t].add(Edge(s, graph[s].size() - 1, 0))
    }

    fun dinicBfs(src: Int, dest: Int): Boolean {
        Arrays.fill(dist, -1)
        dist[src] = 0
        val q = IntArray(graph.size)
        var sizeQ = 0
        q[sizeQ++] = src
        for (i in 0 until sizeQ) {
            val u = q[i]
            for (e in graph[u]) {
                if (dist[e.t] < 0 && e.f < e.cap) {
                    dist[e.t] = dist[u] + 1
                    q[sizeQ++] = e.t
                }
            }
        }
        return dist[dest] >= 0
    }

    fun dinicDfs(ptr: IntArray, dest: Int, u: Int, f: Int): Int {
        if (u == dest) return f
        while (ptr[u] < graph[u].size()) {
            val e = graph[u][ptr[u]]
            if (dist[e.t] == dist[u] + 1 && e.f < e.cap) {
                val df = dinicDfs(ptr, dest, e.t, Math.min(f, e.cap - e.f))
                if (df > 0) {
                    e.f += df
                    graph[e.t][e.rev].f -= df
                    return df
                }
            }
            ++ptr[u]
        }
        return 0
    }

    fun maxFlow(src: Int, dest: Int): Int {
        var flow = 0
        while (dinicBfs(src, dest)) {
            val ptr = IntArray(graph.size)
            var df: Int
            while (dinicDfs(ptr, dest, src, Integer.MAX_VALUE).also { df = it } != 0) {
                flow += df
            }
        }
        return flow
    }

    // invoke after maxFlow()
    fun minCut(): BooleanArray {
        val cut = BooleanArray(graph.size)
        for (i in cut.indices) cut[i] = dist[i] != -1
        return cut
    }

    fun clearFlow() {
        for (edges in graph) for (edge in edges) edge.f = 0
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val flow = MaxFlowDinic(3)
            flow.addBidiEdge(0, 1, 3)
            flow.addBidiEdge(0, 2, 2)
            flow.addBidiEdge(1, 2, 2)
            System.out.println(4 == flow.maxFlow(0, 2))
        }
    }

    init {
        graph = Stream.generate { ArrayList() }.limit(nodes).toArray { _Dummy_.__Array__() }
        dist = IntArray(nodes)
    }
}
