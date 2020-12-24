package structures

import numeric.FFT
import optimization.Simplex

// https://en.wikipedia.org/wiki/Binary_heap
// invariant: heap[parent] <= heap[child]
class BinaryHeap {
    var heap: IntArray
    var size = 0

    constructor(n: Int) {
        heap = IntArray(n)
    }

    // build heap in O(n)
    constructor(values: IntArray) {
        heap = values.clone()
        size = values.size
        for (pos in size / 2 - 1 downTo 0) down(pos)
    }

    fun removeMin(): Int {
        val removed = heap[0]
        heap[0] = heap[--size]
        down(0)
        return removed
    }

    fun add(value: Int) {
        heap[size] = value
        up(size++)
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
        val t = heap[i]
        heap[i] = heap[j]
        heap[j] = t
    }

    companion object {
        // random test
        fun main(args: Array<String?>?) {
            val rnd = Random(1)
            for (step in 0..999) {
                val n: Int = rnd.nextInt(100) + 1
                val q: PriorityQueue<Integer> = PriorityQueue()
                val h = BinaryHeap(n)
                for (op in 0..999) {
                    if (rnd.nextBoolean() && q.size() < n) {
                        val v: Int = rnd.nextInt()
                        q.add(v)
                        h.add(v)
                    } else if (!q.isEmpty()) {
                        if (q.remove() !== h.removeMin()) throw RuntimeException()
                    }
                }
            }
        }
    }
}
