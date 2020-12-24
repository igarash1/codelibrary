package graphs.lca

import java.util.stream.Stream

// Answering LCA queries in O(1) with O(n) preprocessing
class LcaSchieberVishkin(tree: Array<List<Integer>>, root: Int) {
    var parent: IntArray
    var preOrder: IntArray
    var I: IntArray
    var head: IntArray
    var A: IntArray
    var time = 0
    fun dfs1(tree: Array<List<Integer>>, u: Int, p: Int) {
        parent[u] = p
        preOrder[u] = time++
        I[u] = preOrder[u]
        for (v in tree[u]) {
            if (v == p) continue
            dfs1(tree, v, u)
            if (Integer.lowestOneBit(I[u]) < Integer.lowestOneBit(I[v])) {
                I[u] = I[v]
            }
        }
        head[I[u]] = u
    }

    fun dfs2(tree: Array<List<Integer>>, u: Int, p: Int, up: Int) {
        A[u] = up or Integer.lowestOneBit(I[u])
        for (v in tree[u]) {
            if (v == p) continue
            dfs2(tree, v, u, A[u])
        }
    }

    fun enterIntoStrip(x: Int, hz: Int): Int {
        if (Integer.lowestOneBit(I[x]) === hz) return x
        val hw: Int = Integer.highestOneBit(A[x] and hz - 1)
        return parent[head[I[x] and -hw or hw]]
    }

    fun lca(x: Int, y: Int): Int {
        val hb: Int = if (I[x] == I[y]) Integer.lowestOneBit(I[x]) else Integer.highestOneBit(I[x] xor I[y])
        val hz: Int = Integer.lowestOneBit(A[x] and A[y] and -hb)
        val ex = enterIntoStrip(x, hz)
        val ey = enterIntoStrip(y, hz)
        return if (preOrder[ex] < preOrder[ey]) ex else ey
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val tree: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(5).toArray { _Dummy_.__Array__() }
            tree[0].add(1)
            tree[0].add(2)
            tree[1].add(3)
            tree[1].add(4)
            val lca = LcaSchieberVishkin(tree, 0)
            System.out.println(0 == lca.lca(1, 2))
            System.out.println(1 == lca.lca(3, 4))
            System.out.println(0 == lca.lca(4, 2))
        }
    }

    init {
        val n = tree.size
        preOrder = IntArray(n)
        I = IntArray(n)
        head = IntArray(n)
        A = IntArray(n)
        parent = IntArray(n)
        dfs1(tree, root, -1)
        dfs2(tree, root, -1, 0)
    }
}
