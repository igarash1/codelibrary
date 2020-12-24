package geometry

import java.util.List

object RectangleUnion {
    fun unionArea(rectangles: Array<Rectangle?>): Long {
        val n = rectangles.size
        val events = LongArray(2 * n)
        val y = IntArray(2 * n)
        for (i in 0 until n) {
            val rect: Rectangle? = rectangles[i]
            val x1: Int = rect.x
            val x2: Int = rect.x + rect.width
            val y1: Int = rect.y
            val y2: Int = rect.y + rect.height
            events[2 * i] = (x1.toLong() shl 32) + i
            events[2 * i + 1] = (x2.toLong() shl 32) + (i.inv() and 0xFFFFFFFFL)
            y[2 * i] = y1
            y[2 * i + 1] = y2
        }
        Arrays.sort(events)
        Arrays.sort(y)
        val t = CoverageTree(y)
        var area: Long = 0
        var lastX = (events[0] ushr 32).toInt()
        for (event in events) {
            var i = event.toInt()
            val `in` = i >= 0
            if (!`in`) i = i.inv()
            val x = (event ushr 32) as Int
            val dx = x - lastX
            val dy = t.len[0]
            area += dx.toLong() * dy
            lastX = x
            val y1: Int = rectangles[i].y
            val y2: Int = rectangles[i].y + rectangles[i].height
            val from: Int = Arrays.binarySearch(y, y1)
            val to: Int = Arrays.binarySearch(y, y2) - 1
            t.update(from, to, if (`in`) 1 else -1)
        }
        return area
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999) {
            val n: Int = rnd.nextInt(100) + 1
            val rectangles: Array<Rectangle?> = arrayOfNulls<Rectangle>(n)
            for (i in 0 until n) {
                val range = 100
                val x: Int = rnd.nextInt(range) - range / 2
                val y: Int = rnd.nextInt(range) - range / 2
                val width: Int = rnd.nextInt(range) + 1
                val height: Int = rnd.nextInt(range) + 1
                rectangles[i] = Rectangle(x, y, width, height)
            }
            val res1 = unionArea(rectangles)
            val area = Area()
            for (rectangle in rectangles) area.add(Area(rectangle))
            val x: List<Double> = ArrayList()
            val y: List<Double> = ArrayList()
            var res2 = 0.0
            val coords = DoubleArray(6)
            val pi: PathIterator = area.getPathIterator(null)
            while (!pi.isDone()) {
                val type: Int = pi.currentSegment(coords)
                if (type == PathIterator.SEG_CLOSE) {
                    var i = 0
                    var j: Int = x.size() - 1
                    while (i < x.size()) {
                        res2 += x[i] * y[j] - x[j] * y[i]
                        j = i++
                    }
                    x.clear()
                    y.clear()
                } else {
                    x.add(coords[0])
                    y.add(coords[1])
                }
                pi.next()
            }
            res2 = Math.abs(res2) / 2
            if (Math.abs(res1 - res2) >= 1e-9) throw RuntimeException()
        }
    }

    internal class CoverageTree(y: IntArray) {
        var count: IntArray
        var len: IntArray
        var y: IntArray
        fun update(from: Int, to: Int, delta: Int) {
            update(from, to, delta, 0, 0, y.size - 1)
        }

        fun update(from: Int, to: Int, delta: Int, root: Int, left: Int, right: Int) {
            if (from == left && to == right) {
                count[root] += delta
            } else {
                val mid = left + right shr 1
                if (from <= mid) update(from, Math.min(to, mid), delta, 2 * root + 1, left, mid)
                if (to > mid) update(Math.max(from, mid + 1), to, delta, 2 * root + 2, mid + 1, right)
            }
            len[root] =
                if (count[root] != 0) y[right + 1] - y[left] else if (right > left) len[2 * root + 1] + len[2 * root + 2] else 0
        }

        init {
            count = IntArray(4 * y.size)
            len = IntArray(4 * y.size)
            this.y = y
        }
    }
}
