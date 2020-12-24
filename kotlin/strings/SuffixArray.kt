package strings

import java.util.stream.IntStream

// https://en.wikipedia.org/wiki/Suffix_array
object SuffixArray {
    // build suffix array in O(n*log(n))
    fun suffixArray(S: CharSequence): IntArray {
        val n: Int = S.length()

        // Stable sort of characters.
        // Same characters are sorted by their position in descending order.
        // E.g. last character which represents suffix of length 1 should be ordered first among same characters.
        val sa: IntArray = IntStream.range(0, n)
            .mapToObj { i -> n - 1 - i }
            .sorted(Comparator.comparingInt(S::charAt))
            .mapToInt(Integer::intValue)
            .toArray()
        val classes: IntArray = S.chars().toArray()
        // sa[i] - suffix on i'th position after sorting by first len characters
        // classes[i] - equivalence class of the i'th suffix after sorting by first len characters
        var len = 1
        while (len < n) {

            // Calculate classes for suffixes of length len * 2
            val c: IntArray = classes.clone()
            for (i in 0 until n) {
                // Condition sa[i - 1] + len < n emulates 0-symbol at the end of the string.
                // A separate class is created for each suffix followed by emulated 0-symbol.
                classes[sa[i]] =
                    if (i > 0 && c[sa[i - 1]] == c[sa[i]] && sa[i - 1] + len < n && c[sa[i - 1] + len / 2] == c[sa[i] + len / 2]) classes[sa[i - 1]] else i
            }
            // Suffixes are already sorted by first len characters
            // Now sort suffixes by first len * 2 characters
            val cnt: IntArray = IntStream.range(0, n).toArray()
            val s: IntArray = sa.clone()
            for (i in 0 until n) {
                // s[i] - order of suffixes sorted by first len characters
                // (s[i] - len) - order of suffixes sorted only by second len characters
                val s1 = s[i] - len
                // sort only suffixes of length > len, others are already sorted
                if (s1 >= 0) sa[cnt[classes[s1]]++] = s1
            }
            len *= 2
        }
        return sa
    }

    // sort rotations of a string in O(n*log(n))
    fun rotationArray(S: CharSequence): IntArray {
        val n: Int = S.length()
        val sa: IntArray = IntStream.range(0, n)
            .boxed()
            .sorted(Comparator.comparingInt(S::charAt))
            .mapToInt(Integer::intValue)
            .toArray()
        val classes: IntArray = S.chars().toArray()
        var len = 1
        while (len < n) {
            val c: IntArray = classes.clone()
            for (i in 0 until n) classes[sa[i]] =
                if (i > 0 && c[sa[i - 1]] == c[sa[i]] && c[(sa[i - 1] + len / 2) % n] == c[(sa[i] + len / 2) % n]) classes[sa[i - 1]] else i
            val cnt: IntArray = IntStream.range(0, n).toArray()
            val s: IntArray = sa.clone()
            for (i in 0 until n) {
                val s1 = (s[i] - len + n) % n
                sa[cnt[classes[s1]]++] = s1
            }
            len *= 2
        }
        return sa
    }

    // longest common prefixes array in O(n)
    fun lcp(sa: IntArray, s: CharSequence): IntArray {
        val n = sa.size
        val rank = IntArray(n)
        for (i in 0 until n) rank[sa[i]] = i
        val lcp = IntArray(n - 1)
        var i = 0
        var h = 0
        while (i < n) {
            if (rank[i] < n - 1) {
                val j = sa[rank[i] + 1]
                while (Math.max(i, j) + h < s.length() && s.charAt(i + h) === s.charAt(j + h)) {
                    ++h
                }
                lcp[rank[i]] = h
                if (h > 0) --h
            }
            i++
        }
        return lcp
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val s1 = "abcab"
        val sa1 = suffixArray(s1)

        // print suffixes in lexicographic order
        for (p in sa1) System.out.println(s1.substring(p))
        System.out.println("lcp = " + Arrays.toString(lcp(sa1, s1)))

        // random test
        val rnd = Random(1)
        for (step in 0..99999) {
            val n: Int = rnd.nextInt(100) + 1
            val s: StringBuilder = rnd.ints(n, 0, 10)
                .collect({ StringBuilder() }, { sb, i -> sb.append(('a' + i)) }, StringBuilder::append)
            val sa = suffixArray(s)
            val ra = rotationArray(s.toString() + '\u0000')
            val lcp = lcp(sa, s)
            var i = 0
            while (i + 1 < n) {
                val a: String = s.substring(sa[i])
                val b: String = s.substring(sa[i + 1])
                if (a.compareTo(b) >= 0 || !a.substring(0, lcp[i]).equals(b.substring(0, lcp[i]))
                    || "$a ".charAt(lcp[i]) === (b.toString() + " ").charAt(lcp.get(i)) || sa[i] != ra[i + 1]
                ) throw RuntimeException()
                i++
            }
        }
        System.out.println("Test passed")
    }
}
