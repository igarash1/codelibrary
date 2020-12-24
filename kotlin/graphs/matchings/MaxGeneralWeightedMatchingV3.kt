package graphs.matchings

import numeric.FFT
import optimization.Simplex

// Taken from http://uoj.ac/submission/187480
class MaxGeneralWeightedMatchingV3 {
    class edge(var u: Int, var v: Int, var w: Int)

    var n = 0
    var n_x = 0
    var g = Array(N * 2) { arrayOfNulls<edge>(N * 2) }
    var lab = IntArray(N * 2)
    var match = IntArray(N * 2)
    var slack = IntArray(N * 2)
    var st = IntArray(N * 2)
    var pa = IntArray(N * 2)
    var flower_from = Array(N * 2) { IntArray(N + 1) }
    var S = IntArray(N * 2)
    var vis = IntArray(N * 2)
    var flower: Array<List<Integer>?> = arrayOfNulls(N * 2)
    var q: Deque<Integer> = ArrayDeque()
    fun e_delta(e: edge?): Int {
        return lab[e!!.u] + lab[e.v] - g[e.u][e.v]!!.w * 2
    }

    fun update_slack(u: Int, x: Int) {
        if (slack[x] == 0 || e_delta(g[u][x]) < e_delta(g[slack[x]][x])) slack[x] = u
    }

    fun set_slack(x: Int) {
        slack[x] = 0
        for (u in 1..n) if (g[u][x]!!.w > 0 && st[u] != x && S[st[u]] == 0) update_slack(u, x)
    }

    fun q_push(x: Int) {
        if (x <= n) q.addLast(x) else for (i in flower[x]!!) q_push(i)
    }

    fun set_st(x: Int, b: Int) {
        st[x] = b
        if (x > n) for (i in flower[x]!!) set_st(i, b)
    }

    fun get_pr(b: Int, xr: Int): Int {
        val pr = flower[b]!!.indexOf(xr)
        //        int pr = find(flower[b].begin(), flower[b].end(), xr) - flower[b].begin();
        return if (pr % 2 == 1) {
            reverse(flower[b], 1, flower[b]!!.size())
            // reverse(flower[b].begin() + 1, flower[b].end());
            flower[b]!!.size() as Int - pr
        } else pr
    }

    fun set_match(u: Int, v: Int) {
        match[u] = g[u][v]!!.v
        if (u <= n) return
        val e = g[u][v]
        val xr = flower_from[u][e!!.u]
        val pr = get_pr(u, xr)
        for (i in 0 until pr) set_match(flower[u]!![i], flower[u]!![i xor 1])
        set_match(xr, v)
        rotate(flower[u], 0, pr, flower[u]!!.size())
        //        rotate(flower[u].begin(), flower[u].begin() + pr, flower[u].end());
    }

    fun augment(u: Int, v: Int) {
        var u = u
        var v = v
        while (true) {
            val xnv = st[match[u]]
            set_match(u, v)
            if (xnv == 0) return
            set_match(xnv, st[pa[xnv]])
            u = st[pa[xnv]]
            v = xnv
        }
    }

    fun get_lca(u: Int, v: Int): Int {
        var u = u
        var v = v
        ++t
        while (u != 0 || v != 0) {
            if (u == 0) {
                val tmp = u
                u = v
                v = tmp
                continue
            }
            if (vis[u] == t) return u
            vis[u] = t
            u = st[match[u]]
            if (u != 0) u = st[pa[u]]
            val tmp = u
            u = v
            v = tmp
        }
        return 0
    }

    fun add_blossom(u: Int, lca: Int, v: Int) {
        var b = n + 1
        while (b <= n_x && st[b] != 0) ++b
        if (b > n_x) ++n_x
        lab[b] = 0
        S[b] = 0
        match[b] = match[lca]
        flower[b].clear()
        flower[b].add(lca)
        run {
            var x = u
            var y: Int
            while (x != lca) {
                flower[b].add(x)
                flower[b].add(st[match[x]].also { y = it })
                q_push(y)
                x = st[pa[y]]
            }
        }
        reverse(flower[b], 1, flower[b]!!.size())
        // reverse(flower[b].begin() + 1, flower[b].end());
        run {
            var x = v
            var y: Int
            while (x != lca) {
                flower[b].add(x)
                flower[b].add(st[match[x]].also { y = it })
                q_push(y)
                x = st[pa[y]]
            }
        }
        set_st(b, b)
        for (x in 1..n_x) {
            g[x][b]!!.w = 0
            g[b][x]!!.w = g[x][b]!!.w
        }
        for (x in 1..n) flower_from[b][x] = 0
        for (i in 0 until flower[b]!!.size()) {
            val xs: Int = flower[b]!![i]
            for (x in 1..n_x) if (g[b][x]!!.w == 0 || e_delta(g[xs][x]) < e_delta(g[b][x])) {
                g[b][x] = g[xs][x]
                g[x][b] = g[x][xs]
            }
            for (x in 1..n) if (flower_from[xs][x] != 0) flower_from[b][x] = xs
        }
        set_slack(b)
    }

