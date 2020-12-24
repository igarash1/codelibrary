package structures

import numeric.FFT
import optimization.Simplex

class RmqSparseTable(a: IntArray) {
    var rmq: Array<IntArray?>
    fun min(i: Int, j: Int): Int {
        val k: Int = 31 - Integer.numberOfLeadingZeros(j - i + 1)
        return Math.min(rmq[k]!![i], rmq[k]!![j - (1 shl k) + 1])
    }

    companion object {
        fun main(args: Array<String?>?) {
            run {
                val st = RmqSparseTable(intArrayOf(1, 5, -2, 3))
                System.out.println(1 == st.min(0, 0))
                System.out.println(-2 == st.min(1, 2))
                System.out.println(-2 == st.min(0, 2))
                System.out.println(-2 == st.min(0, 3))
            }
            run {
                val st = RmqSparseTable(intArrayOf(1, 5, -2))
                System.out.println(1 == st.min(0, 0))
                System.out.println(-2 == st.min(1, 2))
                System.out.println(-2 == st.min(0, 2))
            }
        }
    }

    init {
        val n = a.size
        rmq = arrayOfNulls(32 - Integer.numberOfLeadingZeros(n))
        rmq[0] = a.clone()
        for (i in 1 until rmq.size) {
            rmq[i] = IntArray(n - (1 shl i) + 1)
            for (j in 0 until rmq[i].length) rmq[i]!![j] = Math.min(
                rmq[i - 1]!![j], rmq[i - 1]!![j + (1 shl i - 1)]
            )
        }
    }
}
