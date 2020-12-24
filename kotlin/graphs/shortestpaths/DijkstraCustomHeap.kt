package graphs.shortestpaths

import java.util.stream.Stream

// https://en.wikipedia.org/wiki/Dijkstra's_algorithm
object DijkstraCustomHeap {
    // calculate shortest paths in O(E*log(V)) time and O(V) memory
    fun shortestPaths(edges: Array<List<Edge>>, s: Int, prio: LongArray, pred: IntArray) {
        Arrays.fill(pred, -1)
        Arrays.fill(prio, Long.MAX_VALUE)
        prio[s] = 0
        val h = BinaryHeap(edges.size)
        h.add(s, 0)
        while (h.size != 0) {
            val u = h.remove()
            for (e in edges[u]) {
                val v = e.t
                val nprio = prio[u] + e.cost
                if (prio[v] > nprio) {
                    if (prio[v] == Long.MAX_VALUE) h.add(v, nprio) else h.increasePriority(v, nprio)
                    prio[v] = nprio
                    pred[v] = u
                }
            }
        }
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val cost = arrayOf(intArrayOf(0, 3, 2), intArrayOf(0, 0, -2), intArrayOf(0, 0, 0))
        val n = cost.size
        val edges: Array<List<Edge>> = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        for (i in 0 until n) {
            for (j in 0 until n) {
                if (cost[i][j] != 0) {
                    edges[i].add(Edge(j, cost[i][j]))
                }
            }
        }
        val dist = LongArray(n)
        val pred = IntArray(n)
        shortestPaths(edges, 0, dist, pred)
        System.out.println(0 == dist[0])
        System.out.println(3 == dist[1])
        System.out.println(1 == dist[2])
        System.out.println(-1 == pred[0])
        System.out.println(0 == pred[1])
        System.out.println(1 == pred[2])
    }

    class Edge(var t: Int, var cost: Int)
    internal class BinaryHeap(n: Int) {
        var heap: LongArray
        var pos2Id: IntArray
        var id2Pos: IntArray
        var size = 0
        fun remove(): Int {
            val removedId = pos2Id[0]
            heap[0] = heap[--size]
            pos2Id[0] = pos2Id[size]
            id2Pos[pos2Id[0]] = 0
            down(0)
            return removedId
        }

        fun add(id: Int, value: Long) {
            heap[size] = value
            pos2Id[size] = id
            id2Pos[id] = size
            up(size++)
        }

        fun increasePriority(id: Int, value: Long) {
            heap[id2Pos[id]] = value
            up(id2Pos[id])
        }

        fun up(pos: Int) {
            var pos = pos
            while (pos > 0) {
                val parent = (pos - 1) / 2
                if (heap[pos] >= heap[parent]) break
                swap(pos, parent)
                pos = parent
            }
        }

        fun down(pos: Int) {
            var pos = pos
            while (true) {
                var child = 2 * pos + 1
                if (child >= size) break
                if (child + 1 < size && heap[child + 1] < heap[child]) ++child
                if (heap[pos] <= heap[child]) break
                swap(pos, child)
                pos = child
            }
        }

        fun swap(i: Int, j: Int) {
            val tt = heap[i]
            heap[i] = heap[j]
            heap[j] = tt
            val t = pos2Id[i]
            pos2Id[i] = pos2Id[j]
            pos2Id[j] = t
            id2Pos[pos2Id[i]] = i
            id2Pos[pos2Id[j]] = j
        }

        init {
            heap = LongArray(n)
            pos2Id = IntArray(n)
            id2Pos = IntArray(n)
        }
    }
}
