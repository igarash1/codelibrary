package structures

import java.util.ArrayList

// Heavy-light decomposition with path queries. Query complexity is O(log^2(n)).
// Based on the code from http://codeforces.com/blog/entry/22072
class HeavyLight(tree: Array<List<Integer>>, valuesOnVertices: Boolean) {
    var tree: Array<List<Integer>>
    var valuesOnVertices // true - values on vertices, false - values on edges
            : Boolean
    var segmentTree: SegmentTree
    var parent: IntArray
    var depth: IntArray
    var pathRoot: IntArray
    var `in`: IntArray
    var time = 0
    fun dfs1(u: Int): Int {
        var size = 1
        var maxSubtree = 0
        for (i in 0 until tree[u].size()) {
            val v: Int = tree[u][i]
            if (v == parent[u]) continue
            parent[v] = u
            depth[v] = depth[u] + 1
            val subtree = dfs1(v)
            if (maxSubtree < subtree) {
                maxSubtree = subtree
                tree[u].set(i, tree[u].set(0, v))
            }
            size += subtree
        }
        return size
    }

    fun dfs2(u: Int) {
        `in`[u] = time++
        for (v in tree[u]) {
            if (v == parent[u]) continue
            pathRoot[v] = if (v == tree[u][0]) pathRoot[u] else v
            dfs2(v)
        }
    }

    operator fun get(u: Int, v: Int): SegmentTree.Node {
        val res: Array<SegmentTree.Node> = arrayOf<SegmentTree.Node>(Node())
        processPath(
            u,
            v,
            BiConsumer<Integer, Integer> { a, b -> res[0] = SegmentTree.unite(res[0], segmentTree.get(a, b)) })
        return res[0]
    }

    fun modify(u: Int, v: Int, delta: Long) {
        processPath(u, v, BiConsumer<Integer, Integer> { a, b -> segmentTree.modify(a, b, delta) })
    }

    fun processPath(u: Int, v: Int, op: BiConsumer<Integer?, Integer?>) {
        var u = u
        var v = v
        while (pathRoot[u] != pathRoot[v]) {
            if (depth[pathRoot[u]] > depth[pathRoot[v]]) {
                val t = u
                u = v
                v = t
            }
            op.accept(`in`[pathRoot[v]], `in`[v])
            v = parent[pathRoot[v]]
        }
        if (u != v || valuesOnVertices) op.accept(
            Math.min(`in`[u], `in`[v]) + if (valuesOnVertices) 0 else 1, Math.max(
                `in`[u], `in`[v]
            )
        )
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val tree: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(5).toArray { _Dummy_.__Array__() }
            tree[0].add(1)
            tree[1].add(0)
            tree[0].add(2)
            tree[2].add(0)
            tree[1].add(3)
            tree[3].add(1)
            tree[1].add(4)
            tree[4].add(1)
            val hlV = HeavyLight(tree, true)
            hlV.modify(3, 2, 1)
            hlV.modify(1, 0, -1)
            System.out.println(1 == hlV[4, 2].sum)
            val hlE = HeavyLight(tree, false)
            hlE.modify(3, 2, 1)
            hlE.modify(1, 0, -1)
            System.out.println(1 == hlE[4, 2].sum)
        }
    }

    init {
        this.tree = tree
        this.valuesOnVertices = valuesOnVertices
        val n = tree.size
        segmentTree = SegmentTree(n)
        parent = IntArray(n)
        depth = IntArray(n)
        pathRoot = IntArray(n)
        `in` = IntArray(n)
        parent[0] = -1
        dfs1(0)
        dfs2(0)
    }
}
