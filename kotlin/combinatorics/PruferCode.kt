package combinatorics

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Pr%C3%BCfer_sequence
object PruferCode {
    // O(n) complexity
    fun pruferCode2Tree(pruferCode: IntArray): Array<List<Integer>> {
        val n = pruferCode.size + 2
        val tree: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        val degree = IntArray(n)
        Arrays.fill(degree, 1)
        for (v in pruferCode) ++degree[v]
        var ptr = 0
        while (degree[ptr] != 1) ++ptr
        var leaf = ptr
        for (v in pruferCode) {
            tree[leaf].add(v)
            tree[v].add(leaf)
            --degree[leaf]
            --degree[v]
            leaf = if (degree[v] == 1 && v < ptr) {
                v
            } else {
                while (degree[++ptr] != 1);
                ptr
            }
        }
        for (v in 0 until n - 1) {
            if (degree[v] == 1) {
                tree[v].add(n - 1)
                tree[n - 1].add(v)
            }
        }
        return tree
    }

    // precondition: n >= 2
    // O(n) complexity
    fun tree2PruferCode(tree: Array<List<Integer>>): IntArray {
        val n = tree.size
        val parent = IntArray(n)
        parent[n - 1] = -1
        pruferDfs(tree, parent, n - 1)
        val degree = IntArray(n)
        var ptr = -1
        for (i in 0 until n) {
            degree[i] = tree[i].size()
            if (degree[i] == 1 && ptr == -1) ptr = i
        }
        val res = IntArray(n - 2)
        var i = 0
        var leaf = ptr
        while (i < n - 2) {
            val next = parent[leaf]
            res[i] = next
            --degree[next]
            leaf = if (degree[next] == 1 && next < ptr) {
                next
            } else {
                while (degree[++ptr] != 1);
                ptr
            }
            ++i
        }
        return res
    }

    fun pruferDfs(tree: Array<List<Integer>>, parent: IntArray, u: Int) {
        for (v in tree[u]) {
            if (v != parent[u]) {
                parent[v] = u
                pruferDfs(tree, parent, v)
            }
        }
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val a = IntArray(5)
        do {
            val tree: Array<List<Integer>> = pruferCode2Tree(a)
            val b = tree2PruferCode(tree)
            if (!Arrays.equals(a, b)) throw RuntimeException()
        } while (Arrangements.nextArrangementWithRepeats(a, a.size + 2))
    }
}
