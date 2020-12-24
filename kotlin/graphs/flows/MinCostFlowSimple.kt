package graphs.flows

import java.util.Arrays

object MinCostFlowSimple {
    fun minCostFlow(cap: Array<IntArray>, cost: Array<IntArray>, s: Int, t: Int): IntArray {
        val n = cap.size
        val d = IntArray(n)
        val p = IntArray(n)
        var flow = 0
        var flowCost = 0
        while (true) {
            Arrays.fill(d, Integer.MAX_VALUE)
            d[s] = 0
            for (i in 0 until n - 1) for (j in 0 until n) for (k in 0 until n) if (cap[j][k] > 0 && d[j] < Integer.MAX_VALUE && d[k] > d[j] + cost[j][k]) {
                d[k] = d[j] + cost[j][k]
                p[k] = j
            }
            if (d[t] == Integer.MAX_VALUE) return intArrayOf(flowCost, flow)
            flowCost += d[t]
            var v = t
            while (v != s) {
                --cap[p[v]][v]
                ++cap[v][p[v]]
                v = p[v]
            }
            ++flow
        }
    }

    fun addEdge(cap: Array<IntArray>, cost: Array<IntArray>, u: Int, v: Int, edgeCapacity: Int, edgeCost: Int) {
        cap[u][v] = edgeCapacity
        cost[u][v] = edgeCost
        cost[v][u] = -edgeCost
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val n = 3
        val capacity = Array(n) { IntArray(n) }
        val cost = Array(n) { IntArray(n) }
        addEdge(capacity, cost, 0, 1, 3, 1)
        addEdge(capacity, cost, 0, 2, 2, 1)
        addEdge(capacity, cost, 1, 2, 2, 1)
        val costFlow = minCostFlow(capacity, cost, 0, 2)
        System.out.println(6 == costFlow[0])
        System.out.println(4 == costFlow[1])
    }
}
