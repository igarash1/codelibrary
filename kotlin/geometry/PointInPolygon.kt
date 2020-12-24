package geometry

import numeric.FFT
import optimization.Simplex

object PointInPolygon {
    fun pointInPolygon(qx: Int, qy: Int, x: IntArray, y: IntArray): Int {
        val n = x.size
        var cnt = 0
        var i = 0
        var j = n - 1
        while (i < n) {
            if (y[i] == qy && (x[i] == qx || y[j] == qy && (x[i] <= qx || x[j] <= qx) && (x[i] >= qx || x[j] >= qx))) return 0 // boundary
            if (y[i] > qy != y[j] > qy) {
                val det = (x[i].toLong() - qx) * (y[j].toLong() - qy) - (x[j]
                    .toLong() - qx) * (y[i].toLong() - qy)
                if (det == 0L) return 0 // boundary
                if (det > 0 != y[j] > y[i]) ++cnt
            }
            j = i++
        }
        return if (cnt % 2 == 0) -1 /* exterior */ else 1 /* interior */
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val x = intArrayOf(0, 0, 2, 2)
        val y = intArrayOf(0, 2, 2, 0)
        System.out.println(1 == pointInPolygon(1, 1, x, y))
        System.out.println(0 == pointInPolygon(0, 0, x, y))
        System.out.println(-1 == pointInPolygon(0, 3, x, y))
    }
}
