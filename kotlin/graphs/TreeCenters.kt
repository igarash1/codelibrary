package graphs

import java.util.stream.Stream

object TreeCenters {
    // returns 1 or 2 tree centers
    // http://en.wikipedia.org/wiki/Graph_center
    fun findTreeCenters(tree: Array<List<Integer>>): List<Integer> {
        val n = tree.size
        var leaves: List<Integer> = ArrayList()
        val degree = IntArray(n)
        for (i in 0 until n) {
            degree[i] = tree[i].size()
            if (degree[i] <= 1) {
                leaves.add(i)
            }
        }
        var removedLeaves: Int = leaves.size()
        while (removedLeaves < n) {
            val nleaves: List<Integer> = ArrayList()
            for (u in leaves) {
                for (v in tree[u]) {
                    if (--degree[v] == 1) {
                        nleaves.add(v)
                    }
                }
            }
            leaves = nleaves
            removedLeaves += leaves.size()
        }
        return leaves
    }

    // returns vertex that has all its subtrees sizes <= n/2
    fun findTreeCentroid(tree: Array<List<Integer>>, u: Int, p: Int): Int {
        val n = tree.size
        var cnt = 1
        var goodCenter = true
        for (v in tree[u]) {
            if (v == p) continue
            val res = findTreeCentroid(tree, v, u)
            if (res >= 0) return res
            val size = -res
            goodCenter = goodCenter and (size <= n / 2)
            cnt += size
        }
        goodCenter = goodCenter and (n - cnt <= n / 2)
        return if (goodCenter) u else -cnt
    }

    fun diameter(tree: Array<List<Integer>>): Int {
        val furthestVertex = dfs(tree, 0, -1, 0).toInt()
        return (dfs(tree, furthestVertex, -1, 0) ushr 32).toInt()
    }

    fun dfs(tree: Array<List<Integer>>, u: Int, p: Int, depth: Int): Long {
        var res = (depth.toLong() shl 32) + u
        for (v in tree[u]) if (v != p) res = Math.max(res, dfs(tree, v, u, depth + 1))
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val n = 4
        val tree: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        tree[3].add(0)
        tree[0].add(3)
        tree[3].add(1)
        tree[1].add(3)
        tree[3].add(2)
        tree[2].add(3)
        System.out.println(3 == findTreeCentroid(tree, 0, -1))
        System.out.println(3 == findTreeCenters(tree)[0])
        System.out.println(2 == diameter(tree))
    }
}
