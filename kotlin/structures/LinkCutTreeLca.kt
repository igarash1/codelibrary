package structures

import numeric.FFT
import optimization.Simplex

// Based on Daniel Sleator's implementation http://www.codeforces.com/contest/117/submission/860934
object LinkCutTreeLca {
    fun connect(ch: Node?, p: Node?, isLeftChild: Boolean?) {
        if (ch != null) ch.parent = p
        if (isLeftChild != null) {
            if (isLeftChild) p!!.left = ch else p!!.right = ch
        }
    }

    // rotates edge (x, x.parent)
    //        g           g
    //       /           /
    //      p           x
    //     / \   -->   / \
    //    x  p.r     x.l  p
    //   / \             / \
    // x.l x.r         x.r p.r
    fun rotate(x: Node?) {
        val p = x!!.parent
        val g = p!!.parent
        val isRootP = p.isRoot
        val leftChildX = x === p.left

        // create 3 edges: (x.r(l),p), (p,x), (x,g)
        connect(if (leftChildX) x.right else x.left, p, leftChildX)
        connect(p, x, !leftChildX)
        connect(x, g, if (!isRootP) p === g!!.left else null)
    }

    // brings x to the root, balancing tree
    //
    // zig-zig case
    //        g                                  x
    //       / \               p                / \
    //      p  g.r rot(p)    /   \     rot(x) x.l  p
    //     / \      -->    x       g    -->       / \
    //    x  p.r          / \     / \           x.r  g
    //   / \            x.l x.r p.r g.r             / \
    // x.l x.r                                    p.r g.r
    //
    // zig-zag case
    //      g               g
    //     / \             / \               x
    //    p  g.r rot(x)   x  g.r rot(x)    /   \
    //   / \      -->    / \      -->    p       g
    // p.l  x           p  x.r          / \     / \
    //     / \         / \            p.l x.l x.r g.r
    //   x.l x.r     p.l x.l
    fun splay(x: Node?) {
        while (!x!!.isRoot) {
            val p = x.parent
            val g = p!!.parent
            if (!p.isRoot) rotate(if (x === p.left == (p === g!!.left)) p /*zig-zig*/ else x /*zig-zag*/)
            rotate(x)
        }
    }

    // makes node x the root of the virtual tree, and also x becomes the leftmost node in its splay tree
    fun expose(x: Node?): Node? {
        var last: Node? = null
        var y = x
        while (y != null) {
            splay(y)
            y.left = last
            last = y
            y = y.parent
        }
        splay(x)
        return last
    }

    fun findRoot(x: Node?): Node? {
        var x = x
        expose(x)
        while (x!!.right != null) x = x.right
        splay(x)
        return x
    }

    fun link(x: Node?, y: Node?) {
        if (findRoot(x) === findRoot(y)) throw RuntimeException("error: x and y are already connected")
        expose(x)
        if (x!!.right != null) throw RuntimeException("error: x is not a root node")
        x.parent = y
    }

    fun cut(x: Node?) {
        expose(x)
        if (x!!.right == null) throw RuntimeException("error: x is a root node")
        x.right!!.parent = null
        x.right = null
    }

    fun lca(x: Node?, y: Node?): Node? {
        if (findRoot(x) !== findRoot(y)) throw RuntimeException("error: x and y are not connected")
        expose(x)
        return expose(y)
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999) {
            val n: Int = rnd.nextInt(50) + 1
            val p = IntArray(n)
            Arrays.fill(p, -1)
            val nodes = arrayOfNulls<Node>(n)
            for (i in 0 until n) nodes[i] = Node()
            for (query in 0..9999) {
                val cmd: Int = rnd.nextInt(10)
                val u: Int = rnd.nextInt(n)
                val x = nodes[u]
                if (cmd == 0) {
                    expose(x)
                    if (x!!.right != null != (p[u] != -1)) throw RuntimeException()
                    if (x.right != null) {
                        cut(x)
                        p[u] = -1
                    }
                } else if (cmd == 1) {
                    val v: Int = rnd.nextInt(n)
                    val y = nodes[v]
                    if (findRoot(x) === findRoot(y) != (root(p, u) == root(p, v))) throw RuntimeException()
                    if (findRoot(x) === findRoot(y)) {
                        val lca = lca(x, y)
                        var cur = u
                        val path: Set<Integer> = HashSet()
                        while (cur != -1) {
                            path.add(cur)
                            cur = p[cur]
                        }
                        cur = v
                        while (cur != -1 && !path.contains(cur)) {
                            cur = p[cur]
                        }
                        if (lca !== nodes[cur]) throw RuntimeException()
                    }
                } else {
                    expose(x)
                    if (x!!.right == null != (p[u] == -1)) throw RuntimeException()
                    if (x.right == null) {
                        val v: Int = rnd.nextInt(n)
                        val y = nodes[v]
                        if (findRoot(x) !== findRoot(y) != (root(p, u) != root(p, v))) throw RuntimeException()
                        if (findRoot(x) !== findRoot(y)) {
                            link(x, y)
                            p[u] = v
                        }
                    }
                }
            }
        }
    }

    fun root(p: IntArray, u: Int): Int {
        var root = u
        while (p[root] != -1) root = p[root]
        return root
    }

    class Node {
        var left: Node? = null
        var right: Node? = null
        var parent: Node? = null

        // tests whether x is a root of a splay tree
        val isRoot: Boolean
            get() = parent == null || parent!!.left !== this && parent!!.right !== this
    }
}
