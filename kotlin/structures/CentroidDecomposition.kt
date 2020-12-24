package structures

import java.util.stream.Stream

// https://sai16vicky.wordpress.com/2014/11/01/divide-and-conquer-on-trees-centroid-decomposition/
object CentroidDecomposition {
    fun calcSizes(tree: Array<List<Integer>>, size: IntArray, deleted: BooleanArray, u: Int, p: Int) {
        size[u] = 1
        for (v in tree[u]) {
            if (v == p || deleted[v]) continue
            calcSizes(tree, size, deleted, v, u)
            size[u] += size[v]
        }
    }

    fun findTreeCentroid(
        tree: Array<List<Integer>>,
        size: IntArray,
        deleted: BooleanArray,
        u: Int,
        p: Int,
        vertices: Int
    ): Int {
        for (v in tree[u]) {
            if (v == p || deleted[v]) continue
            if (size[v] > vertices / 2) {
                return findTreeCentroid(tree, size, deleted, v, u, vertices)
            }
        }
        return u
    }

    //    static void dfs(List<Integer>[] tree, boolean[] deleted, int u, int p) {
    //        for (int v : tree[u]) {
    //            if (v == p || deleted[v]) continue;
    //            dfs(tree, deleted, v, u);
    //        }
    //    }
    fun decompose(tree: Array<List<Integer>>, size: IntArray, deleted: BooleanArray, u: Int, total: Int) {
        calcSizes(tree, size, deleted, u, -1)
        val centroid = findTreeCentroid(tree, size, deleted, u, -1, total)
        deleted[centroid] = true

        // process centroid vertex here
        // dfs(tree, deleted, centroid, -1);
        System.out.println(centroid)
        for (v in tree[centroid]) {
            if (deleted[v]) continue
            decompose(tree, size, deleted, v, size[v])
        }
    }

    fun centroidDecomposition(tree: Array<List<Integer>>) {
        val n = tree.size
        decompose(tree, IntArray(n), BooleanArray(n), 0, n)
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
        centroidDecomposition(tree)
    }
}
