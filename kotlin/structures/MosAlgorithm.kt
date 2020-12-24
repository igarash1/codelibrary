package structures

import numeric.FFT
import optimization.Simplex

// https://www.hackerearth.com/notes/mos-algorithm/
// Solution of http://www.spoj.com/problems/DQUERY/en/
object MosAlgorithm {
    fun add(a: IntArray, cnt: IntArray, i: Int): Int {
        return if (++cnt[a[i]] == 1) 1 else 0
    }

    fun remove(a: IntArray, cnt: IntArray, i: Int): Int {
        return if (--cnt[a[i]] == 0) -1 else 0
    }

    fun processQueries(a: IntArray, queries: Array<Query>): IntArray {
        for (i in queries.indices) queries[i].index = i
        val sqrtn = Math.sqrt(a.size) as Int
        Arrays.sort(
            queries,
            Comparator.< Query > comparingInt < structures . MosAlgorithm . Query ? > { q -> q.a / sqrtn }.thenComparingInt { q -> q.b })
        val cnt = IntArray(1000002)
        val res = IntArray(queries.size)
        var L = 1
        var R = 0
        var cur = 0
        for (query in queries) {
            while (L < query.a) cur += remove(a, cnt, L++)
            while (L > query.a) cur += add(a, cnt, --L)
            while (R < query.b) cur += add(a, cnt, ++R)
            while (R > query.b) cur += remove(a, cnt, R--)
            res[query.index] = cur
        }
        return res
    }

    fun main(args: Array<String?>?) {
        val a = intArrayOf(1, 3, 3, 4)
        val queries = arrayOf(Query(0, 3), Query(1, 3), Query(2, 3), Query(3, 3))
        val res = processQueries(a, queries)
        System.out.println(Arrays.toString(res))
    }

    class Query(var a: Int, var b: Int) {
        var index = 0
    }
}
