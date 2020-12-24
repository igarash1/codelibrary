package combinatorics

import numeric.FFT
import optimization.Simplex

// https://en.wikipedia.org/wiki/Partition_(number_theory)
object Partitions {
    fun nextPartition(p: List<Integer>): Boolean {
        val n: Int = p.size()
        if (n <= 1) return false
        var s: Int = p.remove(n - 1) - 1
        var i = n - 2
        while (i > 0 && p[i].equals(p[i - 1])) {
            s += p.remove(i)
            --i
        }
        p.set(i, p[i] + 1)
        while (s-- > 0) {
            p.add(1)
        }
        return true
    }

    fun partitionByNumber(n: Int, number: Long): List<Integer> {
        var number = number
        val p: List<Integer> = ArrayList()
        var x = n
        while (x > 0) {
            var j = 1
            while (true) {
                val cnt = partitionFunction(x)[x][j]
                if (number < cnt) break
                number -= cnt
                ++j
            }
            p.add(j)
            x -= j
        }
        return p
    }

    fun numberByPartition(p: List<Integer>): Long {
        var res: Long = 0
        var sum = 0
        for (x in p) {
            sum += x
        }
        for (cur in p) {
            for (j in 0 until cur) {
                res += partitionFunction(sum)[sum][j]
            }
            sum -= cur
        }
        return res
    }

    fun generateIncreasingPartitions(p: IntArray, left: Int, last: Int, pos: Int) {
        if (left == 0) {
            for (i in 0 until pos) System.out.print(p[i].toString() + " ")
            System.out.println()
            return
        }
        p[pos] = last + 1
        while (p[pos] <= left) {
            generateIncreasingPartitions(p, left - p[pos], p[pos], pos + 1)
            p[pos]++
        }
    }

    fun countPartitions(n: Int): Long {
        val p = LongArray(n + 1)
        p[0] = 1
        for (i in 1..n) {
            for (j in i..n) {
                p[j] += p[j - i]
            }
        }
        return p[n]
    }

    fun partitionFunction(n: Int): Array<LongArray> {
        val p = Array(n + 1) { LongArray(n + 1) }
        p[0][0] = 1
        for (i in 1..n) {
            for (j in 1..i) {
                p[i][j] = p[i - 1][j - 1] + p[i - j][j]
            }
        }
        return p
    }

    fun partitionFunction2(n: Int): Array<LongArray> {
        val p = Array(n + 1) { LongArray(n + 1) }
        p[0][0] = 1
        for (i in 1..n) {
            for (j in 1..i) {
                for (k in 0..j) {
                    p[i][j] += p[i - j][k]
                }
            }
        }
        return p
    }

    // Usage example
    fun main(args: Array<String?>?) {
        System.out.println(7L == countPartitions(5))
        System.out.println(627L == countPartitions(20))
        System.out.println(5604L == countPartitions(30))
        System.out.println(204226L == countPartitions(50))
        System.out.println(190569292L == countPartitions(100))
        val p: List<Integer> = ArrayList()
        Collections.addAll(p, 1, 1, 1, 1, 1)
        do {
            System.out.println(p)
        } while (nextPartition(p))
        val p1 = IntArray(8)
        generateIncreasingPartitions(p1, p1.size, 0, 0)
        val list: List<Integer> = partitionByNumber(5, 6)
        System.out.println(list)
        System.out.println(numberByPartition(list))
    }
}
