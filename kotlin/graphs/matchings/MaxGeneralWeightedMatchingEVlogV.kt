package graphs.matchings

import java.util.Arrays

// Taken from https://gist.github.com/min-25/b984122f97dd7f72500e0bd6e49906ca
/*
  Maximum Weighted Matching in General Graphs.
  - O(nm \log(n)) time
  - O(n + m) space

  Note: each vertex is 1-indexed.

  Ref:
    Harold N. Gabow,
    "Data Structures for Weighted Matching and
     Extensions to b-matching and f-factors" (2016)
    (https://arxiv.org/abs/1611.07541)
*/
class MaxGeneralWeightedMatchingEVlogV(var N: Int, `in`: Array<InputEdge>) {
    class BinaryHeap<T : Comparable<T>?> {
        internal class Node<V : Comparable<V>?>(var value: V, var id: Int) : Comparable<Node<V>?> {
            @Override
            operator fun compareTo(o: Node<V>): Int {
                return value!!.compareTo(o.value)
            }
        }

        var size_ = 0
        var node: Array<Node<T?>?>
        var index: IntArray

        constructor() {}
        constructor(N: Int) {
            size_ = 0
            node = arrayOfNulls<Node<*>?>(N + 1)
            index = IntArray(N)
        }

        fun size(): Int {
            return size_
        }

        fun empty(): Boolean {
            return size_ == 0
        }

        fun clear() {
            while (size_ > 0) index[node[size_--]!!.id] = 0
        }

        fun min(): T? {
            return node[1]!!.value
        }

        fun argmin(): Int {
            return node[1]!!.id
        } // argmin ?

        fun get_val(id: Int): T? {
            return node[index[id]]!!.value
        }

        fun pop() {
            if (size_ > 0) pop(1)
        }

        fun erase(id: Int) {
            if (index[id] != 0) pop(index[id])
        }

        fun has(id: Int): Boolean {
            return index[id] != 0
        }

        fun update(id: Int, v: T) {
            if (!has(id)) {
                push(id, v)
                return
            }
            val up = v!!.compareTo(node[index[id]]!!.value) < 0
            node[index[id]]!!.value = v
            if (up) up_heap(index[id]) else down_heap(index[id])
        }

        fun decrease_key(id: Int, v: T) {
            if (!has(id)) {
                push(id, v)
                return
            }
            if (v!!.compareTo(node[index[id]]!!.value) < 0) {
                node[index[id]]!!.value = v
                up_heap(index[id])
            }
        }

        fun push(id: Int, v: T) {
            // assert(!has(id));
            index[id] = ++size_
            node[size_] = Node<Any?>(v, id)
            up_heap(size_)
        }

        fun pop(pos: Int) {
            index[node[pos]!!.id] = 0
            if (pos == size_) {
                --size_
                return
            }
            val up = node[size_]!!.value!!.compareTo(node[pos]!!.value) < 0
            node[pos] = node[size_--]
            index[node[pos]!!.id] = pos
            if (up) up_heap(pos) else down_heap(pos)
        }

        fun swap_node(a: Int, b: Int) {
            swap(node, a, b)
            index[node[a]!!.id] = a
            index[node[b]!!.id] = b
        }

        fun down_heap(pos: Int) {
            var k = pos
            var nk = k
            while (2 * k <= size_) {
                if (node[2 * k]!!.compareTo(node[nk]!!) < 0) nk = 2 * k
                if (2 * k + 1 <= size_ && node[2 * k + 1]!!.compareTo(node[nk]!!) < 0) nk = 2 * k + 1
                if (nk == k) break
                swap_node(k, nk)
                k = nk
            }
        }

        fun up_heap(pos: Int) {
            var k = pos
            while (k > 1 && node[k]!!.compareTo(node[k shr 1]!!) < 0) {
                swap_node(k, k shr 1)
                k = k shr 1
            }
        }
    }

    class PairingHeaps<Key : Comparable<Key>?>(H: Int, N: Int) {
        internal class Node<V : Comparable<V>?> {
            var key: V? = null
            var child = 0
            var next = 0
            var prev: Int

