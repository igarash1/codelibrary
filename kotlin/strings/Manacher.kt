package strings

import java.util.Arrays

// Manacher's algorithm to finds all palindromes: https://cp-algorithms.com/string/manacher.html
object Manacher {
    // d1[i] - how many palindromes of odd length with center at i
    fun oddPalindromes(s: String): IntArray {
        val n: Int = s.length()
        val d1 = IntArray(n)
        var l = 0
        var r = -1
        for (i in 0 until n) {
            var k = (if (i > r) 0 else Math.min(d1[l + r - i], r - i)) + 1
            while (i + k < n && i - k >= 0 && s.charAt(i + k) === s.charAt(i - k)) ++k
            d1[i] = k--
            if (i + k > r) {
                l = i - k
                r = i + k
            }
        }
        return d1
    }

    // d2[i] - how many palindromes of even length with center at i
    fun evenPalindromes(s: String): IntArray {
        val n: Int = s.length()
        val d2 = IntArray(n)
        var l = 0
        var r = -1
        for (i in 0 until n) {
            var k = (if (i > r) 0 else Math.min(d2[l + r - i + 1], r - i + 1)) + 1
            while (i + k - 1 < n && i - k >= 0 && s.charAt(i + k - 1) === s.charAt(i - k)) ++k
            d2[i] = --k
            if (i + k - 1 > r) {
                l = i - k
                r = i + k - 1
            }
        }
        return d2
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val text = "aaaba"
        System.out.println(Arrays.toString(oddPalindromes(text)))
        System.out.println(Arrays.toString(evenPalindromes(text)))
    }
}
