package combinatorics

import java.util.stream.Stream

object RandomGraph {
    // precondition: n >= 2
    fun getRandomTree(V: Int, rnd: Random): Array<List<Integer>> {
        val a = IntArray(V - 2)
        for (i in a.indices) {
            a[i] = rnd.nextInt(V)
        }
        return PruferCode.pruferCode2Tree(a)
    }

    fun getRandomTree2(n: Int, rnd: Random): Array<List<Integer>> {
        val t: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        val p = IntArray(n)
        run {
            var i = 0
            var j: Int
            while (i < n) {
                // random permutation
                j = rnd.nextInt(i + 1)
                p[i] = p[j]
                p[j] = i
                i++
            }
        }
        for (i in 1 until n) {
            val parent = p[rnd.nextInt(i)]
            t[parent].add(p[i])
            t[p[i]].add(parent)
        }
        return t
    }

    // precondition: V >= 2, V-1 <= E <= V*(V-1)/2
    fun getRandomUndirectedConnectedGraph(V: Int, E: Int, rnd: Random): Array<List<Integer>> {
        val g: Array<List<Integer>> = getRandomTree(V, rnd)
        val edgeSet: Set<Long> = LinkedHashSet()
        for (i in 0 until V) {
            for (j in i + 1 until V) {
                edgeSet.add((i as Long shl 32) + j)
            }
        }
        for (i in 0 until V) {
            for (j in g[i]) {
                edgeSet.remove((i as Long shl 32) + j)
            }
        }
        val edges: List<Long> = ArrayList(edgeSet)
        for (x in getRandomArrangement(edges.size(), E - (V - 1), rnd)) {
            val e = edges[x]
            val u = (e ushr 32).toInt()
            val v = e.toInt()
            g[u].add(v)
            g[v].add(u)
        }
        for (i in 0 until V) Collections.sort(g[i])
        return g
    }

    // precondition: V >= 2, V-1 <= E <= V*(V-1)/2
    fun getRandomUndirectedConnectedGraph2(V: Int, E: Int, rnd: Random): Array<List<Integer>> {
        val g: Array<List<Integer>> = getRandomTree(V, rnd)
        val edgeSet: Set<Long> = LinkedHashSet()
        for (i in 0 until V) {
            for (j in g[i]) {
                edgeSet.add((i as Long shl 32) + j)
            }
        }
        for (i in 0 until E - (V - 1)) {
            var u: Int
            var v: Int
            var edge: Long
            while (true) {
                u = rnd.nextInt(V)
                v = rnd.nextInt(V)
                edge = (u.toLong() shl 32) + v
                if (u < v && !edgeSet.contains(edge)) break
            }
            edgeSet.add(edge)
            g[u].add(v)
            g[v].add(u)
        }
        for (i in 0 until V) Collections.sort(g[i])
        return g
    }

    fun getRandomArrangement(n: Int, m: Int, rnd: Random): IntArray {
        val res = IntArray(n)
        for (i in 0 until n) {
            res[i] = i
        }
        for (i in 0 until m) {
            val j: Int = n - 1 - rnd.nextInt(n - i)
            val t = res[i]
            res[i] = res[j]
            res[j] = t
        }
        return Arrays.copyOf(res, m)
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val tree: Array<List<Integer>> = PruferCode.pruferCode2Tree(intArrayOf(3, 3, 3, 4))
        System.out.println(Arrays.toString(tree))
        System.out.println(Arrays.toString(PruferCode.tree2PruferCode(tree)))
        System.out.println(Arrays.toString(PruferCode.pruferCode2Tree(intArrayOf(0, 0))))
        val rnd = Random(1)
        for (step in 0..999) {
            val V: Int = rnd.nextInt(50) + 2
            checkGraph(V, V - 1, rnd)
            checkGraph(V, V * (V - 1) / 2, rnd)
            checkGraph(V, rnd.nextInt(V * (V - 1) / 2 - (V - 1) + 1) + V - 1, rnd)
        }
    }

    fun checkGraph(V: Int, E: Int, rnd: Random) {
        val g: Array<List<Integer>> = getRandomUndirectedConnectedGraph(V, E, rnd)
        val n = g.size
        val a = Array(n) { IntArray(n) }
        var edges = 0
        for (i in 0 until n) {
            for (j in g[i]) {
                ++a[i][j]
                ++edges
            }
        }
        if (edges != 2 * E) {
            throw RuntimeException()
        }
        for (i in 0 until n) {
            if (a[i][i] != 0) {
                throw RuntimeException()
            }
            for (j in 0 until n) {
                if (a[i][j] != a[j][i] || a[i][j] != 0 && a[i][j] != 1) {
                    throw RuntimeException()
                }
            }
        }
    }
}