            constructor() {
                prev = -1
            } // "prev < 0" means the node is unused.

            constructor(v: V) {
                key = v
                child = 0
                next = 0
                prev = 0
            }
        }

        var heap: IntArray
        var node: Array<Node<Key?>>
        fun clear(h: Int) {
            if (heap[h] != 0) {
                clear_rec(heap[h])
                heap[h] = 0
            }
        }

        fun clear_all() {
            for (i in heap.indices) heap[i] = 0
            for (i in node.indices) node[i] = Node<Any?>()
        }

        fun empty(h: Int): Boolean {
            return heap[h] == 0
        }

        fun used(v: Int): Boolean {
            return node[v].prev >= 0
        }

        fun min(h: Int): Key? {
            return node[heap[h]].key
        }

        fun argmin(h: Int): Int {
            return heap[h]
        }

        fun pop(h: Int) {
            // assert(!empty(h));
            erase(h, heap[h])
        }

        fun push(h: Int, v: Int, key: Key) {
            // assert(!used(v));
            node[v] = Node<Any?>(key)
            heap[h] = merge(heap[h], v)
        }

        fun erase(h: Int, v: Int) {
            if (!used(v)) return
            val w = two_pass_pairing(node[v].child)
            if (node[v].prev == 0) heap[h] = w else {
                cut(v)
                heap[h] = merge(heap[h], w)
            }
            node[v].prev = -1
        }

        fun decrease_key(h: Int, v: Int, key: Key) {
            if (!used(v)) {
                push(h, v, key)
                return
            }
            if (node[v].prev == 0) node[v].key = key else {
                cut(v)
                node[v].key = key
                heap[h] = merge(heap[h], v)
            }
        }

        fun clear_rec(v: Int) {
            var v = v
            while (v != 0) {
                if (node[v].child != 0) clear_rec(node[v].child)
                node[v].prev = -1
                v = node[v].next
            }
        }

        fun cut(v: Int) {
            val n: Node<*> = node[v]
            val pv = n.prev
            val nv = n.next
            val pn: Node<*> = node[pv]
            if (pn.child == v) pn.child = nv else pn.next = nv
            node[nv].prev = pv
            n.prev = 0
            n.next = n.prev
        }

        fun merge(l: Int, r: Int): Int {
            var l = l
            var r = r
            if (l != 0) return r
            if (r != 0) return l
            if (node[l].key!!.compareTo(node[r].key) > 0) {
                val tmp = l
                l = r
                r = tmp
            }
            node[r].next = node[l].child
            val lc = node[r].next
            node[lc].prev = r
            node[l].child = node[lc].prev
            return l.also { node[r].prev = it }
        }

        fun two_pass_pairing(root: Int): Int {
            var root = root
            if (root == 0) return 0
            var a = root
            root = 0
            while (a != 0) {
                val b = node[a].next
                var na = 0
                node[a].next = 0
                node[a].prev = node[a].next
                if (b != 0) {
                    na = node[b].next
                    node[b].next = 0
                    node[b].prev = node[b].next
                }
                a = merge(a, b)
                node[a].next = root
                root = a
                a = na
            }
            var s = node[root].next
            node[root].next = 0
            while (s != 0) {
                val t = node[s].next
                node[s].next = 0
                root = merge(root, s)
                s = t
            }
            return root
        }

        init {
            // It consists of `H` Pairing heaps.
            // Each heap-node ID can appear at most 1 time(s) among heaps
            // and should be in [1, N).
            heap = IntArray(H)
            node = arrayOfNulls<Node<*>>(N)
            for (i in 0 until N) {
                node[i] = Node()
            }
        }
    }

    //    static  PriorityQueue : public priority_queue<T, vector<T>, greater<T> > {
    //        PriorityQueue() {}
    //
    //        PriorityQueue(int N) { this->c.reserve(N); }
    //
    //        T min() const { return this->top(); }
    //
    //        void clear() { this->c.clear(); }
    //    };
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

