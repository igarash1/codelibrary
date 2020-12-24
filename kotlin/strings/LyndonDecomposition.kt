package strings

object LyndonDecomposition {
    fun minCyclicShift(a: String): String {
        val s: CharArray = (a + a).toCharArray()
        val n = s.size
        var res = 0
        var i = 0
        while (i < n / 2) {
            res = i
            var j = i + 1
            var k = i
            while (j < n && s[k] <= s[j]) {
                if (s[k] < s[j]) k = i else ++k
                ++j
            }
            while (i <= k) i += j - k
        }
        return String(s, res, n / 2)
    }

    fun decompose(s: String): Array<String> {
        val res: List<String> = ArrayList()
        val a: CharArray = s.toCharArray()
        val n = a.size
        var i = 0
        while (i < n) {
            var j = i + 1
            var k = i
            while (j < n && a[k] <= a[j]) {
                if (a[k] < a[j]) k = i else ++k
                ++j
            }
            while (i <= k) {
                res.add(s.substring(i, i + j - k))
                i += j - k
            }
        }
        return res.toArray(arrayOfNulls<String>(0))
    }

    fun main(args: Array<String?>?) {
        val s = "bara"
        val decompose = decompose(s)
        System.out.println(Arrays.toString(decompose))
        System.out.println(minCyclicShift(s))
    }
}
