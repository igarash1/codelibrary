package optimization

import java.util.Arrays

object Simplex {
    // returns max c*x such that A*x <= b, x >= 0
    fun simplex(A: Array<Array<Rational?>>, b: Array<Rational?>, c: Array<Rational?>, x: Array<Rational?>?): Rational? {
        val m = A.size
        val n: Int = A[0].length + 1
        val index = IntArray(n + m)
        for (i in 0 until n + m) {
            index[i] = i
        }
        val a: Array<Array<Rational?>> = Array<Array<Rational?>>(m + 2) { arrayOfNulls<Rational>(n + 1) }
        for (a1 in a) {
            Arrays.fill(a1, Rational.ZERO)
        }
        var L = m
        for (i in 0 until m) {
            for (j in 0 until n - 1) {
                a[i][j] = A[i][j].negate()
            }
            a[i][n - 1] = Rational.ONE
            a[i][n] = b[i]
            if (a[L][n].compareTo(a[i][n]) > 0) {
                L = i
            }
        }
        for (j in 0 until n - 1) {
            a[m][j] = c[j]
        }
        a[m + 1][n - 1] = Rational.ONE.negate()
        var E = n - 1
        while (true) {
            if (L < m) {
                val t = index[E]
                index[E] = index[L + n]
                index[L + n] = t
                a[L][E] = a[L][E].inverse()
                for (j in 0..n) {
                    if (j != E) {
                        a[L][j] = a[L][j].mul(a[L][E].negate())
                    }
                }
                for (i in 0..m + 1) {
                    if (i != L) {
                        for (j in 0..n) {
                            if (j != E) {
                                a[i][j] = a[i][j].add(a[L][j].mul(a[i][E]))
                            }
                        }
                        a[i][E] = a[i][E].mul(a[L][E])
                    }
                }
            }
            E = -1
            for (j in 0 until n) {
                if (E < 0 || index[E] > index[j]) {
                    if (a[m + 1][j].signum() > 0 || a[m + 1][j].signum() === 0 && a[m][j].signum() > 0) {
                        E = j
                    }
                }
            }
            if (E < 0) {
                break
            }
            L = -1
            for (i in 0 until m) {
                if (a[i][E].signum() < 0) {
                    var d: Rational
                    if (L < 0 || a[L][n].div(a[L][E]).sub(a[i][n].div(a[i][E])).also { d = it }
                            .signum() < 0 || d.signum() === 0 && index[L + n] > index[i + n]) {
                        L = i
                    }
                }
            }
            if (L < 0) {
                return Rational.POSITIVE_INFINITY
            }
        }
        if (a[m + 1][n].signum() < 0) {
            return null
        }
        if (x != null) {
            Arrays.fill(x, Rational.ZERO)
            for (i in 0 until m) if (index[n + i] < n - 1) x[index[n + i]] = a[i][n]
        }
        return a[m][n]
    }

    // Usage example
    fun main(args: Array<String?>?) {
        var a = arrayOf(longArrayOf(4, -1), longArrayOf(2, 1), longArrayOf(-5, 2))
        var b = longArrayOf(8, 10, 2)
        var c = longArrayOf(1, 1)
        var x: Array<Rational?>? = arrayOfNulls<Rational>(c.size)
        var res: Rational? = simplex(cnv(a), cnv(b), cnv(c), x)
        System.out.println(Rational(8).equals(res))
        System.out.println(Arrays.toString(x))
        a = arrayOf(longArrayOf(3, 4, -3), longArrayOf(5, -4, -3), longArrayOf(7, 4, 11))
        b = longArrayOf(23, 10, 30)
        c = longArrayOf(-1, 1, 2)
        x = arrayOfNulls<Rational>(c.size)
        res = simplex(cnv(a), cnv(b), cnv(c), x)
        System.out.println(Rational(57, 8).equals(res))
        System.out.println(Arrays.toString(x))

        // no feasible non-negative solutions
        a = arrayOf(longArrayOf(4, -1), longArrayOf(2, 1), longArrayOf(-5, 2))
        b = longArrayOf(8, -10, 2)
        c = longArrayOf(1, 1)
        res = simplex(cnv(a), cnv(b), cnv(c), null)
        System.out.println(null == res)

        // unbounded problem
        a = arrayOf(longArrayOf(-4, 1), longArrayOf(-2, -1), longArrayOf(-5, 2))
        b = longArrayOf(-8, -10, 2)
        c = longArrayOf(1, 1)
        res = simplex(cnv(a), cnv(b), cnv(c), null)
        System.out.println(Rational.POSITIVE_INFINITY === res)

        // no feasible solutions
        a = arrayOf(longArrayOf(1), longArrayOf(-1))
        b = longArrayOf(1, -2)
        c = longArrayOf(0)
        res = simplex(cnv(a), cnv(b), cnv(c), null)
        System.out.println(null == res)

        // infinite number of solutions, but only one is returned
        a = arrayOf(longArrayOf(1, 1))
        b = longArrayOf(0)
        c = longArrayOf(1, 1)
        x = arrayOfNulls<Rational>(c.size)
        res = simplex(cnv(a), cnv(b), cnv(c), x)
        System.out.println(Arrays.toString(x))
    }

    fun cnv(a: LongArray): Array<Rational?> {
        val res: Array<Rational?> = arrayOfNulls<Rational>(a.size)
        for (i in a.indices) {
            res[i] = Rational(a[i])
        }
        return res
    }

    fun cnv(a: Array<LongArray>): Array<Array<Rational?>> {
        val res: Array<Array<Rational?>> = arrayOfNulls<Array<Rational?>>(a.size)
        for (i in a.indices) {
            res[i] = cnv(a[i])
        }
        return res
    }
}
