package combinatorics

import java.util.Arrays

// https://en.wikipedia.org/wiki/Partial_permutation
object Arrangements {
    fun nextArrangement(a: IntArray, n: Int): Boolean {
        val used = BooleanArray(n)
        for (x in a) used[x] = true
        val m = a.size
        var i = m - 1
        while (i >= 0) {
            used[a[i]] = false
            for (j in a[i] + 1 until n) {
                if (!used[j]) {
                    a[i++] = j
                    used[j] = true
                    var k = 0
                    while (i < m) {
                        if (!used[k]) {
                            a[i++] = k
                        }
                        k++
                    }
                    return true
                }
            }
            i--
        }
        return false
    }

    fun arrangementByNumber(n: Int, m: Int, number: Long): IntArray {
        var number = number
        val a = IntArray(m)
        val free = IntArray(n)
        for (i in 0 until n) {
            free[i] = i
        }
        for (i in 0 until m) {
            val cnt = countOfArrangements(n - 1 - i, m - 1 - i)
            val pos = (number / cnt).toInt()
            a[i] = free[pos]
            System.arraycopy(free, pos + 1, free, pos, n - 1 - pos)
            number %= cnt
        }
        return a
    }

    fun numberByArrangement(a: IntArray, n: Int): Long {
        val m = a.size
        var res: Long = 0
        val used = BooleanArray(n)
        for (i in 0 until m) {
            var cnt = 0
            for (j in 0 until a[i]) {
                if (!used[j]) {
                    ++cnt
                }
            }
            res += cnt * countOfArrangements(n - i - 1, m - i - 1)
            used[a[i]] = true
        }
        return res
    }

    fun countOfArrangements(n: Int, m: Int): Long {
        var res: Long = 1
        for (i in 0 until m) {
            res *= (n - i).toLong()
        }
        return res
    }

    fun nextArrangementWithRepeats(a: IntArray, n: Int): Boolean {
        for (i in a.indices.reversed()) {
            if (a[i] < n - 1) {
                ++a[i]
                Arrays.fill(a, i + 1, a.size, 0)
                return true
            }
        }
        return false
    }

    // Usage example
    fun main(args: Array<String?>?) {
        // print all arrangements
        var a = intArrayOf(0, 1, 2)
        var cnt = 0
        val n = 4
        do {
            System.out.println(Arrays.toString(a))
            if (!Arrays.equals(a, arrangementByNumber(n, a.size, numberByArrangement(a, n)))
                || cnt.toLong() != numberByArrangement(arrangementByNumber(n, a.size, cnt.toLong()), n)
            ) throw RuntimeException()
            ++cnt
        } while (nextArrangement(a, n))

        // print all arrangements with repeats
        a = IntArray(2)
        do {
            System.out.println(Arrays.toString(a))
        } while (nextArrangementWithRepeats(a, 2))
        a = intArrayOf(2, 3, 4)
        System.out.println(32L == numberByArrangement(a, 5))
        System.out.println(Arrays.equals(a, arrangementByNumber(5, a.size, 32)))
    }
}
