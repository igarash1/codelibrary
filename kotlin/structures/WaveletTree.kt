package structures

import java.util.Arrays

object WaveletTree {
    fun createTree(a: IntArray): Node {
        val lo: Int = Arrays.stream(a).min().orElse(Integer.MAX_VALUE)
        val hi: Int = Arrays.stream(a).max().orElse(Integer.MIN_VALUE)
        return build(a, 0, a.size, lo, hi)
    }

    fun build(a: IntArray, from: Int, to: Int, lo: Int, hi: Int): Node {
        val node = Node()
        node.lo = lo
        node.hi = hi
        if (lo < hi && from < to) {
            val mid = lo + hi ushr 1
            val p: Predicate<Integer?> = Predicate<Integer> { x -> x <= mid }
            node.b = IntArray(to - from + 1)
            var i = 0
            while (i + 1 < node.b.size) {
                node.b[i + 1] = node.b[i] + if (p.test(a[i + from])) 1 else 0
                i++
            }
            val pivot = stablePartition(a, from, to, p)
            node.left = build(a, from, pivot, lo, mid)
            node.right = build(a, pivot, to, mid + 1, hi)
        }
        return node
    }

    fun stablePartition(a: IntArray, from: Int, to: Int, p: Predicate<Integer?>): Int {
        val b1 = IntArray(to - from + 1)
        val b2 = IntArray(to - from + 1)
        var cnt1 = 0
        var cnt2 = 0
        for (i in from until to) {
            if (p.test(a[i])) {
                b1[cnt1++] = a[i]
            } else {
                b2[cnt2++] = a[i]
            }
        }
        System.arraycopy(b1, 0, a, from, cnt1)
        System.arraycopy(b2, 0, a, from + cnt1, cnt2)
        return cnt1
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val a = intArrayOf(5, 1, 2, 1, 1)
        val t = createTree(a)
        System.out.println(t.countEq(1, 5, 1))
    }

    class Node {
        var lo = 0
        var hi = 0
        var left: Node? = null
        var right: Node? = null
        var b: IntArray

        // kth smallest element in [from, to]
        fun kth(from: Int, to: Int, k: Int): Int {
            if (from > to) return 0
            if (lo == hi) return lo
            val inLeft = b[to] - b[from - 1]
            val lb = b[from - 1] // amt of nos in first (from-1) nos that go in left
            val rb = b[to] // amt of nos in first (to) nos that go in left
            return if (k <= inLeft) left!!.kth(lb + 1, rb, k) else right!!.kth(from - lb, to - rb, k - inLeft)
        }

        // number of elements in [from, to] less than or equal to k
        fun countLessOrEq(from: Int, to: Int, k: Int): Int {
            if (from > to || k < lo) return 0
            if (hi <= k) return to - from + 1
            val lb = b[from - 1]
            val rb = b[to]
            return left!!.countLessOrEq(lb + 1, rb, k) + right!!.countLessOrEq(from - lb, to - rb, k)
        }

        // number of elements in [from, to] equal to k
        fun countEq(from: Int, to: Int, k: Int): Int {
            if (from > to || k < lo || k > hi) return 0
            if (lo == hi) return to - from + 1
            val lb = b[from - 1]
            val rb = b[to]
            val mid = lo + hi ushr 1
            return if (k <= mid) left!!.countEq(lb + 1, rb, k) else right!!.countEq(from - lb, to - rb, k)
        }
    }
}
