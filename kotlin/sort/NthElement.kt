package sort

import java.util.stream.IntStream

object NthElement {
    // See: http://www.cplusplus.com/reference/algorithm/nth_element
    // O(n) on average
    fun nth_element(a: IntArray, low: Int, high: Int, n: Int, rnd: Random) {
        var low = low
        var high = high
        while (true) {
            val k = partition(a, low, high, low + rnd.nextInt(high - low))
            if (n < k) high = k else if (n > k) low = k + 1 else return
        }
    }

    fun partition(a: IntArray, fromInclusive: Int, toExclusive: Int, separatorIndex: Int): Int {
        var i = fromInclusive
        var j = toExclusive - 1
        val separator = a[separatorIndex]
        swap(a, i++, separatorIndex)
        while (i <= j) {
            while (i <= j && a[i] < separator) ++i
            while (i <= j && a[j] > separator) --j
            if (i >= j) break
            swap(a, i++, j--)
        }
        swap(a, j, fromInclusive)
        return j
    }

    fun swap(a: IntArray, i: Int, j: Int) {
        val t = a[j]
        a[j] = a[i]
        a[i] = t
    }

    // O(n) worst case. See Cormen et al
    fun nth_element2(a: IntArray, low: Int, high: Int, n: Int) {
        var low = low
        var high = high
        if (high - low <= 1) return
        while (true) {
            val a5 = IntArray((high - low + 4) / 5)
            var i = low
            var cnt = 0
            while (i < high) {
                val j: Int = Math.min(i + 5, high)
                for (iteration in 0..2) {
                    var k = i
                    while (k + 1 < j) {
                        if (a[k] > a[k + 1]) {
                            val t = a[k]
                            a[k] = a[k + 1]
                            a[k + 1] = t
                        }
                        k++
                    }
                }
                a5[cnt++] = a[i + j ushr 1]
                i += 5
            }
            nth_element2(a5, 0, a5.size, a5.size / 2)
            val separatorIndex: Int =
                IntStream.range(low, high).filter { i -> a[i] == a5[a5.size / 2] }.findFirst().getAsInt()
            val k = partition(a, low, high, separatorIndex)
            if (n < k) high = k else if (n > k) low = k + 1 else return
        }
    }

    // Random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        val len = 1000000
        nth_element(IntArray(len), 0, len, 0, rnd)
        nth_element2(IntArray(len), 0, len, 0)
        for (step in 0..99999) {
            val n: Int = rnd.nextInt(10) + 1
            val a: IntArray = rnd.ints(n, 0, 10).toArray()
            val b: IntArray = a.clone()
            val k: Int = rnd.nextInt(n)
            nth_element(a, 0, n, k, rnd)
            nth_element2(b, 0, n, k)
            val sa: IntArray = a.clone()
            Arrays.sort(sa)
            val sb: IntArray = b.clone()
            Arrays.sort(sb)
            if (!Arrays.equals(sa, sb)) throw RuntimeException()
            if (a[k] != sa[k] || b[k] != sb[k]) throw RuntimeException()
            for (i in 0 until n) {
                if (i < k && a[i] > a[k] || i > k && a[i] < a[k]) throw RuntimeException()
                if (i < k && b[i] > b[k] || i > k && b[i] < b[k]) throw RuntimeException()
            }
        }
    }
}
