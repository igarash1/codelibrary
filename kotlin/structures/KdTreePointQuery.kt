package structures

import java.util.Random

class KdTreePointQuery(var x: IntArray, var y: IntArray) {
    fun build(low: Int, high: Int, divX: Boolean) {
        if (high - low <= 1) return
        val mid = low + high ushr 1
        nth_element(low, high, mid, divX)
        build(low, mid, !divX)
        build(mid + 1, high, !divX)
    }

    // See http://www.cplusplus.com/reference/algorithm/nth_element
    fun nth_element(low: Int, high: Int, n: Int, divX: Boolean) {
        var low = low
        var high = high
        while (true) {
            val k = partition(low, high, low + rnd.nextInt(high - low), divX)
            if (n < k) high = k else if (n > k) low = k + 1 else return
        }
    }

    fun partition(fromInclusive: Int, toExclusive: Int, separatorIndex: Int, divX: Boolean): Int {
        var i = fromInclusive
        var j = toExclusive - 1
        if (i >= j) return j
        val separator = if (divX) x[separatorIndex] else y[separatorIndex]
        swap(i++, separatorIndex)
        while (i <= j) {
            while (i <= j && (if (divX) x[i] else y[i]) < separator) ++i
            while (i <= j && (if (divX) x[j] else y[j]) > separator) --j
            if (i >= j) break
            swap(i++, j--)
        }
        swap(j, fromInclusive)
        return j
    }

    fun swap(i: Int, j: Int) {
        var t = x[i]
        x[i] = x[j]
        x[j] = t
        t = y[i]
        y[i] = y[j]
        y[j] = t
    }

    var bestDist: Long = 0
    var bestNode = 0
    fun findNearestNeighbour(px: Int, py: Int): Int {
        bestDist = Long.MAX_VALUE
        findNearestNeighbour(0, x.size, px, py, true)
        return bestNode
    }

    fun findNearestNeighbour(low: Int, high: Int, px: Int, py: Int, divX: Boolean) {
        if (high - low <= 0) return
        val mid = low + high ushr 1
        val dx = (px - x[mid]).toLong()
        val dy = (py - y[mid]).toLong()
        val dist = dx * dx + dy * dy
        if (bestDist > dist) {
            bestDist = dist
            bestNode = mid
        }
        val delta = if (divX) dx else dy
        val delta2 = delta * delta
        if (delta <= 0) {
            findNearestNeighbour(low, mid, px, py, !divX)
            if (delta2 < bestDist) findNearestNeighbour(mid + 1, high, px, py, !divX)
        } else {
            findNearestNeighbour(mid + 1, high, px, py, !divX)
            if (delta2 < bestDist) findNearestNeighbour(low, mid, px, py, !divX)
        }
    }

    companion object {
        val rnd: Random = Random(1)

        // random test
        fun main(args: Array<String?>?) {
            for (step in 0..99999) {
                val qx: Int = rnd.nextInt(100) - 50
                val qy: Int = rnd.nextInt(100) - 50
                val n: Int = rnd.nextInt(100) + 1
                val x = IntArray(n)
                val y = IntArray(n)
                var minDist = Long.MAX_VALUE
                for (i in 0 until n) {
                    x[i] = rnd.nextInt(100) - 50
                    y[i] = rnd.nextInt(100) - 50
                    minDist = Math.min(minDist, (x[i] - qx).toLong() * (x[i] - qx) + (y[i] - qy).toLong() * (y[i] - qy))
                }
                val kdTree = KdTreePointQuery(x, y)
                val index = kdTree.findNearestNeighbour(qx, qy)
                if (minDist != kdTree.bestDist
                    || (x[index] - qx).toLong() * (x[index] - qx) + (y[index] - qy).toLong() * (y[index] - qy) != minDist
                ) throw RuntimeException()
            }
        }
    }

    init {
        build(0, x.size, true)
    }
}
