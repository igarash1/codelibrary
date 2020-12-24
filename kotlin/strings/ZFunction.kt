package strings

import java.util.Arrays

object ZFunction {
    // z[i] = lcp(s[0..], s[i..])
    fun zFunction(s: String): IntArray {
        val z = IntArray(s.length())
        var i = 1
        var l = 0
        var r = 0
        while (i < z.size) {
            if (i <= r) z[i] = Math.min(r - i + 1, z[i - l])
            while (i + z[i] < z.size && s.charAt(z[i]) === s.charAt(i + z.get(i))) ++z[i]
            if (r < i + z[i] - 1) {
                l = i
                r = i + z[i] - 1
            }
            ++i
        }
        return z
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val z = zFunction("abcababc")
        System.out.println(Arrays.toString(z))
    }
}
