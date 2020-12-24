package geometry

import java.util.Arrays

object SegmentsUnion {
    fun segmentsUnionLength(x1: DoubleArray, x2: DoubleArray): Double {
        val n = x1.size
        val points = arrayOfNulls<Point>(n * 2)
        for (i in 0 until n) {
            points[i * 2] = Point(x1[i], 0)
            points[i * 2 + 1] = Point(x2[i], 1)
        }
        Arrays.sort(points)
        var union = 0.0
        var balance = 0
        for (i in points.indices) {
            if (balance > 0) union += points[i]!!.x - points[i - 1]!!.x
            if (points[i]!!.type == 0) ++balance else --balance
        }
        return union
    }

    // Usage example
    fun main(args: Array<String?>?) {
        System.out.println(segmentsUnionLength(doubleArrayOf(5.0, 10.0), doubleArrayOf(15.0, 20.0)))
    }

    internal class Point(var x: Double, var type: Int) : Comparable<Point?> {
        @Override
        operator fun compareTo(o: Point): Int {
            val cmp: Int = Double.compare(x, o.x)
            return if (cmp != 0) cmp else Integer.compare(type, o.type)
        }
    }
}
