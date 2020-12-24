package sort

object Quicksort {
    var rnd: Random = Random()
    fun quickSort(a: IntArray, low: Int, high: Int) {
        if (low >= high) return
        val separator = a[low + rnd.nextInt(high - low + 1)]
        var i = low
        var j = high
        while (i <= j) {
            while (a[i] < separator) ++i
            while (a[j] > separator) --j
            if (i <= j) {
                val t = a[i]
                a[i] = a[j]
                a[j] = t
                ++i
                --j
            }
        }
        quickSort(a, low, j)
        quickSort(a, i, high)
    }

    // test
    fun main(args: Array<String?>?) {
        val n = 10000000
        val a1: IntArray = rnd.ints(n).toArray()
        val a2: IntArray = a1.clone()
        Arrays.sort(a2)
        val time: Long = System.currentTimeMillis()
        quickSort(a1, 0, a1.size - 1)
        System.out.println(System.currentTimeMillis() - time)
        if (!Arrays.equals(a1, a2)) throw RuntimeException()
    }
}
