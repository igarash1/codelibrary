package structures

import java.util.Random

object MergeableHeap {
    var random: Random = Random()
    fun merge(a: Heap?, b: Heap?): Heap {
        var a = a
        var b = b
        if (a == null) return b!!
        if (b == null) return a
        if (a.value > b.value) {
            val t: Heap = a
            a = b
            b = t
        }
        if (random.nextBoolean()) {
            val t = a.left
            a.left = a.right
            a.right = t
        }
        a.left = merge(a.left, b)
        return a
    }

    fun add(h: Heap?, value: Int): Heap {
        return merge(h, Heap(value))
    }

    fun removeMin(h: Heap): HeapAndResult {
        return HeapAndResult(merge(h.left, h.right), h.value)
    }

    // Usage example
    fun main(args: Array<String?>?) {
        var h: Heap? = null
        h = add(h, 3)
        h = add(h, 1)
        h = add(h, 2)
        while (h != null) {
            val hv = removeMin(h)
            System.out.println(hv.value)
            h = hv.heap
        }
    }

    class Heap internal constructor(var value: Int) {
        var left: Heap? = null
        var right: Heap? = null
    }

    class HeapAndResult internal constructor(var heap: Heap, var value: Int)
}
