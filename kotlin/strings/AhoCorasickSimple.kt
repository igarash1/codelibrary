package strings

import numeric.FFT
import optimization.Simplex

class AhoCorasickSimple {
    var prefixes: Array<String>
    fun buildAutomata(words: Array<String>): Array<IntArray> {
        val prefixMap: Map<String, Integer> = TreeMap()
        for (s in words) {
            for (i in 0..s.length()) {
                prefixMap.put(s.substring(0, i), 0)
            }
        }
        prefixes = prefixMap.keySet().toArray(arrayOfNulls<String>(0))
        for (i in prefixes.indices) {
            prefixMap.put(prefixes[i], i)
        }
        val transitions = Array(prefixes.size) { IntArray(ALPHABET_SIZE) }
        for (i in prefixes.indices) {
            for (j in 0 until ALPHABET_SIZE) {
                var s = prefixes[i] + ('a'.toInt() + j) as Char
                while (!prefixMap.containsKey(s)) {
                    s = s.substring(1)
                }
                transitions[i][j] = prefixMap[s]
            }
        }
        return transitions
    }

    companion object {
        const val ALPHABET_SIZE = 26

        // Usage example
        fun main(args: Array<String?>?) {
            val automaton = AhoCorasickSimple()
            val transitions = automaton.buildAutomata(arrayOf("abc", "bc"))
            val s = "zabcbc"
            var state = 0
            for (i in 0 until s.length()) {
                System.out.println(s.substring(0, i).toString() + " " + automaton.prefixes[state])
                state = transitions[state][s.charAt(i) - 'a']
            }
        }
    }
}
