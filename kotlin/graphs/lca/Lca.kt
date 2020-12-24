package graphs.lca

import java.util.stream.Stream

// Answering LCA queries in O(log(n)) with O(n) preprocessing
class Lca(tree: Array<List<Integer>>, root: Int) {
    var depth: IntArray
    var dfs_order: IntArray
    var cnt: Int
    var first: IntArray
    var minPos: IntArray
    var n: Int
    fun dfs(tree: Array<List<Integer>>, u: Int, d: Int) {
        depth[u] = d
        dfs_order[cnt++] = u
        for (v in tree[u]) if (depth[v] == -1) {
            dfs(tree, v, d + 1)
            dfs_order[cnt++] = u
        }
    }

    fun buildTree(node: Int, left: Int, right: Int) {
        if (left == right) {
            minPos[node] = dfs_order[left]
            return
        }
        val mid = left + right shr 1
        buildTree(2 * node + 1, left, mid)
        buildTree(2 * node + 2, mid + 1, right)
        minPos[node] =
            if (depth[minPos[2 * node + 1]] < depth[minPos[2 * node + 2]]) minPos[2 * node + 1] else minPos[2 * node + 2]
    }

    fun lca(a: Int, b: Int): Int {
        return minPos(Math.min(first[a], first[b]), Math.max(first[a], first[b]), 0, 0, n - 1)
    }

    fun minPos(a: Int, b: Int, node: Int, left: Int, right: Int): Int {
        if (a == left && right == b) return minPos[node]
        val mid = left + right shr 1
        return if (a <= mid && b > mid) {
            val p1 = minPos(a, Math.min(b, mid), 2 * node + 1, left, mid)
            val p2 = minPos(Math.max(a, mid + 1), b, 2 * node + 2, mid + 1, right)
            if (depth[p1] < depth[p2]) p1 else p2
        } else if (a <= mid) {
            minPos(a, Math.min(b, mid), 2 * node + 1, left, mid)
        } else if (b > mid) {
            minPos(Math.max(a, mid + 1), b, 2 * node + 2, mid + 1, right)
        } else {
            throw RuntimeException()
        }
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val tree: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(5).toArray { _Dummy_.__Array__() }
            tree[0].add(1)
            tree[0].add(2)
            tree[1].add(3)
            tree[1].add(4)
            val lca = Lca(tree, 0)
            System.out.println(0 == lca.lca(1, 2))
            System.out.println(1 == lca.lca(3, 4))
            System.out.println(0 == lca.lca(4, 2))
        }
    }

    init {
        val nodes = tree.size
        depth = IntArray(nodes)
        Arrays.fill(depth, -1)
        n = 2 * nodes - 1
        dfs_order = IntArray(n)
        cnt = 0
        dfs(tree, root, 0)
        minPos = IntArray(4 * n)
        buildTree(0, 0, n - 1)
        first = IntArray(nodes)
        Arrays.fill(first, -1)
        for (i in dfs_order.indices) if (first[dfs_order[i]] == -1) first[dfs_order[i]] = i
    }
}
