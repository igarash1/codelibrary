package structures

import numeric.FFT
import optimization.Simplex

// https://en.wikipedia.org/wiki/Persistent_data_structure
object PersistentTree {
    fun build(left: Int, right: Int): Node {
        if (left == right) return Node(0)
        val mid = left + right shr 1
        return Node(build(left, mid), build(mid + 1, right))
    }

    fun sum(from: Int, to: Int, root: Node?, left: Int, right: Int): Int {
        if (from > right || left > to) return 0
        if (from <= left && right <= to) return root!!.sum
        val mid = left + right shr 1
        return sum(from, to, root!!.left, left, mid) + sum(from, to, root.right, mid + 1, right)
    }

    operator fun set(pos: Int, value: Int, root: Node?, left: Int, right: Int): Node {
        if (left == right) return Node(value)
        val mid = left + right shr 1
        return if (pos <= mid) Node(set(pos, value, root!!.left, left, mid), root.right) else Node(
            root!!.left, set(pos, value, root.right, mid + 1, right)
        )
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val n = 10
        val t1 = build(0, n - 1)
        val t2 = set(0, 1, t1, 0, n - 1)
        System.out.println(0 == sum(0, 9, t1, 0, n - 1))
        System.out.println(1 == sum(0, 9, t2, 0, n - 1))
    }

    class Node {
        var left: Node? = null
        var right: Node? = null
        var sum = 0

        internal constructor(value: Int) {
            sum = value
        }

        internal constructor(left: Node?, right: Node?) {
            this.left = left
            this.right = right
            if (left != null) sum += left.sum
            if (right != null) sum += right.sum
        }
    }
}
