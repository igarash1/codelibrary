package misc

import numeric.FFT
import optimization.Simplex

object ArrayRotate {
    fun rotate1(a: IntArray, first: Int, middle: Int, last: Int) {
        var first = first
        var middle = middle
        var next = middle
        while (first != next) {
            swap(a, first++, next++)
            if (next == last) next = middle else if (first == middle) middle = next
        }
    }

    fun rotate2(a: IntArray, first: Int, middle: Int, last: Int) {
        reverse(a, first, middle)
        reverse(a, middle, last)
        reverse(a, first, last)
    }

    fun reverse(a: IntArray, from: Int, to: Int) {
        var from = from
        var to = to
        while (from + 1 < to) swap(a, from++, --to)
    }

    fun swap(a: IntArray, i: Int, j: Int) {
        val t = a[j]
        a[j] = a[i]
        a[i] = t
    }

    fun rotate3(a: IntArray, first: Int, middle: Int, last: Int) {
        val n = last - first
        val jump = middle - first
        var i = first
        var count = 0
        while (count < n) {
            var cur = i
            val tmp = a[cur]
            while (true) {
                ++count
                var next = cur + jump
                if (next >= last) next -= n
                if (next == i) break
                a[cur] = a[next]
                cur = next
            }
            a[cur] = tmp
            i++
        }
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999) {
            val n: Int = rnd.nextInt(10) + 1
            val middle: Int = rnd.nextInt(n)
            val a: IntArray = rnd.ints(n, 0, 10).toArray()
            val b1: IntArray = a.clone()
            rotate1(b1, 0, middle, n)
            val b2: IntArray = a.clone()
            rotate2(b2, 0, middle, n)
            val b3: IntArray = a.clone()
            rotate3(b3, 0, middle, n)
            if (!Arrays.equals(b1, b2) || !Arrays.equals(b1, b3)) throw RuntimeException()
        }
    }
}
