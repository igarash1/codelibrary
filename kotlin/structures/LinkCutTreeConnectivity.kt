package structures

import java.util.Random

// Based on Daniel Sleator's implementation http://www.codeforces.com/contest/117/submission/860934
object LinkCutTreeConnectivity {
    fun connect(ch: Node?, p: Node?, isLeftChild: Boolean?) {
        if (ch != null) ch.parent = p
        if (isLeftChild != null) {
            if (isLeftChild) p!!.left = ch else p!!.right = ch
        }
    }

    // rotates edge (x, x.parent)
    //        g            g
    //       /            /
    //      p            x
    //     / \    ->    / \
    //    x  p.r      x.l  p
    //   / \              / \
    // x.l x.r          x.r p.r
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
            if (!p.isRoot) g!!.push()
            p.push()
            x.push()
            if (!p.isRoot) rotate(if (x === p.left == (p === g!!.left)) p /*zig-zig*/ else x /*zig-zag*/)
            rotate(x)
        }
        x.push()
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

    fun makeRoot(x: Node?) {
        expose(x)
        x!!.revert = !x.revert
    }

    fun connected(x: Node?, y: Node?): Boolean {
        if (x === y) return true
        expose(x)
        // now x.parent is null
        expose(y)
        return x!!.parent != null
    }

    fun link(x: Node?, y: Node?) {
        if (connected(x, y)) throw RuntimeException("error: x and y are already connected")
        makeRoot(x)
        x!!.parent = y
    }

    fun cut(x: Node?, y: Node?) {
        makeRoot(x)
        expose(y)
        // check that exposed path consists of a single edge (y,x)
        if (y!!.right !== x || x!!.left != null || x.right != null) throw RuntimeException("error: no edge (x,y)")
        y!!.right!!.parent = null
        y.right = null
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999) {
            val n: Int = rnd.nextInt(50) + 1
            val g = Array(n) { BooleanArray(n) }
            val nodes = arrayOfNulls<Node>(n)
            for (i in 0 until n) nodes[i] = Node()
            for (query in 0..1999) {
                val cmd: Int = rnd.nextInt(10)
                val u: Int = rnd.nextInt(n)
                val v: Int = rnd.nextInt(n)
                val x = nodes[u]
                val y = nodes[v]
                if (cmd == 0) {
                    makeRoot(x)
                    expose(y)
                    if ((y!!.right === x && x!!.left == null && x.right == null) != g[u][v]) throw RuntimeException()
                    if (y!!.right === x && x!!.left == null && x.right == null) {
                        cut(x, y)
                        g[v][u] = false
                        g[u][v] = g[v][u]
                    }
                } else if (cmd == 1) {
                    if (connected(x, y) != connected(g, u, v, -1)) throw RuntimeException()
                } else {
                    expose(x)
                    if (connected(x, y) != connected(g, u, v, -1)) throw RuntimeException()
                    if (!connected(x, y)) {
                        link(x, y)
                        g[v][u] = true
                        g[u][v] = g[v][u]
                    }
                }
            }
        }
    }

    fun connected(g: Array<BooleanArray>, u: Int, v: Int, p: Int): Boolean {
        if (u == v) return true
        for (i in g.indices) if (i != p && g[u][i] && connected(g, i, v, u)) return true
        return false
    }

    class Node {
        var left: Node? = null
        var right: Node? = null
        var parent: Node? = null
        var revert = false

        // tests whether x is a root of a splay tree
        val isRoot: Boolean
            get() = parent == null || parent!!.left !== this && parent!!.right !== this

        fun push() {
            if (revert) {
                revert = false
                val t = left
                left = right
                right = t
                if (left != null) left!!.revert = !left!!.revert
                if (right != null) right!!.revert = !right!!.revert
            }
        }
    }
}