    class InputEdge(var from: Int, var to: Int, var cost: Int)

    //    template<typename T> using ModifiableHeap = BinaryHeap<T>;
    //    template<typename T> using ModifiableHeaps = PairingHeaps<T>;
    //    template<typename T> using FastHeap = PriorityQueue<T>;
    class Edge(var to: Int, var cost: Int)
    class Link(var from: Int, var to: Int)
    class Node {
        internal class NodeLink(var b: Int, var v: Int)

        var parent = 0
        var size = 0
        var link = arrayOfNulls<NodeLink>(2)

        constructor() {}
        constructor(u: Int) {
            parent = 0
            size = 1
            link[0] = NodeLink(u, u)
            link[1] = NodeLink(u, u)
        }

        fun next_v(): Int {
            return link[0]!!.v
        }

        fun next_b(): Int {
            return link[0]!!.b
        }

        fun prev_v(): Int {
            return link[1]!!.v
        }

        fun prev_b(): Int {
            return link[1]!!.b
        }
    }

    class Event {
        var time = 0
        var id = 0

        constructor() {}
        constructor(time: Int, id: Int) {
            this.time = time
            this.id = id
        } //        bool operator<(const Event &rhs) const { return time < rhs.time; }
        //        bool operator>(const Event &rhs) const { return time > rhs.time; }
    }

    class EdgeEvent : Comparable<EdgeEvent?> {
        var time = 0
        var from = 0
        var to = 0

        constructor() {}
        constructor(time: Int, from: Int, to: Int) {
            this.time = time
            this.from = from
            this.to = to
        }

        @Override
        operator fun compareTo(o: EdgeEvent): Int {
            return Integer.compare(time, o.time)
        } //        bool operator>(const EdgeEvent &rhs) const { return time > rhs.time; }
        //        bool operator<(const EdgeEvent &rhs) const { return time < rhs.time; }
    }

    var B: Int
    var S // N = |V|, B = (|V| - 1) / 2, S = N + B + 1
            : Int
    var ofs: IntArray
    var edges: Array<Edge?>
    var que: Queue? = null
    var mate: IntArray
    var surface: IntArray
    var base: IntArray
    var link: Array<Link?>
    var label: IntArray
    var potential: IntArray
    var unused_bid: IntArray
    var unused_bid_idx_ = 0
    var node: Array<Node?>

    // for O(nm log n) implementation
    var heavy: IntArray
    var group: IntArray
    var time_created: IntArray
    var lazy: IntArray
    var slack: IntArray
    var best_from: IntArray
    var time_current_ = 0
    var event1: Event? = null
    var heap2: BinaryHeap<EdgeEvent>
    var heap2s: PairingHeaps<EdgeEvent>
    var heap3: PriorityQueue<EdgeEvent>
    var heap4: BinaryHeap<Integer>
    fun maximum_weighted_matching(init_matching: Boolean /*= false*/): Long {
        initialize()
        set_potential()
        if (init_matching) find_maximal_matching()
        for (u in 1..N) if (mate[u] == 0) do_edmonds_search(u)
        return compute_optimal_value()
    }

    fun compute_optimal_value(): Long {
        var ret: Long = 0
        for (u in 1..N) if (mate[u] > u) {
            var max_c = 0
            for (eid in ofs[u] until ofs[u + 1]) {
                if (edges[eid]!!.to == mate[u]) max_c = Math.max(max_c, edges[eid]!!.cost)
            }
            ret += max_c.toLong()
        }
        return ret shr 1
    }

    fun reduced_cost(u: Int, v: Int, e: Edge?): Long {
        return (potential[u] + potential[v] - e!!.cost).toLong()
    }

    fun rematch(v: Int, w: Int) {
        val t = mate[v]
        mate[v] = w
        if (mate[t] != v) return
        if (link[v]!!.to == surface[link[v]!!.to]) {
            mate[t] = link[v]!!.from
            rematch(mate[t], t)
        } else {
            val x = link[v]!!.from
            val y = link[v]!!.to
            rematch(x, y)
            rematch(y, x)
        }
    }

