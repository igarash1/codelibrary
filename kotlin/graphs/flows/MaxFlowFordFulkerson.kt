package graphs.flows

// https://en.wikipedia.org/wiki/Ford–Fulkerson_algorithm in O(V^2 * flow)
object MaxFlowFordFulkerson {
    fun maxFlow(cap: Array<IntArray>, s: Int, t: Int): Int {
        var flow = 0
        while (true) {
            val df = findPath(cap, BooleanArray(cap.size), s, t, Integer.MAX_VALUE)
            if (df == 0) return flow
            flow += df
        }
    }

    fun findPath(cap: Array<IntArray>, vis: BooleanArray, u: Int, t: Int, f: Int): Int {
        if (u == t) return f
        vis[u] = true
        for (v in vis.indices) if (!vis[v] && cap[u][v] > 0) {
            val df = findPath(cap, vis, v, t, Math.min(f, cap[u][v]))
            if (df > 0) {
                cap[u][v] -= df
                cap[v][u] += df
                return df
            }
        }
        return 0
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val capacity = arrayOf(intArrayOf(0, 3, 2), intArrayOf(0, 0, 2), intArrayOf(0, 0, 0))
        System.out.println(4 == maxFlow(capacity, 0, 2))
    }
}
