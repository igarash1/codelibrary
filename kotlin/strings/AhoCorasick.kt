package strings

import numeric.FFT
import optimization.Simplex

// https://en.wikipedia.org/wiki/Aho–Corasick_algorithm
class AhoCorasick {
    val ALPHABET_SIZE = 26
    val MAX_STATES = 200000
    var transitions = Array(MAX_STATES) { IntArray(ALPHABET_SIZE) }
    var sufflink = IntArray(MAX_STATES)
    var escape = IntArray(MAX_STATES)
    var states = 1
    fun addString(s: String): Int {
        var v = 0
        for (c in s.toCharArray()) {
            (c -= 'a').toChar()
            if (transitions[v][c.toInt()] == 0) {
                transitions[v][c.toInt()] = states++
            }
            v = transitions[v][c.toInt()]
        }
        escape[v] = v
        return v
    }

    fun buildLinks() {
        val q = IntArray(MAX_STATES)
        var s = 0
        var t = 1
        while (s < t) {
            val v = q[s++]
            val u = sufflink[v]
            if (escape[v] == 0) {
                escape[v] = escape[u]
            }
            for (c in 0 until ALPHABET_SIZE) {
                if (transitions[v][c] != 0) {
                    q[t++] = transitions[v][c]
                    sufflink[transitions[v][c]] = if (v != 0) transitions[u][c] else 0
                } else {
                    transitions[v][c] = transitions[u][c]
                }
            }
        }
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val ahoCorasick = AhoCorasick()
            ahoCorasick.addString("a")
            ahoCorasick.addString("aa")
            ahoCorasick.addString("abaaa")
            ahoCorasick.buildLinks()
            val t = ahoCorasick.transitions
            val e = ahoCorasick.escape
            val s = "abaa"
            var state = 0
            for (i in 0 until s.length()) {
                state = t[state][s.charAt(i) - 'a']
                if (e[state] != 0) System.out.println(i)
            }
        }
    }
}