    fun fix_mate_and_base(b: Int) {
        if (b <= N) return
        var bv = base[b]
        val mv1 = node[bv]!!.link[0]!!.v
        val bmv1 = node[bv]!!.link[0]!!.b
        val d = if (node[bmv1]!!.link[1]!!.v == mate[mv1]) 0 else 1
        while (true) {
            val mv = node[bv]!!.link[d]!!.v
            val bmv = node[bv]!!.link[d]!!.b
            if (node[bmv]!!.link[1 xor d]!!.v != mate[mv]) break
            fix_mate_and_base(bv)
            fix_mate_and_base(bmv)
            bv = node[bmv]!!.link[d]!!.b
        }
        fix_mate_and_base(bv.also { base[b] = it })
        mate[b] = mate[bv]
    }

    fun reset_time() {
        time_current_ = 0
        event1 = Event(Inf, 0)
    }

    fun reset_blossom(b: Int) {
        label[b] = kFree
        link[b]!!.from = 0
        slack[b] = Inf
        lazy[b] = 0
    }

    fun reset_all() {
        label[0] = kFree
        link[0]!!.from = 0
        for (v in 1..N) { // should be optimized for sparse graphs.
            if (label[v] == kOuter) potential[v] -= time_current_ else {
                val bv = surface[v]
                potential[v] += lazy[bv]
                if (label[bv] == kInner) potential[v] += time_current_ - time_created[bv]
            }
            reset_blossom(v)
        }
        var b = N + 1
        var r = B - unused_bid_idx_
        while (r > 0 && b < S) {
            if (base[b] != b) {
                if (surface[b] == b) {
                    fix_mate_and_base(b)
                    if (label[b] == kOuter) potential[b] += time_current_ - time_created[b] shl 1 else if (label[b] == kInner) fix_blossom_potential(
                        b,
                        kInner
                    ) else fix_blossom_potential(b, kFree)
                }
                heap2s.clear(b)
                reset_blossom(b)
                --r
            }
            ++b
        }
        que!!.clear()
        reset_time()
        heap2.clear()
        heap3.clear()
        heap4.clear()
    }

    fun do_edmonds_search(root: Int) {
        if (potential[root] == 0) return
        link_blossom(surface[root], Link(0, 0))
        push_outer_and_fix_potentials(surface[root], 0)
        var augmented = false
        while (!augmented) {
            augmented = augment(root)
            if (augmented) break
            augmented = adjust_dual_variables(root)
        }
        reset_all()
    }

    fun fix_blossom_potential(b: Int, Lab: Int): Int {
        // Return the amount.
        // (If v is an atom, the potential[v] will not be changed.)
        var d = lazy[b]
        lazy[b] = 0
        if (Lab == kInner) {
            val dt = time_current_ - time_created[b]
            if (b > N) potential[b] -= dt shl 1
            d += dt
        }
        return d
    }

    fun update_heap2(x: Int, y: Int, by: Int, t: Int, Lab: Int) {
        if (t >= slack[y]) return
        slack[y] = t
        best_from[y] = x
        if (y == by) {
            if (Lab != kInner) heap2.decrease_key(y, EdgeEvent(t + lazy[y], x, y))
        } else {
            val gy = group[y]
            if (gy != y) {
                if (t >= slack[gy]) return
                slack[gy] = t
            }
            heap2s.decrease_key(by, gy, EdgeEvent(t, x, y))
            if (Lab == kInner) return
            val m = heap2s.min(by)
            if (m != null) heap2.decrease_key(by, EdgeEvent(m.time + lazy[by], m.from, m.to))
        }
    }

    fun activate_heap2_node(b: Int) {
        if (b <= N) {
            if (slack[b] < Inf) heap2.push(
                b, EdgeEvent(
                    slack[b] + lazy[b], best_from[b], b
                )
            )
        } else {
            if (heap2s.empty(b)) return
            val m = heap2s.min(b)!!
            heap2.push(b, EdgeEvent(m.time + lazy[b], m.from, m.to))
        }
    }

