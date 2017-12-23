class SegmentTreeIntervalAddMax(val n: Int) {
    val tmax = IntArray(4 * n)
    val tadd = IntArray(4 * n) // tadd[i] applies to tmax[i], tadd[2*i+1] and tadd[2*i+2]

    fun max(from: Int, to: Int, root: Int = 0, left: Int = 0, right: Int = n - 1): Int {
        if (from == left && to == right) {
            return tmax[root] + tadd[root]
        }
        push(root)
        val mid = left + right shr 1
        var res = Int.MIN_VALUE
        if (from <= mid)
            res = maxOf(res, max(from, minOf(to, mid), 2 * root + 1, left, mid))
        if (to > mid)
            res = maxOf(res, max(maxOf(from, mid + 1), to, 2 * root + 2, mid + 1, right))
        return res
    }

    fun add(from: Int, to: Int, delta: Int, root: Int = 0, left: Int = 0, right: Int = n - 1) {
        if (from == left && to == right) {
            tadd[root] += delta
            return
        }
        push(root) // push can be skipped for add, but is necessary for other operations such as set
        val mid = left + right shr 1
        if (from <= mid)
            add(from, minOf(to, mid), delta, 2 * root + 1, left, mid)
        if (to > mid)
            add(maxOf(from, mid + 1), to, delta, 2 * root + 2, mid + 1, right)
        tmax[root] = maxOf(tmax[2 * root + 1] + tadd[2 * root + 1], tmax[2 * root + 2] + tadd[2 * root + 2])
    }

    fun push(root: Int) {
        tmax[root] += tadd[root]
        tadd[2 * root + 1] += tadd[root]
        tadd[2 * root + 2] += tadd[root]
        tadd[root] = 0
    }
}

// tests
fun main(args: Array<String>) {
    val t = SegmentTreeIntervalAddMax(10)
    t.add(0, 9, 1)
    t.add(2, 4, 2)
    t.add(3, 5, 3)
    println(t.max(0, 9))
    println(t.tmax[0] + t.tadd[0])
    println(t.max(0, 0))
}