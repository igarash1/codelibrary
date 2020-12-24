package strings

import java.util.Arrays

// DC3 linear time suffix array construction algorithm ("Linear Work Suffix Array Construction")
object SuffixArrayDC3 {
    fun leq(a1: Int, a2: Int, b1: Int, b2: Int): Boolean {
        return a1 < b1 || a1 == b1 && a2 <= b2
    }

    fun leq(a1: Int, a2: Int, a3: Int, b1: Int, b2: Int, b3: Int): Boolean {
        return a1 < b1 || a1 == b1 && leq(a2, a3, b2, b3)
    }

    // stably sort a[0..n-1] to b[0..n-1] with keys in 0..K from r
    fun radixPass(a: IntArray, b: IntArray, r: IntArray, offset: Int, n: Int, K: Int) {
        val cnt = IntArray(K + 1)
        for (i in 0 until n) ++cnt[r[a[i] + offset]]
        for (i in 1 until cnt.size) cnt[i] += cnt[i - 1]
        for (i in n - 1 downTo 0) b[--cnt[r[a[i] + offset]]] = a[i]
    }

    // find the suffix array SA of T[0..n-1] in {1..K}^n
    // require T[n]=T[n+1]=T[n+2]=0, n>=2
    private fun suffixArray(T: IntArray, SA: IntArray, n: Int, K: Int) {
        val n0 = (n + 2) / 3
        val n1 = (n + 1) / 3
        val n2 = n / 3
        val n02 = n0 + n2

        //******* Step 0: Construct sample ********
        // generate positions of mod 1 and mod 2 suffixes
        // the "+(n0-n1)" adds a dummy mod 1 suffix if n%3 == 1
        val R = IntArray(n02 + 3)
        run {
            var i = 0
            var j = 0
            while (i < n + (n0 - n1)) {
                if (i % 3 != 0) R[j++] = i
                i++
            }
        }

        //******* Step 1: Sort sample suffixes ********
        // lsb radix sort the mod 1 and mod 2 triples
        val SA12 = IntArray(n02 + 3)
        radixPass(R, SA12, T, 2, n02, K)
        radixPass(SA12, R, T, 1, n02, K)
        radixPass(R, SA12, T, 0, n02, K)

        // find lexicographic names of triples and
        // write them to correct places in R
        var name = 0
        for (i in 0 until n02) {
            if (i == 0 || T[SA12[i]] != T[SA12[i - 1]] || T[SA12[i] + 1] != T[SA12[i - 1] + 1] || T[SA12[i] + 2] != T[SA12[i - 1] + 2]) {
                ++name
            }
            R[SA12[i] / 3 + (if (SA12[i] % 3 == 1) 0 else n0)] = name
        }
        if (name < n02) {
            // recurse if names are not yet unique
            suffixArray(R, SA12, n02, name)
            // store unique names in R using the suffix array
            for (i in 0 until n02) R[SA12[i]] = i + 1
        } else {
            // generate the suffix array of R directly
            for (i in 0 until n02) SA12[R[i] - 1] = i
        }

        //******* Step 2: Sort nonsample suffixes ********
        // stably sort the mod 0 suffixes from SA12 by their first character
        val R0 = IntArray(n0)
        var i = 0
        var j = 0
        while (i < n02) {
            if (SA12[i] < n0) R0[j++] = 3 * SA12[i]
            i++
        }
        val SA0 = IntArray(n0)
        radixPass(R0, SA0, T, 0, n0, K)

        //******* Step 3: Merge ********
        // merge sorted SA0 suffixes and sorted SA12 suffixes
        var p = 0
        var t = n0 - n1
        var k = 0
        while (k < n) {
            val i = if (SA12[t] < n0) SA12[t] * 3 + 1 else (SA12[t] - n0) * 3 + 2 // pos of current offset 12 suffix
            val j = SA0[p] // pos of current offset 0 suffix
            if (if (SA12[t] < n0) // different compares for mod 1 and mod 2 suffixes
                    leq(T[i], R[SA12[t] + n0], T[j], R[j / 3]) else leq(
                    T[i], T[i + 1], R[SA12[t] - n0 + 1], T[j], T[j + 1],
                    R[j / 3 + n0]
                )
            ) { // suffix from SA12 is smaller
                SA[k] = i
                if (++t == n02) // done --- only SA0 suffixes left
                {
                    k++
                    while (p < n0) {
                        SA[k] = SA0[p]
                        p++
                        k++
                    }
                }
            } else { // suffix from SA0 is smaller
                SA[k] = j
                if (++p == n0) // done --- only SA12 suffixes left
                {
                    k++
                    while (t < n02) {
                        SA[k] = if (SA12[t] < n0) SA12[t] * 3 + 1 else (SA12[t] - n0) * 3 + 2
                        t++
                        k++
                    }
                }
            }
            k++
        }
    }

    fun suffixArray(s: CharSequence): IntArray {
        val n: Int = s.length()
        if (n <= 1) return IntArray(n)
        val S: IntArray = IntStream.range(0, n + 3).map { i -> if (i < n) s.charAt(i) else 0 }.toArray()
        val sa = IntArray(n)
        suffixArray(S, sa, n, 255)
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
        val rnd1 = Random(1)
        val n2 = 5000000
        val ss: StringBuilder =
            rnd1.ints(n2, 0, 26).collect({ StringBuilder() }, { sb, i -> sb.append(('a' + i)) }, StringBuilder::append)
        val time: Long = System.currentTimeMillis()
        val sa2 = suffixArray(ss)
        System.out.println(System.currentTimeMillis() - time)
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
            val lcp = lcp(sa, s)
            var i = 0
            while (i + 1 < n) {
                val a: String = s.substring(sa[i])
                val b: String = s.substring(sa[i + 1])
                if (a.compareTo(b) >= 0 || !a.substring(0, lcp[i]).equals(b.substring(0, lcp[i]))
                    || "$a ".charAt(lcp[i]) === (b.toString() + " ").charAt(lcp.get(i))
                ) throw RuntimeException()
                i++
            }
        }
        System.out.println("Test passed")
    }
}