    fun swap_blossom(a: Int, b: Int) {
        // Assume that `b` is a maximal blossom.
        swap(base, a, b)
        if (base[a] == a) base[a] = b
        swap(heavy, a, b)
        if (heavy[a] == a) heavy[a] = b
        swap(link, a, b)
        swap(mate, a, b)
        swap(potential, a, b)
        swap(lazy, a, b)
        swap(time_created, a, b)
        for (d in 0..1) node[node[a]!!.link[d]!!.b]!!.link[1 xor d]!!.b = b
        swap(node, a, b)
    }

    fun set_surface_and_group(b: Int, sf: Int, g: Int) {
        surface[b] = sf
        group[b] = g
        if (b <= N) return
        var bb = base[b]
        while (surface[bb] != sf) {
            set_surface_and_group(bb, sf, g)
            bb = node[bb]!!.next_b()
        }
    }

    fun merge_smaller_blossoms(bid: Int) {
        var lb = bid
        var largest_size = 1
        run {
            val beta = base[bid]
            var b = beta
            while (true) {
                if (node[b]!!.size > largest_size) {
                    largest_size = node[b]!!.size
                    lb = b
                }
                if (node[b]!!.next_b().also { b = it } == beta) break
            }
        }
        val beta = base[bid]
        var b = beta
        while (true) {
            if (b != lb) set_surface_and_group(b, lb, b)
            if (node[b]!!.next_b().also { b = it } == beta) break
        }
        group[lb] = lb
        if (largest_size > 1) {
            heavy[bid] = lb
            surface[bid] = heavy[bid]
            swap_blossom(lb, bid)
        } else heavy[bid] = 0
    }

    fun contract(x: Int, y: Int, eid: Int) {
        var x = x
        var y = y
        var bx = surface[x]
        var by = surface[y]
        assert(bx != by)
        val h = -(eid + 1)
        link[surface[mate[by]]]!!.from = h
        link[surface[mate[bx]]]!!.from = link[surface[mate[by]]]!!.from
        var lca = -1
        while (true) {
            if (mate[by] != 0) {
                val tmp = bx
                bx = by
                by = tmp
            }
            lca = surface[link[bx]!!.from]
            bx = lca
            if (link[surface[mate[bx]]]!!.from == h) break
            link[surface[mate[bx]]]!!.from = h
        }
        val bid = unused_bid[--unused_bid_idx_]
        assert(unused_bid_idx_ >= 0)
        var tree_size = 0
        for (d in 0..1) {
            var bv = surface[x]
            while (bv != lca) {
                val mv = mate[bv]
                val bmv = surface[mv]
                val v = mate[mv]
                val f = link[v]!!.from
                val t = link[v]!!.to
                tree_size += node[bv]!!.size + node[bmv]!!.size
                link[mv] = Link(x, y)
                if (bv > N) potential[bv] += time_current_ - time_created[bv] shl 1
                if (bmv > N) heap4.erase(bmv)
                push_outer_and_fix_potentials(bmv, fix_blossom_potential(bmv, kInner))
                node[bv]!!.link[d] = Node.NodeLink(bmv, mv)
                node[bmv]!!.link[1 xor d] = Node.NodeLink(bv, v)
                node[bmv]!!.link[d] = Node.NodeLink(surface[f].also { bv = it }, f)
                node[bv]!!.link[1 xor d] = Node.NodeLink(bmv, t)
            }
            node[surface[x]]!!.link[1 xor d] = Node.NodeLink(surface[y], y)
            val tmp = x
            x = y
            y = tmp
        }
        if (lca > N) potential[lca] += time_current_ - time_created[lca] shl 1
        node[bid]!!.size = tree_size + node[lca]!!.size
        base[bid] = lca
        link[bid] = link[lca]
        mate[bid] = mate[lca]
        label[bid] = kOuter
        surface[bid] = bid
        time_created[bid] = time_current_
        potential[bid] = 0
        lazy[bid] = 0
        merge_smaller_blossoms(bid) // O(n log n) time / Edmonds search
    }

