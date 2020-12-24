package graphs.spanningtree

import java.util.Arrays

// https://en.wikipedia.org/wiki/Prim%27s_algorithm in O(V^2)
object Prim {
    fun mstPrim(d: Array<IntArray>): Long {
        val n = d.size
        val prev = IntArray(n)
        val dist = IntArray(n)
        Arrays.fill(dist, Integer.MAX_VALUE)
        dist[0] = 0
        val visited = BooleanArray(n)
        var res: Long = 0
        for (i in 0 until n) {
            var u = -1
            for (j in 0 until n) {
                if (!visited[j] && (u == -1 || dist[u] > dist[j])) u = j
            }
            res += dist[u]
            visited[u] = true
            for (j in 0 until n) {
                if (!visited[j] && dist[j] > d[u][j]) {
                    dist[j] = d[u][j]
                    prev[j] = u
                }
            }
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {}
}
