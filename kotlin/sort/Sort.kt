package sort

import java.util.function.IntPredicate

object Sort {
    var rnd: Random = Random(1)
    fun qSort(a: IntArray, low: Int, high: Int) {
        if (low >= high) return
        val separator = a[low + rnd.nextInt(high - low + 1)]
        var i = low
        var j = high
        do {
            while (a[i] < separator) ++i
            while (a[j] > separator) --j
            if (i > j) break
            val t = a[i]
            a[i] = a[j]
            a[j] = t
            ++i
            --j
        } while (i <= j)
        qSort(a, low, j)
        qSort(a, i, high)
    }

    fun mergeSort(a: IntArray, low: Int, high: Int) {
        if (high - low < 2) return
        val mid = low + high ushr 1
        mergeSort(a, low, mid)
        mergeSort(a, mid, high)
        val b: IntArray = Arrays.copyOfRange(a, low, mid)
        var i = low
        var j = mid
        var k = 0
        while (k < b.size) {
            if (j == high || b[k] <= a[j]) {
                a[i] = b[k++]
            } else {
                a[i] = a[j++]
            }
            i++
        }
    }

    fun mergeSort2(a: IntArray, low: Int, high: Int) {
        val size = high - low
        if (size < 2) return
        val mid = low + high ushr 1
        mergeSort2(a, low, mid)
        mergeSort2(a, mid, high)
        val b = IntArray(size)
        var i = low
        var j = mid
        for (k in 0 until size) {
            if (i < mid && (j == high || a[i] <= a[j])) {
                b[k] = a[i++]
            } else {
                b[k] = a[j++]
            }
        }
        System.arraycopy(b, 0, a, low, size)
    }

    fun inPlaceMergeSort(a: IntArray, low: Int, high: Int) {
        if (low < high - 1) {
            val mid = low + high ushr 1
            mergeSort(a, low, mid)
            mergeSort(a, mid, high)
            inPlaceMerge(a, low, mid, high)
        }
    }

    // O(n*log(n)) complexity
    fun inPlaceMerge(a: IntArray, from: Int, mid: Int, to: Int) {
        var mid = mid
        if (from >= mid || mid >= to) return
        if (to - from == 2) {
            if (a[from] > a[mid]) swap(a, from, mid)
            return
        }
        val firstCut: Int
        val secondCut: Int
        if (mid - from > to - mid) {
            firstCut = from + (mid - from) / 2
            secondCut = binarySearchFirstTrue(IntPredicate { i -> a[i] >= a[firstCut] }, mid, to)
        } else {
            secondCut = mid + (to - mid) / 2
            firstCut = binarySearchFirstTrue(IntPredicate { i -> a[i] > a[secondCut] }, from, mid)
        }
        if (mid != firstCut && mid != secondCut) {
            rotate(a, firstCut, mid, secondCut)
        }
        mid = firstCut + (secondCut - mid)
        inPlaceMerge(a, from, firstCut, mid)
        inPlaceMerge(a, mid, secondCut, to)
    }

    fun swap(a: IntArray, i: Int, j: Int) {
        val t = a[j]
        a[j] = a[i]
        a[i] = t
    }

    fun rotate(a: IntArray, first: Int, middle: Int, last: Int) {
        var first = first
        var middle = middle
        var next = middle
        while (first != next) {
            swap(a, first++, next++)
            if (next == last) next = middle else if (first == middle) middle = next
        }
    }

    fun binarySearchFirstTrue(predicate: IntPredicate, fromInclusive: Int, toExclusive: Int): Int {
        var lo = fromInclusive - 1
        var hi = toExclusive
        while (lo < hi - 1) {
            val mid = lo + hi ushr 1
            if (!predicate.test(mid)) {
                lo = mid
            } else {
                hi = mid
            }
        }
        return hi
    }

    fun heapSort(a: IntArray) {
        var n = a.size
        for (i in n / 2 - 1 downTo 0) pushDown(a, i, n)
        while (n > 1) {
            swap(a, 0, n - 1)
            pushDown(a, 0, --n)
        }
    }