    fun expand_blossom(b: Int) {
        for (i in flower[b]!!) set_st(i, i)
        val xr = flower_from[b][g[b][pa[b]]!!.u]
        val pr = get_pr(b, xr)
        run {
            var i = 0
            while (i < pr) {
                val xs: Int = flower[b]!![i]
                val xns: Int = flower[b]!![i + 1]
                pa[xs] = g[xns][xs]!!.u
                S[xs] = 1
                S[xns] = 0
                slack[xs] = 0
                set_slack(xns)
                q_push(xns)
                i += 2
            }
        }
        S[xr] = 1
        pa[xr] = pa[b]
        for (i in pr + 1 until flower[b]!!.size()) {
            val xs: Int = flower[b]!![i]
            S[xs] = -1
            set_slack(xs)
        }
        st[b] = 0
    }

    fun on_found_edge(e: edge?): Boolean {
        val u = st[e!!.u]
        val v = st[e.v]
        if (S[v] == -1) {
            pa[v] = e.u
            S[v] = 1
            val nu = st[match[v]]
            slack[nu] = 0
            slack[v] = slack[nu]
            S[nu] = 0
            q_push(nu)
        } else if (S[v] == 0) {
            val lca = get_lca(u, v)
            if (lca == 0) {
                augment(u, v)
                augment(v, u)
                return true
            } else {
                add_blossom(u, lca, v)
            }
        }
        return false
    }

    fun matching(): Boolean {
        Arrays.fill(S, 1, n_x + 1, -1)
        //        memset(S + 1, -1, sizeof( int) *n_x);
        Arrays.fill(slack, 1, n_x + 1, 0)
        //        memset(slack + 1, 0, sizeof( int) *n_x);
        q = ArrayDeque()
        for (x in 1..n_x) if (st[x] == x && match[x] == 0) {
            pa[x] = 0
            S[x] = 0
            q_push(x)
        }
        if (q.isEmpty()) return false
        while (true) {
            while (!q.isEmpty()) {
                val u: Int = q.removeFirst()
                if (S[st[u]] == 1) continue
                for (v in 1..n) if (g[u][v]!!.w > 0 && st[u] != st[v]) {
                    if (e_delta(g[u][v]) == 0) {
                        if (on_found_edge(g[u][v])) return true
                    } else update_slack(u, st[v])
                }
            }
            var d = INF
            for (b in n + 1..n_x) if (st[b] == b && S[b] == 1) d = Math.min(d, lab[b] / 2)
            for (x in 1..n_x) if (st[x] == x && slack[x] != 0) {
                if (S[x] == -1) d = Math.min(d, e_delta(g[slack[x]][x])) else if (S[x] == 0) d =
                    Math.min(d, e_delta(g[slack[x]][x]) / 2)
            }
            for (u in 1..n) {
                if (S[st[u]] == 0) {
                    if (lab[u] <= d) return false
                    lab[u] -= d
                } else if (S[st[u]] == 1) lab[u] += d
            }
            for (b in n + 1..n_x) if (st[b] == b) {
                if (S[st[b]] == 0) lab[b] += d * 2 else if (S[st[b]] == 1) lab[b] -= d * 2
            }
            q = ArrayDeque()
            for (x in 1..n_x) if (st[x] == x && slack[x] != 0 && st[slack[x]] != x && e_delta(g[slack[x]][x]) == 0) if (on_found_edge(
                    g[slack[x]][x]
                )
            ) return true
            for (b in n + 1..n_x) if (st[b] == b && S[b] == 1 && lab[b] == 0) expand_blossom(b)
        }
    }

    class Result(var totalWeight: Long, var matches: Int)

    fun solve(): Result {
        //        memset(match + 1, 0, sizeof(int) * n);
        Arrays.fill(match, 1, n + 1, 0)
        n_x = n
        var n_matches = 0
        var tot_weight: Long = 0
        for (u in 0..n) {
            st[u] = u
            flower[u].clear()
        }
        var w_max = 0
        for (u in 1..n) for (v in 1..n) {
            flower_from[u][v] = if (u == v) u else 0
            w_max = Math.max(w_max, g[u][v]!!.w)
        }
        for (u in 1..n) lab[u] = w_max
        while (matching()) ++n_matches
        for (u in 1..n) if (match[u] != 0 && match[u] < u) tot_weight += g[u][match[u]]!!.w.toLong()
        return Result(tot_weight, n_matches)
    }

    fun add_edge(u: Int, v: Int, w: Int) {
        g[v][u]!!.w = w
        g[u][v]!!.w = g[v][u]!!.w
    }

    fun init(_n: Int) {
        n = _n
        for (u in 1..n) for (v in 1..n) g[u][v] = edge(u, v, 0)
    }

    companion object {
        // N^3 (but fast in practice)
        val INF: Int = Integer.MAX_VALUE
        const val N = 1032
        fun rotate(a: List<Integer?>?, first: Int, middle: Int, last: Int) {
            reverse(a, first, middle)
            reverse(a, middle, last)
            reverse(a, first, last)
        }

        fun reverse(a: List<Integer?>?, from: Int, to: Int) {
            var from = from
            var to = to
            while (from + 1 < to) swap(a, from++, --to)
        }

        fun swap(a: List<Integer?>?, i: Int, j: Int) {
            a.set(i, a.set(j, a!![i]))
        }

        var t = 0

        // usage example
        fun main(args: Array<String?>?) {
            val n = 4
            val mm = MaxGeneralWeightedMatchingV3()
            mm.init(n)
            mm.add_edge(1, 2, 3)
            mm.add_edge(1, 3, 4)
            mm.add_edge(4, 3, 4)
            val res = mm.solve()
            System.out.println(res.totalWeight.toString() + " " + res.matches)
        }
    }

    init {
        for (i in flower.indices) {
            flower[i] = ArrayList()
        }
    }
}
