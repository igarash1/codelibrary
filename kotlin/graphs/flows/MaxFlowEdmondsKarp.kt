package graphs.flows

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Edmonds%E2%80%93Karp_algorithm in O(V * E^2)
class MaxFlowEdmondsKarp(nodes: Int) {
    var graph: Array<List<Edge>>

    inner class Edge(var s: Int, var t: Int, var rev: Int, var cap: Int) {
        var f = 0
    }

    fun addBidiEdge(s: Int, t: Int, cap: Int) {
        graph[s].add(Edge(s, t, graph[t].size(), cap))
        graph[t].add(Edge(t, s, graph[s].size() - 1, 0))
    }

    fun maxFlow(s: Int, t: Int): Int {
        var flow = 0
        val q = IntArray(graph.size)
        while (true) {
            var qt = 0
            q[qt++] = s
            val pred = arrayOfNulls<Edge>(graph.size)
            var qh = 0
            while (qh < qt && pred[t] == null) {
                val cur = q[qh]
                for (e in graph[cur]) {
                    if (pred[e.t] == null && e.cap > e.f) {
                        pred[e.t] = e
                        q[qt++] = e.t
                    }
                }
                qh++
            }
            if (pred[t] == null) break
            var df: Int = Integer.MAX_VALUE
            run {
                var u = t
                while (u != s) {
                    df = Math.min(df, pred[u]!!.cap - pred[u]!!.f)
                    u = pred[u]!!.s
                }
            }
            var u = t
            while (u != s) {
                pred[u]!!.f += df
                graph[pred[u]!!.t][pred[u]!!.rev].f -= df
                u = pred[u]!!.s
            }
            flow += df
        }
        return flow
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val flow = MaxFlowEdmondsKarp(3)
            flow.addBidiEdge(0, 1, 3)
            flow.addBidiEdge(0, 2, 2)
            flow.addBidiEdge(1, 2, 2)
            System.out.println(4 == flow.maxFlow(0, 2))
        }
    }

    init {
        graph = Stream.generate { ArrayList() }.limit(nodes).toArray { _Dummy_.__Array__() }
    }
}
