package graphs.spanningtree

import numeric.FFT
import optimization.Simplex

// https://en.wikipedia.org/wiki/Prim%27s_algorithm in O(E*log(V))
object PrimHeap {
    fun mst(edges: Array<List<Edge>?>, pred: IntArray): Long {
        val n = edges.size
        Arrays.fill(pred, -1)
        val used = BooleanArray(n)
        val prio = IntArray(n)
        Arrays.fill(prio, Integer.MAX_VALUE)
        prio[0] = 0
        val q: PriorityQueue<Long> = PriorityQueue()
        q.add(0L)
        var res: Long = 0
        while (!q.isEmpty()) {
            val cur: Long = q.poll()
            val u = cur.toInt()
            if (used[u]) continue
            used[u] = true
            res += cur ushr 32
            for (e in edges[u]!!) {
                val v = e.t
                if (!used[v] && prio[v] > e.cost) {
                    prio[v] = e.cost
                    pred[v] = u
                    q.add((prio[v].toLong() shl 32) + v)
                }
            }
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val cost = arrayOf(intArrayOf(0, 1, 2), intArrayOf(1, 0, 3), intArrayOf(2, 3, 0))
        val n = cost.size
        val edges: Array<List<Edge>?> = arrayOfNulls(n)
        for (i in 0 until n) {
            edges[i] = ArrayList()
            for (j in 0 until n) {
                if (cost[i][j] != 0) {
                    edges[i].add(Edge(j, cost[i][j]))
                }
            }
        }
        val pred = IntArray(n)
        System.out.println(mst(edges, pred))
    }

    class Edge(var t: Int, var cost: Int)
}
