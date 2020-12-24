package graphs.flows

import java.util.stream.Stream

// https://cp-algorithms.com/graph/min_cost_flow.html in O(E * V + min(E * logV * FLOW, V^2 * FLOW))
// negative-cost edges are allowed
// negative-cost cycles are not allowed
class MinCostFlowDijkstra(nodes: Int) {
    var graph: Array<List<Edge>>

    inner class Edge(var to: Int, var rev: Int, var cap: Int, var cost: Int) {
        var f = 0
    }

    fun addEdge(s: Int, t: Int, cap: Int, cost: Int) {
        graph[s].add(Edge(t, graph[t].size(), cap, cost))
        graph[t].add(Edge(s, graph[s].size() - 1, 0, -cost))
    }

    fun bellmanFord(s: Int, dist: IntArray) {
        val n = graph.size
        Arrays.fill(dist, Integer.MAX_VALUE)
        dist[s] = 0
        val inqueue = BooleanArray(n)
        val q = IntArray(n)
        var qt = 0
        q[qt++] = s
        for (qh in 0 until qt) {
            val u = q[qh % n]
            inqueue[u] = false
            for (i in 0 until graph[u].size()) {
                val e = graph[u][i]
                if (e.cap <= e.f) continue
                val v = e.to
                val ndist = dist[u] + e.cost
                if (dist[v] > ndist) {
                    dist[v] = ndist
                    if (!inqueue[v]) {
                        inqueue[v] = true
                        q[qt++ % n] = v
                    }
                }
            }
        }
    }

    fun dijkstra(
        s: Int,
        t: Int,
        pot: IntArray,
        dist: IntArray,
        finished: BooleanArray,
        curflow: IntArray,
        prevnode: IntArray,
        prevedge: IntArray
    ) {
        val q: PriorityQueue<Long> = PriorityQueue()
        q.add(s.toLong())
        Arrays.fill(dist, Integer.MAX_VALUE)
        dist[s] = 0
        Arrays.fill(finished, false)
        curflow[s] = Integer.MAX_VALUE
        while (!finished[t] && !q.isEmpty()) {
            val cur: Long = q.remove()
            val u = (cur and 0xFFFFFFFFL).toInt()
            val priou = (cur ushr 32).toInt()
            if (priou != dist[u]) continue
            finished[u] = true
            for (i in 0 until graph[u].size()) {
                val e = graph[u][i]
                if (e.f >= e.cap) continue
                val v = e.to
                val nprio = dist[u] + e.cost + pot[u] - pot[v]
                if (dist[v] > nprio) {
                    dist[v] = nprio
                    q.add((nprio.toLong() shl 32) + v)
                    prevnode[v] = u
                    prevedge[v] = i
                    curflow[v] = Math.min(curflow[u], e.cap - e.f)
                }
            }
        }
    }

    fun dijkstra2(
        s: Int,
        t: Int,
        pot: IntArray,
        dist: IntArray,
        finished: BooleanArray,
        curflow: IntArray,
        prevnode: IntArray,
        prevedge: IntArray
    ) {
        Arrays.fill(dist, Integer.MAX_VALUE)
        dist[s] = 0
        val n = graph.size
        Arrays.fill(finished, false)
        curflow[s] = Integer.MAX_VALUE
        var i = 0
        while (i < n && !finished[t]) {
            var u = -1
            for (j in 0 until n) if (!finished[j] && (u == -1 || dist[u] > dist[j])) u = j
            if (dist[u] == Integer.MAX_VALUE) break
            finished[u] = true
            for (k in 0 until graph[u].size()) {
                val e = graph[u][k]
                if (e.f >= e.cap) continue
                val v = e.to
                val nprio = dist[u] + e.cost + pot[u] - pot[v]
                if (dist[v] > nprio) {
                    dist[v] = nprio
                    prevnode[v] = u
                    prevedge[v] = k
                    curflow[v] = Math.min(curflow[u], e.cap - e.f)
                }
            }
            i++
        }
    }

    fun minCostFlow(s: Int, t: Int, maxf: Int): IntArray {
        val n = graph.size
        val pot = IntArray(n)
        val dist = IntArray(n)
        val finished = BooleanArray(n)
        val curflow = IntArray(n)
        val prevedge = IntArray(n)
        val prevnode = IntArray(n)
        bellmanFord(s, pot) // this can be commented out if edges costs are non-negative
        var flow = 0
        var flowCost = 0
        while (flow < maxf) {
            dijkstra(s, t, pot, dist, finished, curflow, prevnode, prevedge) // E*logV
            //            dijkstra2(s, t, pot, dist, finished, curflow, prevnode, prevedge); // V^2
            if (dist[t] == Integer.MAX_VALUE) break
            for (i in 0 until n) if (finished[i]) pot[i] += dist[i] - dist[t]
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
            val mcf = MinCostFlowDijkstra(3)
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
