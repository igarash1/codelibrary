package structures

import java.util.Random

// https://cp-algorithms.com/data_structures/treap.html
object Treap {
    var random: Random = Random()
    fun split(root: Node?, minRight: Long): TreapPair {
        if (root == null) return TreapPair(null, null)
        root.push()
        return if (root.key >= minRight) {
            val sub = split(root.left, minRight)
            root.left = sub.right
            root.pull()
            sub.right = root
            sub
        } else {
            val sub = split(root.right, minRight)
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

    fun insert(root: Node?, key: Long, value: Long): Node? {
        val t = split(root, key)
        return merge(merge(t.left, Node(key, value)), t.right)
    }

    fun remove(root: Node?, key: Long): Node? {
        val t = split(root, key)
        return merge(t.left, split(t.right, key + 1).right)
    }

    fun modify(root: Node?, ll: Long, rr: Long, delta: Long): Node? {
        val t1 = split(root, rr + 1)
        val t2 = split(t1.left, ll)
        if (t2.right != null) t2.right!!.apply(delta)
        return merge(merge(t2.left, t2.right), t1.right)
    }

    fun query(root: Node?, ll: Long, rr: Long): TreapAndResult {
        val t1 = split(root, rr + 1)
        val t2 = split(t1.left, ll)
        val mx = Node.getMx(t2.right)
        val sum = Node.getSum(t2.right)
        return TreapAndResult(merge(merge(t2.left, t2.right), t1.right), mx, sum)
    }

    fun kth(root: Node?, k: Int): Node? {
        if (k < Node.getSize(root!!.left)) return kth(root.left, k) else if (k > Node.getSize(
                root.left
            )
        ) return kth(root.right, k - Node.getSize(root.left) - 1)
        return root
    }

    fun print(root: Node?) {
        if (root == null) return
        root.push()
        print(root.left)
        System.out.print(root.nodeValue.toString() + " ")
        print(root.right)
    }

    // Random test
    fun main(args: Array<String?>?) {
        var treap: Node? = null
        treap = insert(treap, 5, 3)
        treap = insert(treap, 3, 2)
        treap = insert(treap, 6, 1)
        System.out.println(kth(treap, 1)!!.key)
        System.out.println(query(treap, 1, 10).mx)
        treap = remove(treap, 5)
        System.out.println(query(treap, 1, 10).mx)
    }

    class Node internal constructor(// keys should be unique
        var key: Long, var nodeValue: Long
    ) {
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