    fun link_blossom(v: Int, l: Link) {
        var l = l
        link[v] = Link(l.from, l.to)
        if (v <= N) return
        val b = base[v]
        link_blossom(b, l)
        val pb = node[b]!!.prev_b()
        l = Link(node[pb]!!.next_v(), node[b]!!.prev_v())
        var bv = b
        while (true) {
            val bw = node[bv]!!.next_b()
            if (bw == b) break
            link_blossom(bw, l)
            val nl = Link(
                node[bw]!!.prev_v(), node[bv]!!.next_v()
            )
            bv = node[bw]!!.next_b()
            link_blossom(bv, nl)
        }
    }

    fun push_outer_and_fix_potentials(v: Int, d: Int) {
        label[v] = kOuter
        if (v > N) {
            var b = base[v]
            while (label[b] != kOuter) {
                push_outer_and_fix_potentials(b, d)
                b = node[b]!!.next_b()
            }
        } else {
            potential[v] += time_current_ + d
            if (potential[v] < event1!!.time) event1 = Event(potential[v], v)
            que!!.enqueue(v)
        }
    }

    fun grow(x: Int, y: Int): Boolean {
        val by = surface[y]
        val visited = label[by] != kFree
        if (!visited) link_blossom(by, Link(0, 0))
        label[by] = kInner
        time_created[by] = time_current_
        heap2.erase(by)
        if (y != by) heap4.update(by, time_current_ + (potential[by] shr 1))
        val z = mate[by]
        if (z == 0) {
            rematch(x, y)
            rematch(y, x)
            return true
        }
        val bz = surface[z]
        if (!visited) link_blossom(bz, Link(x, y)) else {
            link[z] = Link(x, y)
            link[bz] = link[z]
        }
        push_outer_and_fix_potentials(bz, fix_blossom_potential(bz, kFree))
        time_created[bz] = time_current_
        heap2.erase(bz)
        return false
    }

    fun free_blossom(bid: Int) {
        unused_bid[unused_bid_idx_++] = bid
        base[bid] = bid
    }

    fun recalculate_minimum_slack(b: Int, g: Int): Int {
        // Return the destination of the best edge of blossom `g`.
        if (b <= N) {
            if (slack[b] >= slack[g]) return 0
            slack[g] = slack[b]
            best_from[g] = best_from[b]
            return b
        }
        var v = 0
        val beta = base[b]
        var bb = beta
        while (true) {
            val w = recalculate_minimum_slack(bb, g)
            if (w != 0) v = w
            if (node[bb]!!.next_b().also { bb = it } == beta) break
        }
        return v
    }

    fun construct_smaller_components(b: Int, sf: Int, g: Int) {
        surface[b] = sf
        group[b] = g // `group[b] = g` is unneeded.
        if (b <= N) return
        var bb = base[b]
        while (surface[bb] != sf) {
            if (bb == heavy[b]) {
                construct_smaller_components(bb, sf, g)
            } else {
                set_surface_and_group(bb, sf, bb)
                var to = 0
                if (bb > N) {
                    slack[bb] = Inf
                    to = recalculate_minimum_slack(bb, bb)
                } else if (slack[bb] < Inf) to = bb
                if (to > 0) heap2s.push(sf, bb, EdgeEvent(slack[bb], best_from[bb], to))
            }
            bb = node[bb]!!.next_b()
        }
    }

    fun move_to_largest_blossom(bid: Int) {
        var bid = bid
        val h = heavy[bid]
        val d = time_current_ - time_created[bid] + lazy[bid]
        lazy[bid] = 0
        val beta = base[bid]
        var b = beta
        while (true) {
            time_created[b] = time_current_
            lazy[b] = d
            if (b != h) {
                construct_smaller_components(b, b, b)
                heap2s.erase(bid, b)
            }
            if (node[b]!!.next_b().also { b = it } == beta) break
        }
        if (h > 0) {
            swap_blossom(h, bid)
            bid = h
        }
        free_blossom(bid)
    }

