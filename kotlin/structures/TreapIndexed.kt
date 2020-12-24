package structures

import java.util.function.Predicate

// https://cp-algorithms.com/data_structures/treap.html
object TreapIndexed {
    var random: Random = Random()
    fun split(root: Node?, minRight: Int): TreapPair {
        if (root == null) return TreapPair(null, null)
        root.push()
        return if (Node.getSize(root.left) >= minRight) {
            val sub = split(root.left, minRight)
            root.left = sub.right
            root.pull()
            sub.right = root
            sub
        } else {
            val sub = split(
                root.right,
                minRight - Node.getSize(root.left) - 1
            )
            root.right = sub.left
            root.pull()
            sub.left = root
            sub
        }
    }

    fun merge(left: Node?, right: Node?): Node? {
        if (left == null) return right
        if (right == null) return left
        left.push()
        right.push()
        return if (left.prio > right.prio) {
            left.right = merge(left.right, right)
            left.pull()
            left
        } else {
            right.left = merge(left, right.left)
            right.pull()
            right
        }
    }

    fun insert(root: Node?, index: Int, value: Long): Node? {
        val t = split(root, index)
        return merge(merge(t.left, Node(value)), t.right)
    }

    fun remove(root: Node?, index: Int): Node? {
        val t = split(root, index)
        return merge(t.left, split(t.right, index + 1 - Node.getSize(t.left)).right)
    }

    fun modify(root: Node?, ll: Int, rr: Int, delta: Long): Node? {
        val t1 = split(root, rr + 1)
        val t2 = split(t1.left, ll)
        if (t2.right != null) t2.right!!.apply(delta)
        return merge(merge(t2.left, t2.right), t1.right)
    }

    fun query(root: Node?, ll: Int, rr: Int): TreapAndResult {
        val t1 = split(root, rr + 1)
        val t2 = split(t1.left, ll)
        val mx = Node.getMx(t2.right)
        val sum = Node.getSum(t2.right)
        return TreapAndResult(merge(merge(t2.left, t2.right), t1.right), mx, sum)
    }

    // calls all FALSE elements to the left of the sought position exactly once
    fun findFirst(root: Node?, ll: Int, rr: Int, f: Predicate<Node?>): Int {
        return findFirst(root, ll, rr, f, 0, Node.getSize(root) - 1)
    }

    fun findFirst(root: Node?, ll: Int, rr: Int, f: Predicate<Node?>, l: Int, r: Int): Int {
        if (ll <= l && r <= rr && !f.test(root)) {
            return -1
        }
        if (l == r) {
            return l
        }
        root!!.push()
        val m = Node.getSize(root.left)
        var res = -1
        if (ll < m) {
            res = findFirst(root.left, ll, rr, f, l, l + m - 1)
        }
        if (res == -1) {
            val single = Node(0)
            single.size = 1
            single.apply(root.nodeValue)
            res = findFirst(single, ll, rr, f, l + m, l + m)
        }
        if (rr > m && res == -1) {
            res = findFirst(root.right, ll, rr, f, l + m + 1, r)
        }
        root.pull()
        return res
    }

    fun print(root: Node?) {
        if (root == null) return
        root.push()
        print(root.left)
        System.out.print(root.nodeValue.toString() + " ")
        print(root.right)
    }

    // Returns min(p | p<=rr && sum[ll..p]>=sum). If no such p exists, returns -1
    fun sumLowerBound(treap: Node?, ll: Int, rr: Int, sum: Long): Int {
        val sumSoFar = LongArray(1)
        return findFirst(treap, ll, rr, Predicate<Node> { node ->
            if (sumSoFar[0] + node.sum >= sum) return@findFirst true
            sumSoFar[0] += node.sum
            false
        })
    }

    // Random test
    fun main(args: Array<String?>?) {
        var treap: Node? = null
        val list: List<Integer> = ArrayList()
        val rnd = Random(1)
        for (step in 0..99999) {
            val cmd: Int = rnd.nextInt(6)
            if (cmd < 2 && list.size() < 100) {
                val pos: Int = rnd.nextInt(list.size() + 1)
                val value: Int = rnd.nextInt(100)
                list.add(pos, value)
                treap = insert(treap, pos, value.toLong())
            } else if (cmd < 3 && list.size() > 0) {
                val pos: Int = rnd.nextInt(list.size())
                list.remove(pos)
                treap = remove(treap, pos)
            } else if (cmd < 4 && list.size() > 0) {
                val b: Int = rnd.nextInt(list.size())
                val a: Int = rnd.nextInt(b + 1)
                var res: Int = list[a]
                for (i in a + 1..b) res = Math.max(res, list[i])
                val tr = query(treap, a, b)
                treap = tr.treap
                if (res.toLong() != tr.mx) throw RuntimeException()
            } else if (cmd < 5 && list.size() > 0) {
                val b: Int = rnd.nextInt(list.size())
                val a: Int = rnd.nextInt(b + 1)
                val delta: Int = rnd.nextInt(100) - 50
                for (i in a..b) list.set(i, list[i] + delta)
                treap = modify(treap, a, b, delta.toLong())
            } else {
                for (i in 0 until list.size()) {
                    val tr = query(treap, i, i)
                    treap = tr.treap
                    val v = tr.mx
                    if (list[i] !== v) throw RuntimeException()
                }
            }
        }
        System.out.println("Test passed")
        treap = null
        for (v in longArrayOf(2, 1, 10, 20)) {
            treap = insert(treap, Node.getSize(treap), v)
        }
        System.out.println(2 == sumLowerBound(treap, 0, treap!!.size - 1, 12))
    }

    class Node internal constructor(var nodeValue: Long) {
        var mx: Long
        var sum: Long
        var add: Long
        var size: Int
        var prio: Long
        var left: Node? = null
        var right: Node? = null
        fun apply(v: Long) {
            nodeValue += v
            mx += v
            sum += v * size
            add += v
        }

        fun push() {
            if (add != 0L) {
                if (left != null) left!!.apply(add)
                if (right != null) right!!.apply(add)
                add = 0
            }
        }

        fun pull() {
            mx = Math.max(nodeValue, Math.max(getMx(left), getMx(right)))
            sum = nodeValue + getSum(left) + getSum(right)
            size = 1 + getSize(left) + getSize(right)
        }

        companion object {
            fun getMx(root: Node?): Long {
                return root?.mx ?: Long.MIN_VALUE
            }

            fun getSum(root: Node?): Long {
                return root?.sum ?: 0
            }

            fun getSize(root: Node?): Int {
                return root?.size ?: 0
            }
        }

        init {
            mx = nodeValue
            sum = nodeValue
            add = 0
            size = 1
            prio = random.nextLong()
        }
    }

    class TreapPair internal constructor(var left: Node?, var right: Node?)
    class TreapAndResult internal constructor(var treap: Node?, var mx: Long, var sum: Long)
}
