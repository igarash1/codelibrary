package strings

import java.util.stream.IntStream

// https://en.wikipedia.org/wiki/Suffix_array
object SuffixArray2 {
    // suffix array in O(n*log^2(n))
    fun suffixArray(s: CharSequence): IntArray {
        val n: Int = s.length()
        val sa: Array<Integer> = IntStream.range(0, n).boxed().toArray { _Dummy_.__Array__() }
        val rank: IntArray = s.chars().toArray()
        var len = 1
        while (len < n) {
            val rank2 = LongArray(n)
            for (i in 0 until n) rank2[i] = (rank[i].toLong() shl 32) + if (i + len < n) rank[i + len] + 1 else 0
            Arrays.sort(sa, Comparator.comparingLong { a -> rank2[a] })
            for (i in 0 until n) rank[sa[i]] = if (i > 0 && rank2[sa[i - 1]] == rank2[sa[i]]) rank[sa[i - 1]] else i
            len *= 2
        }
        return Arrays.stream(sa).mapToInt(Integer::intValue).toArray()
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..99999) {
            val n: Int = rnd.nextInt(100)
            val s: StringBuilder = rnd.ints(n, 0, 10)
                .collect({ StringBuilder() }, { sb, i -> sb.append(('a' + i)) }, StringBuilder::append)
            val sa = suffixArray(s)
            var i = 0
            while (i + 1 < n) {
                if (s.substring(sa[i]).compareTo(s.substring(sa[i + 1])) >= 0) throw RuntimeException()
                i++
            }
        }
        System.out.println("Test passed")
    }
}
