package structures

import java.util.Random

class KdTreeRectQuery(points: Array<Point?>) {
    class Point(var x: Int, var y: Int)

    var tx: IntArray
    var ty: IntArray
    var minx: IntArray
    var miny: IntArray
    var maxx: IntArray
    var maxy: IntArray
    var count: IntArray
    fun build(low: Int, high: Int, divX: Boolean, points: Array<Point?>) {
        if (low >= high) return
        val mid = low + high ushr 1
        nth_element(points, low, high, mid, divX)
        tx[mid] = points[mid]!!.x
        ty[mid] = points[mid]!!.y
        count[mid] = high - low
        minx[mid] = Integer.MAX_VALUE
        miny[mid] = Integer.MAX_VALUE
        maxx[mid] = Integer.MIN_VALUE
        maxy[mid] = Integer.MIN_VALUE
        for (i in low until high) {
            minx[mid] = Math.min(minx[mid], points[i]!!.x)
            miny[mid] = Math.min(miny[mid], points[i]!!.y)
            maxx[mid] = Math.max(maxx[mid], points[i]!!.x)
            maxy[mid] = Math.max(maxy[mid], points[i]!!.y)
        }
        build(low, mid, !divX, points)
        build(mid + 1, high, !divX, points)
    }

    // number of points in [x1,x2] x [y1,y2]
    fun count(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        return count(0, tx.size, x1, y1, x2, y2)
    }

    fun count(low: Int, high: Int, x1: Int, y1: Int, x2: Int, y2: Int): Int {
        if (low >= high) return 0
        val mid = low + high ushr 1
        val ax = minx[mid]
        val ay = miny[mid]
        val bx = maxx[mid]
        val by = maxy[mid]
        if (ax > x2 || x1 > bx || ay > y2 || y1 > by) return 0
        if (x1 <= ax && bx <= x2 && y1 <= ay && by <= y2) return count[mid]
        var res = 0
        res += count(low, mid, x1, y1, x2, y2)
        res += count(mid + 1, high, x1, y1, x2, y2)
        if (x1 <= tx[mid] && tx[mid] <= x2 && y1 <= ty[mid] && ty[mid] <= y2) ++res
        return res
    }

    companion object {
        val rnd: Random = Random(1)

        // See: http://www.cplusplus.com/reference/algorithm/nth_element
        fun nth_element(a: Array<Point?>, low: Int, high: Int, n: Int, divX: Boolean) {
            var low = low
            var high = high
            while (true) {
                val k = partition(a, low, high, low + rnd.nextInt(high - low), divX)
                if (n < k) high = k else if (n > k) low = k + 1 else return
            }
        }

        fun partition(a: Array<Point?>, fromInclusive: Int, toExclusive: Int, separatorIndex: Int, divX: Boolean): Int {
            var i = fromInclusive
            var j = toExclusive - 1
            if (i >= j) return j
            val separator = if (divX) a[separatorIndex]!!.x.toDouble() else a[separatorIndex]!!.y.toDouble()
            swap(a, i++, separatorIndex)
            while (i <= j) {
                while (i <= j && (if (divX) a[i]!!.x else a[i]!!.y) < separator) ++i
                while (i <= j && (if (divX) a[j]!!.x else a[j]!!.y) > separator) --j
                if (i >= j) break
                swap(a, i++, j--)
            }
            swap(a, j, fromInclusive)
            return j
        }

        fun swap(a: Array<Point?>, i: Int, j: Int) {
            val t = a[i]
            a[i] = a[j]
            a[j] = t
        }

        // Usage example
        fun main(args: Array<String?>?) {
            val x = intArrayOf(0, 10, 0, 10)
            val y = intArrayOf(0, 10, 10, 0)
            val points = arrayOfNulls<Point>(x.size)
            for (i in points.indices) points[i] = Point(x[i], y[i])
            val kdTree = KdTreeRectQuery(points)
            val count = kdTree.count(0, 0, 10, 10)
            System.out.println(4 == count)
        }
    }

    init {
        val n = points.size
        tx = IntArray(n)
        ty = IntArray(n)
        minx = IntArray(n)
        miny = IntArray(n)
        maxx = IntArray(n)
        maxy = IntArray(n)
        count = IntArray(n)
        build(0, n, true, points)
    }
}
