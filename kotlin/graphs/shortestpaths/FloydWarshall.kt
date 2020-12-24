package graphs.shortestpaths

import java.util.Arrays

object FloydWarshall {
    val INF: Int = Integer.MAX_VALUE / 2

    // precondition: d[i][i] == 0
    fun floydWarshall(d: Array<IntArray>): Array<IntArray>? {
        val n = d.size
        val pred = Array(n) { IntArray(n) }
        for (i in 0 until n) for (j in 0 until n) pred[i][j] = if (i == j || d[i][j] == INF) -1 else i
        for (k in 0 until n) {
            for (i in 0 until n) {
                // if (d[i][k] == INF) continue;
                for (j in 0 until n) {
                    // if (d[k][j] == INF) continue;
                    if (d[i][j] > d[i][k] + d[k][j]) {
                        d[i][j] = d[i][k] + d[k][j]
                        // d[i][j] = Math.max(d[i][j], -INF);
                        pred[i][j] = pred[k][j]
                    }
                }
            }
        }
        for (i in 0 until n) if (d[i][i] < 0) return null
        return pred
    }

    fun restorePath(pred: Array<IntArray>?, i: Int, j: Int): IntArray {
        var j = j
        val n = pred!!.size
        val path = IntArray(n)
        var pos = n
        while (true) {
            path[--pos] = j
            if (i == j) break
            j = pred[i][j]
        }
        return Arrays.copyOfRange(path, pos, n)
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val dist = arrayOf(intArrayOf(0, 3, 2), intArrayOf(0, 0, 1), intArrayOf(INF, 0, 0))
        val pred = floydWarshall(dist)
        val path = restorePath(pred, 0, 1)
        System.out.println(0 == dist[0][0])
        System.out.println(2 == dist[0][1])
        System.out.println(2 == dist[0][2])
        System.out.println(-1 == pred!![0][0])
        System.out.println(2 == pred[0][1])
        System.out.println(0 == pred[0][2])
        System.out.println(Arrays.equals(intArrayOf(0, 2, 1), path))
    }
}
