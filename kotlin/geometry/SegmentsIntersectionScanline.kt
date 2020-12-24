package geometry

import numeric.FFT
import optimization.Simplex

object SegmentsIntersectionScanline {
    fun findIntersection(s: Array<Segment?>): Array<Segment?>? {
        val n = s.size
        val events = arrayOfNulls<Event>(n * 2)
        var i = 0
        var cnt = 0
        while (i < n) {
            events[cnt++] = Event(s[i]!!.x1, s[i]!!.y1, 1, s[i])
            events[cnt++] = Event(s[i]!!.x2, s[i]!!.y2, -1, s[i])
            ++i
        }
        Arrays.sort(events, eventComparator)
        val set: NavigableSet<Segment> = TreeSet(segmentComparator)
        for (event in events) {
            val cur = event!!.segment
            if (event.type == 1) {
                val floor: Segment = set.floor(cur)
                if (floor != null && isCrossOrTouchIntersect(cur, floor)) return arrayOf(cur, floor)
                val ceiling: Segment = set.ceiling(cur)
                if (ceiling != null && isCrossOrTouchIntersect(cur, ceiling)) return arrayOf(cur, ceiling)
                set.add(cur)
            } else {
                val lower: Segment = set.lower(cur)
                val higher: Segment = set.higher(cur)
                if (lower != null && higher != null && isCrossOrTouchIntersect(lower, higher)) return arrayOf(
                    lower,
                    higher
                )
                set.remove(cur)
            }
        }
        return null
    }

    val segmentComparator: Comparator<Segment> = label@ Comparator<Segment> { a, b ->
        if (a.x1 < b.x1) {
            val v = cross(a.x1, a.y1, a.x2, a.y2, b.x1, b.y1)
            if (v != 0L) return@label if (v > 0) -1 else 1
        } else if (a.x1 > b.x1) {
            val v = cross(b.x1, b.y1, b.x2, b.y2, a.x1, a.y1)
            if (v != 0L) return@label if (v < 0) -1 else 1
        }
        Integer.compare(a.y1, b.y1)
    }
    val eventComparator: Comparator<Event> =
        Comparator.< Event > comparingInt < geometry . SegmentsIntersectionScanline . Event ? > { e -> e.x }.thenComparingInt { e -> -e.type }
            .thenComparingInt { e -> e.y }

    fun cross(ax: Long, ay: Long, bx: Long, by: Long, cx: Long, cy: Long): Long {
        return (bx - ax) * (cy - ay) - (by - ay) * (cx - ax)
    }

    fun isCrossOrTouchIntersect(s1: Segment?, s2: Segment?): Boolean {
        val x1 = s1!!.x1.toLong()
        val y1 = s1.y1.toLong()
        val x2 = s1.x2.toLong()
        val y2 = s1.y2.toLong()
        val x3 = s2!!.x1.toLong()
        val y3 = s2.y1.toLong()
        val x4 = s2.x2.toLong()
        val y4 = s2.y2.toLong()
        if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2) || Math.max(
                y1,
                y2
            ) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2)
        ) return false
        val z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1)
        val z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1)
        val z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)
        val z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3)
        return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0)
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random()
        for (step in 0..99999) {
            val n: Int = rnd.nextInt(100)
            val s = arrayOfNulls<Segment>(n)
            for (i in 0 until n) {
                val r = 20
                val x1: Int = rnd.nextInt(r) - r / 2
                val y1: Int = rnd.nextInt(r) - r / 2
                val x2: Int = rnd.nextInt(r) - r / 2
                val y2: Int = rnd.nextInt(r) - r / 2
                s[i] = Segment(x1, y1, x2, y2)
            }
            val intersection = findIntersection(s)
            val hasIntersection = hasIntersection(s)
            if (intersection == null == hasIntersection) throw RuntimeException()
        }
    }

    fun hasIntersection(s: Array<Segment?>): Boolean {
        for (i in s.indices) for (j in i + 1 until s.size) if (isCrossOrTouchIntersect(
                s[i], s[j]
            )
        ) return true
        return false
    }

    class Segment(x1: Int, y1: Int, x2: Int, y2: Int) {
        val x1 = 0
        val y1 = 0
        val x2 = 0
        val y2 = 0

        init {
            if (x1 < x2 || x1 == x2 && y1 < y2) {
                this.x1 = x1
                this.y1 = y1
                this.x2 = x2
                this.y2 = y2
            } else {
                this.x1 = x2
                this.y1 = y2
                this.x2 = x1
                this.y2 = y1
            }
        }
    }

    class Event(val x: Int, val y: Int, val type: Int, val segment: Segment?)
}