    fun expand(bid: Int) {
        val mv = mate[base[bid]]
        move_to_largest_blossom(bid) // O(n log n) time / Edmonds search
        val old_link = link[mv]
        val old_base = surface[mate[mv]]
        val root = surface[old_link!!.to]
        val d = if (mate[root] == node[root]!!.link[0]!!.v) 1 else 0
        run {
            var b = node[old_base]!!.link[d xor 1]!!.b
            while (b != root) {
                label[b] = kSeparated
                activate_heap2_node(b)
                b = node[b]!!.link[d xor 1]!!.b
                label[b] = kSeparated
                activate_heap2_node(b)
                b = node[b]!!.link[d xor 1]!!.b
            }
        }
        var b = old_base
        while (true) {
            label[b] = kInner
            val nb = node[b]!!.link[d]!!.b
            if (b == root) link[mate[b]] = old_link else link[mate[b]] = Link(
                node[b]!!.link[d]!!.v, node[nb]!!.link[d xor 1]!!.v
            )
            link[surface[mate[b]]] = link[mate[b]] // fix tree links
            if (b > N) {
                if (potential[b] == 0) expand(b) else heap4.push(b, time_current_ + (potential[b] shr 1))
            }
            if (b == root) break
            push_outer_and_fix_potentials(nb, fix_blossom_potential(nb.also { b = it }, kInner))
            b = node[b]!!.link[d]!!.b
        }
    }

    fun augment(root: Int): Boolean {
        // Return true if an augmenting path is found.
        while (!que!!.empty()) {
            val x = que!!.dequeue()
            var bx = surface[x]
            if (potential[x] == time_current_) {
                if (x != root) rematch(x, 0)
                return true
            }
            for (eid in ofs[x] until ofs[x + 1]) {
                val e = edges[eid]
                val y = e!!.to
                val by = surface[y]
                if (bx == by) continue
                val l = label[by]
                if (l == kOuter) {
                    val t = reduced_cost(x, y, e) shr 1 // < 2 * Inf
                    if (t == time_current_.toLong()) {
                        contract(x, y, eid)
                        bx = surface[x]
                    } else if (t < event1!!.time) {
                        heap3.add(EdgeEvent(t.toInt(), x, eid))
                    }
                } else {
                    val t = reduced_cost(x, y, e) // < 3 * Inf
                    if (t >= Inf) continue
                    if (l != kInner) {
                        if (t + lazy[by] == time_current_.toLong()) {
                            if (grow(x, y)) return true
                        } else update_heap2(x, y, by, t.toInt(), kFree)
                    } else {
                        if (mate[x] != y) update_heap2(x, y, by, t.toInt(), kInner)
                    }
                }
            }
        }
        return false
    }

    fun adjust_dual_variables(root: Int): Boolean {
        // delta1 : rematch
        val time1 = event1!!.time

        // delta2 : grow
        var time2 = Inf
        if (!heap2.empty()) time2 = heap2.min()!!.time

        // delta3 : contract : O(m log n) time / Edmonds search [ bottleneck (?) ]
        var time3 = Inf
        while (!heap3.isEmpty()) {
            val e: EdgeEvent = heap3.peek()
            val x = e.from
            val y = edges[e.to]!!.to // e.to is some edge id.
            if (surface[x] != surface[y]) {
                time3 = e.time
                break
            } else heap3.remove()
        }

        // delta4 : expand
        var time4 = Inf
        if (!heap4.empty()) time4 = heap4.min()

        // -- events --
        val time_next: Int = Math.min(Math.min(time1, time2), Math.min(time3, time4))
        assert(time_current_ <= time_next && time_next < Inf)
        time_current_ = time_next
        if (time_current_ == event1!!.time) {
            val x = event1!!.id
            if (x != root) rematch(x, 0)
            return true
        }
        while (!heap2.empty() && heap2.min()!!.time == time_current_) {
            val x = heap2.min()!!.from
            val y = heap2.min()!!.to
            if (grow(x, y)) return true // `grow` function will call `heap2.erase(by)`.
        }
        while (!heap3.isEmpty() && heap3.peek().time === time_current_) {
            val x: Int = heap3.peek().from
            val eid: Int = heap3.peek().to
            val y = edges[eid]!!.to
            heap3.remove()
            if (surface[x] == surface[y]) continue
            contract(x, y, eid)
        }
        while (!heap4.empty() && heap4.min() == time_current_) {
            val b = heap4.argmin()
            heap4.pop()
            expand(b)
        }
        return false
    }

