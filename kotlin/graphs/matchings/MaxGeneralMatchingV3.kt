package graphs.matchings

import java.util.stream.IntStream

// https://en.wikipedia.org/wiki/Blossom_algorithm in O(V^3)
object MaxGeneralMatchingV3 {
    fun maxMatching(graph: Array<List<Integer>>): Int {
        val n = graph.size
        val match = IntArray(n)
        Arrays.fill(match, -1)
        val p = IntArray(n)
        for (i in 0 until n) {
            if (match[i] == -1) {
                var v = findPath(graph, match, p, i)
                while (v != -1) {
                    val pv = p[v]
                    val ppv = match[pv]
                    match[v] = pv
                    match[pv] = v
                    v = ppv
                }
            }
        }
        return Arrays.stream(match).filter { x -> x !== -1 }.count() as Int / 2
    }

    fun findPath(graph: Array<List<Integer>>, match: IntArray, p: IntArray, root: Int): Int {
        Arrays.fill(p, -1)
        val n = graph.size
        val bases: IntArray = IntStream.range(0, n).toArray()
        val used = BooleanArray(n)
        val q = IntArray(n)
        var qt = 0
        used[root] = true
        q[qt++] = root
        for (qh in 0 until qt) {
            val u = q[qh]
            for (v in graph[u]) {
                if (bases[u] == bases[v] || match[u] == v) continue
                if (v == root || match[v] != -1 && p[match[v]] != -1) {
                    val base = lca(match, bases, p, u, v)
                    val blossom = BooleanArray(n)
                    markPath(match, bases, blossom, p, u, base, v)
                    markPath(match, bases, blossom, p, v, base, u)
                    for (i in 0 until n) if (blossom[bases[i]]) {
                        bases[i] = base
                        if (!used[i]) {
                            used[i] = true
                            q[qt++] = i
                        }
                    }
                } else if (p[v] == -1) {
                    p[v] = u
                    if (match[v] == -1) return v
                    v = match[v]
                    used[v] = true
                    q[qt++] = v
                }
            }
        }
        return -1
    }

    fun markPath(match: IntArray, bases: IntArray, blossom: BooleanArray, p: IntArray, u: Int, base: Int, child: Int) {
        var u = u
        var child = child
        while (bases[u] != base) {
            blossom[bases[match[u]]] = true
            blossom[bases[u]] = blossom[bases[match[u]]]
            p[u] = child
            child = match[u]
            u = p[match[u]]
        }
    }

    fun lca(match: IntArray, base: IntArray, p: IntArray, a: Int, b: Int): Int {
        var a = a
        var b = b
        val used = BooleanArray(match.size)
        while (true) {
            a = base[a]
            used[a] = true
            if (match[a] == -1) break
            a = p[match[a]]
        }
        while (true) {
            b = base[b]
            if (used[b]) return b
            b = p[match[b]]
        }
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val graph: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(4).toArray { _Dummy_.__Array__() }
        graph[0].add(1)
        graph[1].add(0)
        graph[2].add(1)
        graph[1].add(2)
        graph[2].add(0)
        graph[0].add(2)
        graph[3].add(0)
        graph[0].add(3)
        System.out.println(2 == maxMatching(graph))
    }
}
