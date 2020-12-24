package structures

// https://en.wikipedia.org/wiki/R-tree
class RTree(segments: Array<Segment?>) {
    class Segment(val x1: Int, val y1: Int, val x2: Int, val y2: Int)

    val x1: IntArray
    val y1: IntArray
    val x2: IntArray
    val y2: IntArray
    val minx: IntArray
    val maxx: IntArray
    val miny: IntArray
    val maxy: IntArray
    fun build(low: Int, high: Int, divX: Boolean, segments: Array<Segment?>) {
        if (low >= high) return
        val mid = low + high ushr 1
        nth_element(segments, low, high, mid, divX)
        x1[mid] = segments[mid]!!.x1
        y1[mid] = segments[mid]!!.y1
        x2[mid] = segments[mid]!!.x2
        y2[mid] = segments[mid]!!.y2
        for (i in low until high) {
            minx[mid] = Math.min(minx[mid], Math.min(segments[i]!!.x1, segments[i]!!.x2))
            miny[mid] = Math.min(miny[mid], Math.min(segments[i]!!.y1, segments[i]!!.y2))
            maxx[mid] = Math.max(maxx[mid], Math.max(segments[i]!!.x1, segments[i]!!.x2))
            maxy[mid] = Math.max(maxy[mid], Math.max(segments[i]!!.y1, segments[i]!!.y2))
        }
        build(low, mid, !divX, segments)
        build(mid + 1, high, !divX, segments)
    }

    var bestDist = 0.0
    var bestNode = 0
    fun findNearestNeighbour(x: Int, y: Int): Int {
        bestDist = Double.POSITIVE_INFINITY
        findNearestNeighbour(0, x1.size, x, y, true)
        return bestNode
    }

    fun findNearestNeighbour(low: Int, high: Int, x: Int, y: Int, divX: Boolean) {
        if (low >= high) return
        val mid = low + high ushr 1
        val distance = pointToSegmentSquaredDistance(x, y, x1[mid], y1[mid], x2[mid], y2[mid])
        if (bestDist > distance) {
            bestDist = distance
            bestNode = mid
        }
        val delta = if (divX) (2 * x - x1[mid] - x2[mid]).toLong() else 2 * y - y1[mid] - y2[mid].toLong()
        if (delta <= 0) {
            findNearestNeighbour(low, mid, x, y, !divX)
            if (mid + 1 < high) {
                val mid1 = mid + 1 + high ushr 1
                val dist = if (divX) getDist(x, minx[mid1], maxx[mid1])
                    .toLong() else getDist(y, miny[mid1], maxy[mid1]).toLong()
                if (dist * dist < bestDist) findNearestNeighbour(mid + 1, high, x, y, !divX)
            }
        } else {
            findNearestNeighbour(mid + 1, high, x, y, !divX)
            if (low < mid) {
                val mid1 = low + mid ushr 1
                val dist = if (divX) getDist(x, minx[mid1], maxx[mid1])
                    .toLong() else getDist(y, miny[mid1], maxy[mid1]).toLong()
                if (dist * dist < bestDist) findNearestNeighbour(low, mid, x, y, !divX)
            }
        }
    }

    companion object {
        val rnd: Random = Random(1)

        // See: http://www.cplusplus.com/reference/algorithm/nth_element
        fun nth_element(a: Array<Segment?>, low: Int, high: Int, n: Int, divX: Boolean) {
            var low = low
            var high = high
            while (true) {
                val k = partition(a, low, high, low + rnd.nextInt(high - low), divX)
                if (n < k) high = k else if (n > k) low = k + 1 else return
            }
        }

        fun partition(
            a: Array<Segment?>,
            fromInclusive: Int,
            toExclusive: Int,
            separatorIndex: Int,
            divX: Boolean
        ): Int {
            var i = fromInclusive
            var j = toExclusive - 1
            if (i >= j) return j
            val separator =
                if (divX) a[separatorIndex]!!.x1 + a[separatorIndex]!!.x2 else a[separatorIndex]!!.y1 + a[separatorIndex]!!.y2
            swap(a, i++, separatorIndex)
            while (i <= j) {
                while (i <= j && (if (divX) a[i]!!.x1 + a[i]!!.x2 else a[i]!!.y1 + a[i]!!.y2) < separator) ++i
                while (i <= j && (if (divX) a[j]!!.x1 + a[j]!!.x2 else a[j]!!.y1 + a[j]!!.y2) > separator) --j
                if (i >= j) break
                swap(a, i++, j--)
            }
            swap(a, j, fromInclusive)
            return j
        }

        fun swap(a: Array<Segment?>, i: Int, j: Int) {
            val t = a[i]
            a[i] = a[j]
            a[j] = t
        }

        fun getDist(v: Int, min: Int, max: Int): Int {
            if (v <= min) return min - v
            return if (v >= max) v - max else 0
        }

        fun pointToSegmentSquaredDistance(x: Int, y: Int, x1: Int, y1: Int, x2: Int, y2: Int): Double {
            val dx = (x2 - x1).toLong()
            val dy = (y2 - y1).toLong()
            val px = (x - x1).toLong()
            val py = (y - y1).toLong()
            val squaredLength = dx * dx + dy * dy
            val dotProduct = dx * px + dy * py
            if (dotProduct <= 0 || squaredLength == 0L) return (px * px + py * py).toDouble()
            if (dotProduct >= squaredLength) return ((px - dx) * (px - dx) + (py - dy) * (py - dy)).toDouble()
            val q = dotProduct.toDouble() / squaredLength
            return (px - q * dx) * (px - q * dx) + (py - q * dy) * (py - q * dy)
        }

        // random test
        fun main(args: Array<String?>?) {
            for (step in 0..99999) {
                val qx: Int = rnd.nextInt(1000) - 500
                val qy: Int = rnd.nextInt(1000) - 500
                val n: Int = rnd.nextInt(100) + 1
                val segments = arrayOfNulls<Segment>(n)
                var minDist = Double.POSITIVE_INFINITY
                for (i in 0 until n) {
                    val x1: Int = rnd.nextInt(1000) - 500
                    val y1: Int = rnd.nextInt(1000) - 500
                    val x2: Int = x1 + rnd.nextInt(10)
                    val y2: Int = y1 + rnd.nextInt(10)
                    segments[i] = Segment(x1, y1, x2, y2)
                    minDist = Math.min(minDist, pointToSegmentSquaredDistance(qx, qy, x1, y1, x2, y2))
                }
                val rTree = RTree(segments)
                val index = rTree.findNearestNeighbour(qx, qy)
                val s = segments[index]
                if (minDist != rTree.bestDist
                    || Math.abs(pointToSegmentSquaredDistance(qx, qy, s!!.x1, s.y1, s.x2, s.y2) - minDist) >= 1e-9
                ) throw RuntimeException()
            }
        }
    }

    init {
        val n = segments.size
        x1 = IntArray(n)
        y1 = IntArray(n)
        x2 = IntArray(n)
        y2 = IntArray(n)
        minx = IntArray(n)
        maxx = IntArray(n)
        miny = IntArray(n)
        maxy = IntArray(n)
        Arrays.fill(minx, Integer.MAX_VALUE)
        Arrays.fill(maxx, Integer.MIN_VALUE)
        Arrays.fill(miny, Integer.MAX_VALUE)
        Arrays.fill(maxy, Integer.MIN_VALUE)
        build(0, n, true, segments)
    }
}