    fun initialize() {
        que = Queue(N)
        mate = IntArray(S)
        link = arrayOfNulls(S)
        for (i in 0 until S) {
            link[i] = Link(0, 0)
        }
        label = IntArray(S)
        Arrays.fill(label, kFree)
        base = IntArray(S)
        for (u in 1 until S) base[u] = u
        surface = IntArray(S)
        for (u in 1 until S) surface[u] = u
        potential = IntArray(S)
        node = arrayOfNulls(S)
        for (b in 1 until S) node[b] = Node(b)
        unused_bid = IntArray(B)
        for (i in 0 until B) unused_bid[i] = N + B - i
        unused_bid_idx_ = B

        // for O(nm log n) implementation
        reset_time()
        time_created = IntArray(S)
        slack = IntArray(S)
        for (i in 0 until S) slack[i] = Inf
        best_from = IntArray(S)
        heavy = IntArray(S)
        lazy = IntArray(S)
        group = IntArray(S)
        for (i in 0 until S) group[i] = i
    }

    fun set_potential() {
        for (u in 1..N) {
            var max_c = 0
            for (eid in ofs[u] until ofs[u + 1]) {
                max_c = Math.max(max_c, edges[eid]!!.cost)
            }
            potential[u] = max_c shr 1
        }
    }

    fun find_maximal_matching() {
        // Find a maximal matching naively.
        for (u in 1..N) if (mate[u] == 0) {
            for (eid in ofs[u] until ofs[u + 1]) {
                val e = edges[eid]
                val v = e!!.to
                if (mate[v] > 0 || reduced_cost(u, v, e) > 0) continue
                mate[u] = v
                mate[v] = u
                break
            }
        }
    }

    companion object {
        const val kSeparated = -2
        const val kInner = -1
        const val kFree = 0
        const val kOuter = 1
        var Inf = 1 shl 30
        fun swap(a: IntArray, i: Int, j: Int) {
            val t = a[i]
            a[i] = a[j]
            a[j] = t
        }

        fun swap(a: Array<Object?>, i: Int, j: Int) {
            val t: Object? = a[i]
            a[i] = a[j]
            a[j] = t
        }

        // usage example
        fun main(args: Array<String?>?) {
            val n = 6
            val edges = arrayOf(
                InputEdge(1, 4, 1), InputEdge(1, 5, 1), InputEdge(1, 5, 1),
                InputEdge(2, 4, 2), InputEdge(2, 5, 3), InputEdge(2, 6, 1), InputEdge(3, 4, 1),
                InputEdge(3, 5, 1), InputEdge(3, 6, 1)
            )
            val mm = MaxGeneralWeightedMatchingEVlogV(n, edges)
            System.out.println(mm.maximum_weighted_matching(false))
        }
    }

    init {
        B = (N - 1) / 2
        S = N + B + 1
        ofs = IntArray(N + 2)
        edges = arrayOfNulls(`in`.size * 2)
        heap2 = BinaryHeap(S)
        heap2s = PairingHeaps(S, S)
        heap3 = PriorityQueue(edges.size)
        heap4 = BinaryHeap<Integer>(S)
        for (e in `in`) {
            ofs[e.from + 1]++
            ofs[e.to + 1]++
        }
        for (i in 1..N + 1) ofs[i] += ofs[i - 1]
        for (e in `in`) {
            edges[ofs[e.from]++] = Edge(e.to, e.cost * 2)
            edges[ofs[e.to]++] = Edge(e.from, e.cost * 2)
        }
        for (i in N + 1 downTo 1) ofs[i] = ofs[i - 1]
        ofs[0] = 0
    }
}
