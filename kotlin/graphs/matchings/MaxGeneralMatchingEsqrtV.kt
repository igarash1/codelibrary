package graphs.matchings

import java.util.Arrays

// Taken from https://gist.github.com/min-25/aed29a23b004505d2094a5cddaf56ff9
// Tested: https://codeforces.com/contest/1198/submission/62218577
/*
  Maximum Cardinality Matching in General Graphs.
  - O(\sqrt{n} m \log_{\max\{2, 1 + m/n\}} n) time
  - O(n + m) space

  Note: each vertex is 1-indexed.

  Ref:
    Harold N. Gabow,
    "The Weighted Matching Approach to Maximum Cardinality Matching" (2017)
    (https://arxiv.org/abs/1703.03998)
*/
class MaxGeneralMatchingEsqrtV(var N: Int, `in`: Array<Edge>) {
    class Edge(var from: Int, var to: Int)
    class Link(var from: Int, var to: Int)
    class Log(var v: Int, var par: Int)
    class LinkedList {
        var N = 0
        var head: IntArray
        var next: IntArray

        constructor() {}
        constructor(N: Int, M: Int) {
            this.N = N
            next = IntArray(M)
            head = IntArray(N)
            clear()
        }

        fun clear() {
            Arrays.fill(head, -1)
        }

        fun push(h: Int, u: Int) {
            next[u] = head[h]
            head[h] = u
        }
    }

    class Queue {
        var qh = 0
        var qt = 0
        var data: IntArray

        constructor() {}
        constructor(N: Int) {
            qh = 0
            qt = 0
            data = IntArray(N)
        }

        operator fun get(i: Int): Int {
            return data[i]
        }

        fun enqueue(u: Int) {
            data[qt++] = u
        }

        fun dequeue(): Int {
            return data[qh++]
        }

        fun empty(): Boolean {
            return qh == qt
        }

        fun clear() {
            qt = 0
            qh = qt
        }

        fun size(): Int {
            return qt
        }
    }

    class DisjointSetUnion {
        var par: IntArray

        constructor() {}
        constructor(N: Int) {
            par = IntArray(N)
            for (i in 0 until N) par[i] = i
        }

        fun find(u: Int): Int {
            return if (par[u] == u) u else find(par[u]).also { par[u] = it }
        }

        fun unite(u: Int, v: Int) {
            var u = u
            var v = v
            u = find(u)
            v = find(v)
            if (u != v) par[v] = u
        }
    }

    var NH: Int
    var ofs: IntArray
    var edges: Array<Edge?>
    var que: Queue? = null
    var mate: IntArray
    var potential: IntArray
    var label: IntArray
    var link: Array<Link?>
    var dsu_changelog: Array<Log?>
    var dsu_changelog_last_ = 0
    var dsu_changelog_size_ = 0
    var dsu: DisjointSetUnion? = null
    var list: LinkedList? = null
    var blossom: LinkedList? = null
    var stack: IntArray
    var stack_last_ = 0
    var time_current_ = 0
    var time_augment_ = 0
    var contract_count_ = 0
    var outer_id_ = 0
    fun maximum_matching(): Int {
        initialize()
        var match = 0
        while (match * 2 + 1 < N) {
            reset_count()
            val has_augmenting_path = do_edmonds_search()
            if (!has_augmenting_path) break
            match += find_maximal()
            clear()
        }
        return match
    }

    fun reset_count() {
        time_current_ = 0
        time_augment_ = Inf
        contract_count_ = 0
        outer_id_ = 1
        dsu_changelog_last_ = 0
        dsu_changelog_size_ = dsu_changelog_last_
    }

    fun clear() {
        que!!.clear()
        for (u in 1..N) potential[u] = 1
        for (u in 1..N) dsu!!.par[u] = u
        for (t in time_current_..N / 2) list!!.head[t] = -1
        for (u in 1..N) blossom!!.head[u] = -1
    }

    // first phase
    fun grow(x: Int, y: Int, z: Int) {
        label[y] = kInner
        potential[y] = time_current_ // visited time
        link[z] = Link(x, y)
        label[z] = label[x]
        potential[z] = time_current_ + 1
        que!!.enqueue(z)
    }

