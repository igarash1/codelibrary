package misc

import numeric.FFT
import optimization.Simplex

object MaximumZeroSubmatrix {
    fun maximumZeroSubmatrix(a: Array<IntArray>): Int {
        val R = a.size
        val C: Int = a[0].length
        var res = 0
        val d = IntArray(C)
        Arrays.fill(d, -1)
        val d1 = IntArray(C)
        val d2 = IntArray(C)
        val st = IntArray(C)
        for (r in 0 until R) {
            for (c in 0 until C) if (a[r][c] == 1) d[c] = r
            var size = 0
            for (c in 0 until C) {
                while (size > 0 && d[st[size - 1]] <= d[c]) --size
                d1[c] = if (size == 0) -1 else st[size - 1]
                st[size++] = c
            }
            size = 0
            for (c in C - 1 downTo 0) {
                while (size > 0 && d[st[size - 1]] <= d[c]) --size
                d2[c] = if (size == 0) C else st[size - 1]
                st[size++] = c
            }
            for (j in 0 until C) res = Math.max(res, (r - d[j]) * (d2[j] - d1[j] - 1))
        }
        return res
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..999) {
            val R: Int = rnd.nextInt(10) + 1
            val C: Int = rnd.nextInt(10) + 1
            val a = Array(R) { IntArray(C) }
            for (r in 0 until R) for (c in 0 until C) a[r][c] = rnd.nextInt(2)
            val res1 = maximumZeroSubmatrix(a)
            val res2 = slowMaximumZeroSubmatrix(a)
            if (res1 != res2) throw RuntimeException("$res1 $res2")
        }
    }

    fun slowMaximumZeroSubmatrix(a: Array<IntArray>): Int {
        var res = 0
        val R = a.size
        val C: Int = a[0].length
        for (r2 in 0 until R) for (c2 in 0 until C) for (r1 in 0..r2) m1@ for (c1 in 0..c2) {
            for (r in r1..r2) for (c in c1..c2) if (a[r][c] != 0) continue@m1
            res = Math.max(res, (r2 - r1 + 1) * (c2 - c1 + 1))
        }
        return res
    }
}
