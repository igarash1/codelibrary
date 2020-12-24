package structures

// https://en.wikipedia.org/wiki/Binary_heap
// invariant: heap[parent] <= heap[child]
class BinaryHeapExtended(n: Int) {
    var heap: LongArray
    var pos2Id: IntArray
    var id2Pos: IntArray
    var size = 0
    fun add(id: Int, value: Long) {
        heap[size] = value
        pos2Id[size] = id
        id2Pos[id] = size
        up(size++)
    }

    fun removeMin(): Int {
        val removedId = pos2Id[0]
        heap[0] = heap[--size]
        pos2Id[0] = pos2Id[size]
        id2Pos[pos2Id[0]] = 0
        down(0)
        return removedId
    }

    fun removeMin(id: Int) {
        val pos = id2Pos[id]
        pos2Id[pos] = pos2Id[--size]
        id2Pos[pos2Id[pos]] = pos
        changePriority(pos2Id[pos], heap[size])
    }

    fun changePriority(id: Int, value: Long) {
        val pos = id2Pos[id]
        if (heap[pos] < value) {
            heap[pos] = value
            down(pos)
        } else if (heap[pos] > value) {
            heap[pos] = value
            up(pos)
        }
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

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val heap = BinaryHeapExtended(10)
            heap.add(0, 4)
            heap.add(1, 5)
            heap.add(2, 2)
            heap.changePriority(1, 3)
            heap.changePriority(2, 6)
            heap.removeMin(0)

            // print elements in sorted order
            while (heap.size != 0) {
                System.out.println(heap.heap[0].toString() + " " + heap.removeMin())
            }
        }
    }

    init {
        heap = LongArray(n)
        pos2Id = IntArray(n)
        id2Pos = IntArray(n)
    }
}
