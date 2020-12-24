package combinatorics

import java.util.Arrays

object CombinatorialEnumerations {
    fun main(args: Array<String?>?) {
        val permutations = Permutations(3)
        permutations.enumerate()
        val combinations = Combinations(4, 3)
        combinations.enumerate()
        val arrangements = Arrangements(3, 2)
        arrangements.enumerate()
        val correctBracketSequences = CorrectBracketSequences(6)
        correctBracketSequences.enumerate()
        val partitions = Partitions(4)
        partitions.enumerate()
    }

    // subclass and implement count() method
    abstract class AbstractEnumeration protected constructor(// range of definition of sequence elements
        protected val range: Int, // length of generated sequences
        protected val length: Int
    ) {
        // returns number of combinatorial sequences starting with prefix
        // by contract only the last element of prefix can be invalid and in this case 0 must be returned
        protected abstract fun count(prefix: IntArray?): Long
        fun next(sequence: IntArray): IntArray {
            return fromNumber(toNumber(sequence) + 1)
        }

        fun totalCount(): Long {
            return count(IntArray(0))
        }

        fun toNumber(sequence: IntArray): Long {
            var res: Long = 0
            for (i in sequence.indices) {
                val prefix: IntArray = Arrays.copyOf(sequence, i + 1)
                prefix[i] = 0
                while (prefix[i] < sequence[i]) {
                    res += count(prefix)
                    ++prefix[i]
                }
            }
            return res
        }

        fun fromNumber(number: Long): IntArray {
            var number = number
            val sequence = IntArray(length)
            for (i in sequence.indices) {
                val prefix: IntArray = Arrays.copyOf(sequence, i + 1)
                prefix[i] = 0
                while (prefix[i] < range) {
                    val cur = count(prefix)
                    if (number < cur) {
                        break
                    }
                    number -= cur
                    ++prefix[i]
                }
                sequence[i] = prefix[i]
            }
            return sequence
        }

        fun enumerate() {
            System.out.println(getClass().getName())
            val total = totalCount()
            for (i in 0 until total) {
                val p = fromNumber(i)
                System.out.println(i.toString() + " " + Arrays.toString(p))
                val j = toNumber(p)
                if (i != j) throw RuntimeException()
            }
        }
    }

    class Arrangements(n: Int, k: Int) : AbstractEnumeration(n, k) {
        @Override
        protected override fun count(prefix: IntArray): Long {
            val size = prefix.size

            // if the last element appears twice, then prefix is invalid and 0 must be returned
            for (i in 0 until size - 1) if (prefix[size - 1] == prefix[i]) return 0
            var res: Long = 1
            for (i in 0 until length - size) res *= (range - size - i).toLong()
            return res
        }
    }

    class Permutations(n: Int) : Arrangements(n, n)
    class Combinations(n: Int, k: Int) : AbstractEnumeration(n, k) {
        val binomial: Array<LongArray>
        @Override
        protected override fun count(prefix: IntArray): Long {
            val size = prefix.size

            // if there is no combination with given prefix, 0 must be returned.
            // by contract only the last element can make prefix invalid.
            if (size >= 2 && prefix[size - 1] <= prefix[size - 2]) return 0

            // prefix is valid. return the number of combinations starting with prefix
            val last = if (size > 0) prefix[size - 1] else -1
            return binomial[range - 1 - last][length - size]
        }

        init {
            binomial = Array(n + 1) { LongArray(n + 1) }
            // calculate binomial coefficients in advance
            for (i in 0..n) for (j in 0..i) binomial[i][j] =
                if (j == 0) 1 else binomial[i - 1][j - 1] + binomial[i - 1][j]
        }
    }

    class CorrectBracketSequences(sequenceLength: Int) : AbstractEnumeration(2, sequenceLength) {
        val d: Array<LongArray>
        @Override
        protected override fun count(prefix: IntArray): Long {
            val size = prefix.size
            var balance = 0
            for (cur in prefix)  // 0 designates '('
            // 1 designates ')'
                balance += if (cur == 0) 1 else -1
            return if (balance < 0 || balance > length - size) 0 else d[length - size][balance]
        }

        // sequenceLength must be a multiple of 2
        init {
            d = Array(sequenceLength + 1) { LongArray(sequenceLength / 2 + 1) }
            // d[i][j] - number of bracket sequences of length i with balance j
            d[0][0] = 1
            for (i in 1..sequenceLength) {
                for (balance in 0..sequenceLength / 2) {
                    if (balance - 1 >= 0) d[i][balance] += d[i - 1][balance - 1]
                    if (balance + 1 <= sequenceLength / 2) d[i][balance] += d[i - 1][balance + 1]
                }
            }
        }
    }

    class Partitions(value: Int) : AbstractEnumeration(value + 1, value) {
        val pp: Array<LongArray>
        @Override
        protected override fun count(prefix: IntArray): Long {
            val size = prefix.size
            var sum = 0
            for (e in prefix) sum += e
            if (sum == range - 1) return 1
            if (sum > range - 1 || size > 0 && prefix[size - 1] == 0 || size >= 2 && prefix[size - 1] > prefix[size - 2]) return 0
            val last = if (size > 0) prefix[size - 1] else range - 1
            return pp[range - 1 - sum][last]
        }

        init {
            val p = Array(value + 1) { LongArray(value + 1) }
            // p[i][j] - number of partitions of i with largest summand equal to j
            p[0][0] = 1
            for (i in 1..value) for (j in 1..i) p[i][j] = p[i - 1][j - 1] + p[i - j][j]
            pp = Array(value + 1) { LongArray(value + 1) }
            for (i in 1..value) for (j in 1..value) pp[i][j] = p[i][j] + pp[i][j - 1]
        }
    }
}
