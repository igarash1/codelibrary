package graphs.lca

import java.util.stream.Stream

// Answering LCA queries in O(1) with O(n*log(n)) preprocessing
class LcaSparseTable(tree: Array<List<Integer>>, root: Int) {
    var len: Int
    var up: Array<IntArray>
    var tin: IntArray
    var tout: IntArray
    var time = 0
    fun dfs(tree: Array<List<Integer>>, u: Int, p: Int) {
        tin[u] = time++
        up[0][u] = p
        for (i in 1 until len) up[i][u] = up[i - 1][up[i - 1][u]]
        for (v in tree[u]) if (v != p) dfs(tree, v, u)
        tout[u] = time++
    }

    fun isParent(parent: Int, child: Int): Boolean {
        return tin[parent] <= tin[child] && tout[child] <= tout[parent]
    }

    fun lca(a: Int, b: Int): Int {
        var a = a
        if (isParent(a, b)) return a
        if (isParent(b, a)) return b
        for (i in len - 1 downTo 0) if (!isParent(up[i][a], b)) a = up[i][a]
        return up[0][a]
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val tree: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(5).toArray { _Dummy_.__Array__() }
            tree[0].add(1)
            tree[1].add(0)
            tree[1].add(2)
            tree[2].add(1)
            tree[3].add(1)
            tree[1].add(3)
            tree[0].add(4)
            tree[4].add(0)
            val t = LcaSparseTable(tree, 0)
            System.out.println(1 == t.lca(3, 2))
            System.out.println(0 == t.lca(2, 4))
        }
    }

    init {
        val n = tree.size
        len = 32 - Integer.numberOfLeadingZeros(n)
        up = Array(len) { IntArray(n) }
        tin = IntArray(n)
        tout = IntArray(n)
        dfs(tree, root, root)
    }
}