    fun pushDown(h: IntArray, pos: Int, size: Int) {
        var pos = pos
        while (true) {
            var child = 2 * pos + 1
            if (child >= size) break
            if (child + 1 < size && h[child + 1] > h[child]) child++
            if (h[pos] >= h[child]) break
            swap(h, pos, child)
            pos = child
        }
    }

    fun bubbleSort(a: IntArray) {
        var i = 0
        while (i + 1 < a.size) {
            var j = 0
            while (j + 1 < a.size) {
                if (a[j] > a[j + 1]) {
                    swap(a, j, j + 1)
                }
                j++
            }
            i++
        }
    }

    fun selectionSort(a: IntArray) {
        val n = a.size
        val p = IntArray(n)
        for (i in 0 until n) p[i] = i
        for (i in 0 until n - 1) {
            for (j in i + 1 until n) {
                if (a[p[i]] > a[p[j]]) {
                    swap(p, i, j)
                }
            }
        }
        val b: IntArray = a.clone()
        for (i in 0 until n) a[i] = b[p[i]]
    }

    fun insertionSort(a: IntArray) {
        for (i in 1 until a.size) {
            for (j in i downTo 1) {
                if (a[j - 1] > a[j]) {
                    swap(a, j - 1, j)
                }
            }
        }
    }

    fun countingSort(a: IntArray) {
        var max = 0
        for (x in a) {
            max = Math.max(max, x)
        }
        val cnt = IntArray(max + 1)
        for (x in a) {
            ++cnt[x]
        }
        for (i in 1 until cnt.size) {
            cnt[i] += cnt[i - 1]
        }
        val n = a.size
        val b = IntArray(n)
        for (i in 0 until n) {
            b[--cnt[a[i]]] = a[i]
        }
        System.arraycopy(b, 0, a, 0, n)
    }

    fun radixSort(a: IntArray) {
        val d = 8
        val w = 32
        val t = IntArray(a.size)
        for (p in 0 until w / d) {
            // counting-sort
            val cnt = IntArray(1 shl d)
            for (i in a.indices) ++cnt[a[i] xor Integer.MIN_VALUE ushr d * p and (1 shl d) - 1]
            for (i in 1 until cnt.size) cnt[i] += cnt[i - 1]
            for (i in a.indices.reversed()) t[--cnt[a[i] xor Integer.MIN_VALUE ushr d * p and (1 shl d) - 1]] = a[i]
            System.arraycopy(t, 0, a, 0, a.size)
        }
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999) {
            val n: Int = rnd.nextInt(10) + 1
            val a: IntArray = rnd.ints(n, 0, 1000).toArray()
            val s: IntArray = a.clone()
            Arrays.sort(s)
            var b: IntArray = a.clone()
            bubbleSort(b)
            if (!Arrays.equals(s, b)) throw RuntimeException()
            b = a.clone()
            selectionSort(b)
            if (!Arrays.equals(s, b)) throw RuntimeException()
            b = a.clone()
            insertionSort(b)
            if (!Arrays.equals(s, b)) throw RuntimeException()
            b = a.clone()
            countingSort(b)
            if (!Arrays.equals(s, b)) throw RuntimeException()
            b = a.clone()
            qSort(b, 0, b.size - 1)
            if (!Arrays.equals(s, b)) throw RuntimeException()
        }
        for (step in 0..9) {
            val n: Int = rnd.nextInt(50000) + 100000
            val a = if (step == 0) IntArray(n) else rnd.ints(n).toArray()
            val s: IntArray = a.clone()
            Arrays.sort(s)
            var b: IntArray = a.clone()
            qSort(b, 0, b.size - 1)
            if (!Arrays.equals(s, b)) throw RuntimeException()
            b = a.clone()
            mergeSort(b, 0, b.size)
            if (!Arrays.equals(s, b)) throw RuntimeException()
            b = a.clone()
            mergeSort2(b, 0, b.size)
            if (!Arrays.equals(s, b)) throw RuntimeException()
            b = a.clone()
            inPlaceMergeSort(b, 0, b.size)
            if (!Arrays.equals(s, b)) throw RuntimeException()
            b = a.clone()
            heapSort(b)
            if (!Arrays.equals(s, b)) throw RuntimeException()
            b = a.clone()
            radixSort(b)
            if (!Arrays.equals(s, b)) throw RuntimeException()
        }
    }
}