    fun contract(x: Int, y: Int) {
        var bx = dsu!!.find(x)
        var by = dsu!!.find(y)
        val h = -++contract_count_ + kInner
        label[mate[by]] = h
        label[mate[bx]] = label[mate[by]]
        var lca = -1
        while (true) {
            if (mate[by] != 0) {
                val t = bx
                bx = by
                by = t
            }
            lca = dsu!!.find(link[bx]!!.from)
            bx = lca
            if (label[mate[bx]] == h) break
            label[mate[bx]] = h
        }
        for (bv in intArrayOf(dsu!!.par[x], dsu!!.par[y])) {
            while (bv != lca) {
                val mv = mate[bv]
                link[mv] = Link(x, y)
                label[mv] = label[x]
                potential[mv] = 1 + (time_current_ - potential[mv]) + time_current_
                que!!.enqueue(mv)
                dsu!!.par[mv] = lca
                dsu!!.par[bv] = dsu!!.par[mv]
                dsu_changelog[dsu_changelog_last_++] = Log(bv, lca)
                dsu_changelog[dsu_changelog_last_++] = Log(mv, lca)
                bv = dsu!!.par[link[bv]!!.from]
            }
        }
    }

    fun find_augmenting_path(): Boolean {
        while (!que!!.empty()) {
            val x = que!!.dequeue()
            val lx = label[x]
            val px = potential[x]
            var bx = dsu!!.find(x)
            for (eid in ofs[x] until ofs[x + 1]) {
                val y = edges[eid]!!.to
                if (label[y] > 0) { // outer blossom/vertex
                    val time_next = px + potential[y] shr 1
                    if (lx != label[y]) {
                        if (time_next == time_current_) return true
                        time_augment_ = Math.min(time_next, time_augment_)
                    } else {
                        if (bx == dsu!!.find(y)) continue
                        if (time_next == time_current_) {
                            contract(x, y)
                            bx = dsu!!.find(x)
                        } else if (time_next <= NH) list!!.push(time_next, eid)
                    }
                } else if (label[y] == kFree) { // free vertex
                    val time_next = px + 1
                    if (time_next == time_current_) grow(x, y, mate[y]) else if (time_next <= NH) list!!.push(
                        time_next,
                        eid
                    )
                }
            }
        }
        return false
    }

    fun adjust_dual_variables(): Boolean {
        // Return true if the current matching is maximum.
        val time_lim: Int = Math.min(NH + 1, time_augment_)
        ++time_current_
        while (time_current_ <= time_lim) {
            dsu_changelog_size_ = dsu_changelog_last_
            if (time_current_ == time_lim) break
            var updated = false
            var h = list!!.head[time_current_]
            while (h >= 0) {
                val e = edges[h]
                val x = e!!.from
                val y = e.to
                if (label[y] > 0) {
                    // Case: outer -- (free => inner => outer)
                    if (potential[x] + potential[y] != time_current_ shl 1) {
                        h = list!!.next[h]
                        continue
                    }
                    if (dsu!!.find(x) == dsu!!.find(y)) {
                        h = list!!.next[h]
                        continue
                    }
                    if (label[x] != label[y]) {
                        time_augment_ = time_current_
                        return false
                    }
                    contract(x, y)
                    updated = true
                } else if (label[y] == kFree) {
                    grow(x, y, mate[y])
                    updated = true
                }
                h = list!!.next[h]
            }
            list!!.head[time_current_] = -1
            if (updated) return false
            ++time_current_
        }
        return time_current_ > NH
    }

    fun do_edmonds_search(): Boolean {
        label[0] = kFree
        for (u in 1..N) {
            if (mate[u] == 0) {
                que!!.enqueue(u)
                label[u] = u // component id
            } else label[u] = kFree
        }
        while (true) {
            if (find_augmenting_path()) break
            val maximum = adjust_dual_variables()
            if (maximum) return false
            if (time_current_ == time_augment_) break
        }
        for (u in 1..N) {
            if (label[u] > 0) potential[u] -= time_current_ else if (label[u] < 0) potential[u] =
                1 + (time_current_ - potential[u])
        }
        return true
    }

    // second phase
    fun rematch(v: Int, w: Int) {
        val t = mate[v]
        mate[v] = w
        if (mate[t] != v) return
        if (link[v]!!.to == dsu!!.find(link[v]!!.to)) {
            mate[t] = link[v]!!.from
            rematch(mate[t], t)
        } else {
            val x = link[v]!!.from
            val y = link[v]!!.to
            rematch(x, y)
            rematch(y, x)
        }
    }

