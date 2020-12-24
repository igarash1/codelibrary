package structures

import numeric.FFT
import optimization.Simplex

class Tree2d(n: Int) {
    var t: Array<Treap.Node?>
    fun query(x1: Int, x2: Int, y1: Int, y2: Int): Long {
        var x1 = x1
        var x2 = x2
        var res: Long = 0
        x1 += t.size / 2
        x2 += t.size / 2
        while (x1 <= x2) {
            if (x1 and 1 != 0) {
                val treapAndResult: Treap.TreapAndResult = Treap.query(t[x1], y1.toLong(), y2.toLong())
                t[x1] = treapAndResult.treap
                res += treapAndResult.sum
            }
            if (x2 and 1 == 0) {
                val treapAndResult: Treap.TreapAndResult = Treap.query(t[x2], y1.toLong(), y2.toLong())
                t[x2] = treapAndResult.treap
                res += treapAndResult.sum
            }
            x1 = x1 + 1 shr 1
            x2 = x2 - 1 shr 1
        }
        return res
    }

    fun insert(x: Int, y: Int, value: Int) {
        var x = x
        x += t.size / 2
        while (x > 0) {
            t[x] = Treap.insert(t[x], y.toLong(), value.toLong())
            x = x shr 1
        }
    }

    fun modify(x1: Int, x2: Int, y1: Int, y2: Int, delta: Int) {
        var x1 = x1
        var x2 = x2
        x1 += t.size / 2
        x2 += t.size / 2
        while (x1 <= x2) {
            if (x1 and 1 != 0) {
                t[x1] = Treap.modify(t[x1], y1.toLong(), y2.toLong(), delta.toLong())
            }
            if (x2 and 1 == 0) {
                t[x2] = Treap.modify(t[x2], y1.toLong(), y2.toLong(), delta.toLong())
            }
            x1 = x1 + 1 shr 1
            x2 = x2 - 1 shr 1
        }
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val t = Tree2d(10)
            t.insert(1, 5, 3)
            t.insert(3, 3, 2)
            t.insert(2, 6, 1)
            t.modify(0, 9, 0, 9, 1)
            System.out.println(t.query(0, 9, 0, 9))
        }
    }

    init {
        t = arrayOfNulls<Treap.Node>(2 * n)
    }
}
