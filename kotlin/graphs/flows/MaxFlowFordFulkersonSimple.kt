package graphs.flows

// https://en.wikipedia.org/wiki/Ford–Fulkerson_algorithm in O(V^2 * flow)
object MaxFlowFordFulkersonSimple {
    fun maxFlow(cap: Array<IntArray>, s: Int, t: Int): Int {
        var flow = 0
        while (true) {
            if (!augmentPath(cap, BooleanArray(cap.size), s, t)) return flow
            ++flow
        }
    }

    fun augmentPath(cap: Array<IntArray>, vis: BooleanArray, i: Int, t: Int): Boolean {
        if (i == t) return true
        vis[i] = true
        for (j in vis.indices) if (!vis[j] && cap[i][j] > 0 && augmentPath(cap, vis, j, t)) {
            --cap[i][j]
            ++cap[j][i]
            return true
        }
        return false
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val capacity =
            arrayOf(intArrayOf(0, 1, 1, 0), intArrayOf(1, 0, 1, 1), intArrayOf(1, 1, 0, 1), intArrayOf(0, 1, 1, 0))
        System.out.println(2 == maxFlow(capacity, 0, 3))
    }
}