    fun dfs_augment(x: Int, bx: Int): Boolean {
        val px = potential[x]
        val lx = label[bx]
        for (eid in ofs[x] until ofs[x + 1]) {
            val y = edges[eid]!!.to
            if (px + potential[y] != 0) continue
            val by = dsu!!.find(y)
            val ly = label[by]
            if (ly > 0) { // outer
                if (lx >= ly) continue
                val stack_beg = stack_last_
                var bv = by
                while (bv != bx) {
                    val bw = dsu!!.find(mate[bv])
                    stack[stack_last_++] = bw
                    link[bw] = Link(x, y)
                    dsu!!.par[bw] = bx
                    dsu!!.par[bv] = dsu!!.par[bw]
                    bv = dsu!!.find(link[bv]!!.from)
                }
                while (stack_last_ > stack_beg) {
                    val bv = stack[--stack_last_]
                    var v = blossom!!.head[bv]
                    while (v >= 0) {
                        if (!dfs_augment(v, bx)) {
                            v = blossom!!.next[v]
                            continue
                        }
                        stack_last_ = stack_beg
                        return true
                        v = blossom!!.next[v]
                    }
                }
            } else if (ly == kFree) {
                label[by] = kInner
                val z = mate[by]
                if (z == 0) {
                    rematch(x, y)
                    rematch(y, x)
                    return true
                }
                val bz = dsu!!.find(z)
                link[bz] = Link(x, y)
                label[bz] = outer_id_++
                var v = blossom!!.head[bz]
                while (v >= 0) {
                    if (dfs_augment(v, bz)) return true
                    v = blossom!!.next[v]
                }
            }
        }
        return false
    }

    fun find_maximal(): Int {
        // discard blossoms whose potential is 0.
        for (u in 1..N) dsu!!.par[u] = u
        for (i in 0 until dsu_changelog_size_) {
            dsu!!.par[dsu_changelog[i]!!.v] = dsu_changelog[i]!!.par
        }
        for (u in 1..N) {
            label[u] = kFree
            blossom!!.push(dsu!!.find(u), u)
        }
        var ret = 0
        for (u in 1..N) if (mate[u] == 0) {
            val bu = dsu!!.par[u]
            if (label[bu] != kFree) continue
            label[bu] = outer_id_++
            var v = blossom!!.head[bu]
            while (v >= 0) {
                if (!dfs_augment(v, bu)) {
                    v = blossom!!.next[v]
                    continue
                }
                ret += 1
                break
                v = blossom!!.next[v]
            }
        }
        assert(ret >= 1)
        return ret
    }

    fun initialize() {
        que = Queue(N)
        mate = IntArray(N + 1)
        potential = IntArray(N + 1)
        Arrays.fill(potential, 1)
        label = IntArray(N + 1)
        Arrays.fill(label, kFree)
        link = arrayOfNulls(N + 1)
        for (i in link.indices) link[i] = Link(0, 0)
        dsu_changelog = arrayOfNulls(N)
        dsu = DisjointSetUnion(N + 1)
        list = LinkedList(NH + 1, edges.size)
        blossom = LinkedList(N + 1, N + 1)
        stack = IntArray(N)
        stack_last_ = 0
    }

    companion object {
        const val Inf = 1 shl 30
        const val kInner = -1 // should be < 0
        const val kFree = 0 // should be 0

        // Usage example
        fun main(args: Array<String?>?) {
            val n = 6
            val edges = arrayOf(
                Edge(1, 4), Edge(1, 5), Edge(1, 5), Edge(2, 4), Edge(2, 5), Edge(2, 6),
                Edge(3, 4), Edge(3, 5), Edge(3, 6)
            )
            val mm = MaxGeneralMatchingEsqrtV(n, edges)
            val ans = mm.maximum_matching()
            System.out.println(ans)
            for (i in 1 until n) {
                if (mm.mate[i] > i) {
                    System.out.println(i.toString() + " " + mm.mate[i])
                }
            }
        }
    }

    init {
        NH = N shr 1
        ofs = IntArray(N + 2)
        edges = arrayOfNulls(`in`.size * 2)
        for (e in `in`) {
            ofs[e.from + 1] += 1
            ofs[e.to + 1] += 1
        }
        for (i in 1..N + 1) ofs[i] += ofs[i - 1]
        for (e in `in`) {
            edges[ofs[e.from]++] = e
            edges[ofs[e.to]++] = Edge(e.to, e.from)
        }
        for (i in N + 1 downTo 1) ofs[i] = ofs[i - 1]
        ofs[0] = 0
    }
}
