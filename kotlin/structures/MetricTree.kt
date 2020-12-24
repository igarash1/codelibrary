package structures

import java.util.Random

// https://en.wikipedia.org/wiki/Metric_tree
class MetricTree(var x: IntArray, var y: IntArray) {
    fun build(low: Int, high: Int) {
        if (high - low <= 2) return
        swap(low + rnd.nextInt(high - low), low)
        val mid = low + 1 + high ushr 1
        nth_element(low + 1, high, mid)
        build(low + 1, mid)
        build(mid + 1, high)
    }

    // See http://www.cplusplus.com/reference/algorithm/nth_element
    fun nth_element(low: Int, high: Int, n: Int) {
        var low = low
        var high = high
        val center = low - 1
        while (true) {
            val k = partition(center, low, high, low + rnd.nextInt(high - low))
            if (n < k) high = k else if (n > k) low = k + 1 else return
        }
    }

    fun partition(center: Int, fromInclusive: Int, toExclusive: Int, separatorIndex: Int): Int {
        var i = fromInclusive
        var j = toExclusive - 1
        if (i >= j) return j
        val separator = dist2(x[center], y[center], x[separatorIndex], y[separatorIndex])
        swap(i++, separatorIndex)
        while (i <= j) {
            while (i <= j && dist2(x[center], y[center], x[i], y[i]) < separator) ++i
            while (i <= j && dist2(x[center], y[center], x[j], y[j]) > separator) --j
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
        findNearestNeighbour(0, x.size, px, py)
        return bestNode
    }

    fun findNearestNeighbour(low: Int, high: Int, px: Int, py: Int) {
        if (high - low <= 0) return
        val d2 = dist2(px, py, x[low], y[low])
        if (bestDist > d2) {
            bestDist = d2
            bestNode = low
        }
        if (high - low <= 1) return
        val mid = low + 1 + high ushr 1
        val dist2 = dist2(px, py, x[mid], y[mid])
        if (bestDist > dist2) {
            bestDist = dist2
            bestNode = mid
        }
        val R: Double = Math.sqrt(dist2(x[low], y[low], x[mid], y[mid]))
        val r: Double = Math.sqrt(bestDist)
        val d: Double = Math.sqrt(d2)
        if (d <= R + r) {
            findNearestNeighbour(low + 1, mid, px, py)
        }
        if (d >= R - r) {
            findNearestNeighbour(mid + 1, high, px, py)
        }
    }

    companion object {
        val rnd: Random = Random(1)
        fun dist2(x1: Int, y1: Int, x2: Int, y2: Int): Long {
            val dx = x1 - x2
            val dy = y1 - y2
            return dx.toLong() * dx + dy.toLong() * dy
        }

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
                    minDist = Math.min(minDist, dist2(qx, qy, x[i], y[i]))
                }
                val metricTree = MetricTree(x, y)
                val index = metricTree.findNearestNeighbour(qx, qy)
                if (minDist != metricTree.bestDist || dist2(
                        qx,
                        qy,
                        x[index],
                        y[index]
                    ) != minDist
                ) throw RuntimeException()
            }
        }
    }

    init {
        build(0, x.size)
    }
}
