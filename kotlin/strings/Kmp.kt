package strings

import java.util.Random

// https://en.wikipedia.org/wiki/Knuth–Morris–Pratt_algorithm
object Kmp {
    fun prefixFunction(s: String): IntArray {
        val p = IntArray(s.length())
        var k = 0
        for (i in 1 until s.length()) {
            while (k > 0 && s.charAt(k) !== s.charAt(i)) k = p[k - 1]
            if (s.charAt(k) === s.charAt(i)) ++k
            p[i] = k
        }
        return p
    }

    fun findSubstring(haystack: String, needle: String): Int {
        val m: Int = needle.length()
        if (m == 0) return 0
        val p = prefixFunction(needle)
        var i = 0
        var k = 0
        while (i < haystack.length()) {
            while (k > 0 && needle.charAt(k) !== haystack.charAt(i)) k = p[k - 1]
            if (needle.charAt(k) === haystack.charAt(i)) ++k
            if (k == m) return i + 1 - m
            i++
        }
        return -1
    }

    fun minPeriod(s: String): Int {
        val n: Int = s.length()
        val p = prefixFunction(s)
        val maxBorder = p[n - 1]

        // check periodicity
        // if (n % minPeriod != 0) return -1;
        return n - maxBorder
    }

    // random tests
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..9999) {
            val s = getRandomString(rnd, 100)
            val pattern = getRandomString(rnd, 5)
            val pos1 = findSubstring(s, pattern)
            val pos2: Int = s.indexOf(pattern)
            if (pos1 != pos2) throw RuntimeException()
        }
        System.out.println(minPeriod("abababab"))
    }

    fun getRandomString(rnd: Random, maxlen: Int): String {
        val n: Int = rnd.nextInt(maxlen)
        val s = CharArray(n)
        for (i in 0 until n) s[i] = ('a' + rnd.nextInt(3))
        return String(s)
    }
}
