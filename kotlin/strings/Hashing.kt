package strings

import java.math.BigInteger

class Hashing(s: CharSequence) {
    var hash1: IntArray
    var hash2: IntArray
    var p1: IntArray
    var p2: IntArray
    fun getHash(i: Int, len: Int): Long {
        val h1 = (hash1[i + len] + hash1[i].toLong() * (mod1 - p1[len])) % mod1
        val h2 = (hash2[i + len] + hash2[i].toLong() * (mod2 - p2[len])) % mod2
        return (h1 shl 32) + h2
    }

    companion object {
        const val multiplier: Long = 131
        val rnd: Random = Random()
        val mod1: Int = BigInteger.valueOf((1e9 + rnd.nextInt(1e9.toInt())) as Int).nextProbablePrime().intValue()
        val mod2: Int = BigInteger.valueOf((1e9 + rnd.nextInt(1e9.toInt())) as Int).nextProbablePrime().intValue()

        // random test
        fun main(args: Array<String?>?) {
            val rnd = Random(1)
            for (step in 0..999) {
                val n1: Int = rnd.nextInt(50)
                val s1 = getRandomString(n1, rnd)
                val n2: Int = rnd.nextInt(50)
                val s2 = getRandomString(n2, rnd)
                val h1 = Hashing(s1)
                val h2 = Hashing(s2)
                for (k in 0..999) {
                    val i1: Int = rnd.nextInt(n1 + 1)
                    val j1: Int = rnd.nextInt(n1 - i1 + 1) + i1
                    val i2: Int = rnd.nextInt(n2 + 1)
                    val j2: Int = rnd.nextInt(n2 - i2 + 1) + i2
                    if (s1.substring(i1, j1).equals(s2.substring(i2, j2))
                        !== (h1.getHash(i1, j1 - i1) == h2.getHash(i2, j2 - i2))
                    ) throw RuntimeException()
                }
            }
        }

        fun getRandomString(n: Int, rnd: Random): String {
            val s = CharArray(n)
            for (i in 0 until n) {
                s[i] = ('a' + rnd.nextInt(3))
            }
            return String(s)
        }
    }

    init {
        val n: Int = s.length()
        hash1 = IntArray(n + 1)
        hash2 = IntArray(n + 1)
        p1 = IntArray(n + 1)
        p2 = IntArray(n + 1)
        p1[0] = 1
        p2[0] = 1
        for (i in 0 until n) {
            hash1[i + 1] = ((hash1[i] * multiplier + s.charAt(i)) % mod1) as Int
            hash2[i + 1] = ((hash2[i] * multiplier + s.charAt(i)) % mod2) as Int
            p1[i + 1] = (p1[i] * multiplier % mod1).toInt()
            p2[i + 1] = (p2[i] * multiplier % mod2).toInt()
        }
    }
}
