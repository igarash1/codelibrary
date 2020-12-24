package combinatorics

import java.util.function.BiFunction

object NumberIterator {
    fun count(to: Long, countWithPrefix: BiFunction<IntArray?, Integer?, Long?>): Long {
        val s: CharArray = String.valueOf(to).toCharArray()
        val n = s.size
        val digits: IntArray = IntStream.range(0, n).map { i -> s[i] - '0' }.toArray()
        var res: Long = 0
        for (i in 0 until n) {
            val prefix: IntArray = Arrays.copyOf(digits, i + 1)
            prefix[i] = 0
            while (prefix[i] < digits[i] + if (i == n - 1) 1 else 0) {
                res += countWithPrefix.apply(prefix, n - 1 - i)
                ++prefix[i]
            }
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        // count numbers consisting only of digits 2 and 5
        val num_2_5 = count(222, BiFunction<IntArray, Integer, Long> { prefix, remainingDigits ->
            if (Arrays.equals(prefix, intArrayOf(0))) return@count (1L shl remainingDigits + 1) - 2
            val all_2_5: Boolean = Arrays.stream(prefix).allMatch { v -> v === 2 || v === 5 }
            if (all_2_5) 1L shl remainingDigits else 0
        })
        System.out.println(7L == num_2_5)
    }
}
