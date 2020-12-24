package structures

import java.util.function.Predicate

class SegmentTree {
    var n: Int
    var tree: Array<Node?>

    class Node {
        // initial values for leaves
        var mx: Long = 0
        var sum: Long = 0
        var add: Long = 0
        fun apply(l: Int, r: Int, v: Long) {
            mx += v
            sum += v * (r - l + 1)
            add += v
        }
    }

    fun push(x: Int, l: Int, r: Int) {
        val m = l + r shr 1
        val y = x + (m - l + 1 shl 1)
        if (tree[x]!!.add != 0L) {
            tree[x + 1]!!.apply(l, m, tree[x]!!.add)
            tree[y]!!.apply(m + 1, r, tree[x]!!.add)
            tree[x]!!.add = 0
        }
    }

    fun pull(x: Int, y: Int) {
        tree[x] = unite(tree[x + 1], tree[y])
    }

    internal constructor(n: Int) {
        this.n = n
        tree = arrayOfNulls(2 * n - 1)
        for (i in tree.indices) tree[i] = Node()
        build(0, 0, n - 1)
    }

    internal constructor(v: LongArray) {
        n = v.size
        tree = arrayOfNulls(2 * n - 1)
        for (i in tree.indices) tree[i] = Node()
        build(0, 0, n - 1, v)
    }

    fun build(x: Int, l: Int, r: Int) {
        if (l == r) {
            return
        }
        val m = l + r shr 1
        val y = x + (m - l + 1 shl 1)
        build(x + 1, l, m)
        build(y, m + 1, r)
        pull(x, y)
    }

    fun build(x: Int, l: Int, r: Int, v: LongArray) {
        if (l == r) {
            tree[x]!!.apply(l, r, v[l])
            return
        }
        val m = l + r shr 1
        val y = x + (m - l + 1 shl 1)
        build(x + 1, l, m, v)
        build(y, m + 1, r, v)
        pull(x, y)
    }

    operator fun get(ll: Int, rr: Int): Node? {
        return get(ll, rr, 0, 0, n - 1)
    }

    operator fun get(ll: Int, rr: Int, x: Int, l: Int, r: Int): Node? {
        if (ll <= l && r <= rr) {
            return tree[x]
        }
        val m = l + r shr 1
        val y = x + (m - l + 1 shl 1)
        push(x, l, r)
        val res: Node?
        res = if (rr <= m) {
            get(ll, rr, x + 1, l, m)
        } else {
            if (ll > m) {
                get(ll, rr, y, m + 1, r)
            } else {
                unite(get(ll, rr, x + 1, l, m), get(ll, rr, y, m + 1, r))
            }
        }
        pull(x, y)
        return res
    }

    @kotlin.jvm.JvmOverloads
    fun modify(ll: Int, rr: Int, v: Long, x: Int = 0, l: Int = 0, r: Int = n - 1) {
        if (ll <= l && r <= rr) {
            tree[x]!!.apply(l, r, v)
            return
        }
        val m = l + r shr 1
        val y = x + (m - l + 1 shl 1)
        push(x, l, r)
        if (ll <= m) {
            modify(ll, rr, v, x + 1, l, m)
        }
        if (rr > m) {
            modify(ll, rr, v, y, m + 1, r)
        }
        pull(x, y)
    }

    // calls all FALSE elements to the left of the sought position exactly once
    fun findFirst(ll: Int, rr: Int, f: Predicate<Node?>): Int {
        return findFirst(ll, rr, f, 0, 0, n - 1)
    }

    fun findFirst(ll: Int, rr: Int, f: Predicate<Node?>, x: Int, l: Int, r: Int): Int {
        if (ll <= l && r <= rr && !f.test(tree[x])) {
            return -1
        }
        if (l == r) {
            return l
        }
        push(x, l, r)
        val m = l + r shr 1
        val y = x + (m - l + 1 shl 1)
        var res = -1
        if (ll <= m) {
            res = findFirst(ll, rr, f, x + 1, l, m)
        }
        if (rr > m && res == -1) {
            res = findFirst(ll, rr, f, y, m + 1, r)
        }
        pull(x, y)
        return res
    }

    companion object {
        fun unite(a: Node?, b: Node?): Node {
            val res = Node()
            res.mx = Math.max(a!!.mx, b!!.mx)
            res.sum = a.sum + b.sum
            return res
        }

        // Returns min(p | p<=rr && sum[ll..p]>=sum). If no such p exists, returns -1
        fun sumLowerBound(t: SegmentTree, ll: Int, rr: Int, sum: Long): Int {
            val sumSoFar = LongArray(1)
            return t.findFirst(ll, rr, Predicate<Node> { node ->
                if (sumSoFar[0] + node.sum >= sum) return@findFirst true
                sumSoFar[0] += node.sum
                false
            })
        }

        // Usage example
        fun main(args: Array<String?>?) {
            val t = SegmentTree(10)
            t.modify(1, 2, 10)
            t.modify(2, 3, 20)
            System.out.println(30L == t[1, 3]!!.mx)
            System.out.println(60L == t[1, 3]!!.sum)
            val tt = SegmentTree(longArrayOf(2, 1, 10, 20))
            System.out.println(2 == sumLowerBound(tt, 0, tt.n - 1, 12))
        }
    }
}
