package graphs.flows

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Minimum-cost_flow_problem in O(E * V * FLOW)
// negative-cost edges are allowed
// negative-cost cycles are not allowed
class MinCostFlowBF(nodes: Int) {
    var graph: Array<List<Edge>>

    inner class Edge(var to: Int, var rev: Int, var cap: Int, var cost: Int) {
        var f = 0
    }

    fun addEdge(s: Int, t: Int, cap: Int, cost: Int) {
        graph[s].add(Edge(t, graph[t].size(), cap, cost))
        graph[t].add(Edge(s, graph[s].size() - 1, 0, -cost))
    }

    fun bellmanFord(s: Int, dist: IntArray, prevnode: IntArray, prevedge: IntArray, curflow: IntArray) {
        val n = graph.size
        Arrays.fill(dist, 0, n, Integer.MAX_VALUE)
        dist[s] = 0
        curflow[s] = Integer.MAX_VALUE
        val inqueue = BooleanArray(n)
        val q = IntArray(n)
        var qt = 0
        q[qt++] = s
        for (qh in 0 until qt) {
            val u = q[qh % n]
            inqueue[u] = false
            for (i in 0 until graph[u].size()) {
                val e = graph[u][i]
                if (e.f >= e.cap) continue
                val v = e.to
                val ndist = dist[u] + e.cost
                if (dist[v] > ndist) {
                    dist[v] = ndist
                    prevnode[v] = u
                    prevedge[v] = i
                    curflow[v] = Math.min(curflow[u], e.cap - e.f)
                    if (!inqueue[v]) {
                        inqueue[v] = true
                        q[qt++ % n] = v
                    }
                }
            }
        }
    }

    fun minCostFlow(s: Int, t: Int, maxf: Int): IntArray {
        val n = graph.size
        val dist = IntArray(n)
        val curflow = IntArray(n)
        val prevedge = IntArray(n)
        val prevnode = IntArray(n)
        var flow = 0
        var flowCost = 0
        while (flow < maxf) {
            bellmanFord(s, dist, prevnode, prevedge, curflow)
            if (dist[t] == Integer.MAX_VALUE) break
            val df: Int = Math.min(curflow[t], maxf - flow)
            flow += df
            var v = t
            while (v != s) {
                val e = graph[prevnode[v]][prevedge[v]]
                e.f += df
                graph[v][e.rev].f -= df
                flowCost += df * e.cost
                v = prevnode[v]
            }
        }
        return intArrayOf(flow, flowCost)
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val mcf = MinCostFlowBF(3)
            mcf.addEdge(0, 1, 3, 1)
            mcf.addEdge(0, 2, 2, 1)
            mcf.addEdge(1, 2, 2, 1)
            val res = mcf.minCostFlow(0, 2, Integer.MAX_VALUE)
            val flow = res[0]
            val flowCost = res[1]
            System.out.println(4 == flow)
            System.out.println(6 == flowCost)
        }
    }

    init {
        graph = Stream.generate { ArrayList() }.limit(nodes).toArray { _Dummy_.__Array__() }
    }
}
