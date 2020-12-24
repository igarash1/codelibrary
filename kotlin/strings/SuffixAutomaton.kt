package strings

import numeric.FFT
import optimization.Simplex

// https://en.wikipedia.org/wiki/Suffix_automaton
object SuffixAutomaton {
    fun buildSuffixAutomaton(s: CharSequence): Array<State> {
        val n: Int = s.length()
        val st = arrayOfNulls<State>(Math.max(2, 2 * n - 1))
        st[0] = State()
        st[0]!!.suffLink = -1
        var last = 0
        var size = 1
        for (i in 0 until s.length()) {
            val c: Char = s.charAt(i)
            val cur = size++
            st[cur] = State()
            st[cur]!!.length = i + 1
            st[cur]!!.firstPos = i
            var p = last
            while (p != -1 && st[p]!!.next[c.toInt()] == -1) {
                st[p]!!.next[c.toInt()] = cur
                p = st[p]!!.suffLink
            }
            if (p == -1) {
                st[cur]!!.suffLink = 0
            } else {
                val q = st[p]!!.next[c.toInt()]
                if (st[p]!!.length + 1 == st[q]!!.length) {
                    st[cur]!!.suffLink = q
                } else {
                    val clone = size++
                    st[clone] = State()
                    st[clone]!!.length = st[p]!!.length + 1
                    System.arraycopy(st[q]!!.next, 0, st[clone]!!.next, 0, st[q]!!.next.size)
                    st[clone]!!.suffLink = st[q]!!.suffLink
                    while (p != -1 && st[p]!!.next[c.toInt()] == q) {
                        st[p]!!.next[c.toInt()] = clone
                        p = st[p]!!.suffLink
                    }
                    st[q]!!.suffLink = clone
                    st[cur]!!.suffLink = clone
                }
            }
            last = cur
        }
        for (i in 1 until size) {
            st[st[i]!!.suffLink]!!.invSuffLinks.add(i)
        }
        return Arrays.copyOf(st, size)
    }

    // random tests
    fun main(args: Array<String?>?) {
        val occurrences = occurrences("abaabbab", "ab")
        System.out.println(Arrays.toString(occurrences))
        val lcs = lcs("abaabbab", "abb")
        System.out.println(lcs)
    }

    fun occurrences(haystack: String, needle: String): IntArray {
        val automaton = buildSuffixAutomaton(haystack)
        var node = 0
        for (c in needle.toCharArray()) {
            val next = automaton[node].next[c.toInt()]
            if (next == -1) {
                return IntArray(0)
            }
            node = next
        }
        val occurrences: List<Integer> = ArrayList()
        val q: Queue<Integer> = ArrayDeque()
        q.add(node)
        while (!q.isEmpty()) {
            val curNode: Int = q.remove()
            if (automaton[curNode].firstPos != -1) {
                occurrences.add(automaton[curNode].firstPos - needle.length() + 1)
            }
            q.addAll(automaton[curNode].invSuffLinks)
        }
        return occurrences.stream().sorted().mapToInt(Integer::intValue).toArray()
    }

    fun lcs(a: String, b: String): String {
        val st = buildSuffixAutomaton(a)
        var len = 0
        var bestLen = 0
        var bestPos = -1
        var i = 0
        var cur = 0
        while (i < b.length()) {
            val c: Char = b.charAt(i)
            if (st[cur].next[c.toInt()] == -1) {
                while (cur != -1 && st[cur].next[c.toInt()] == -1) {
                    cur = st[cur].suffLink
                }
                if (cur == -1) {
                    cur = 0
                    len = 0
                    ++i
                    continue
                }
                len = st[cur].length
            }
            ++len
            cur = st[cur].next[c.toInt()]
            if (bestLen < len) {
                bestLen = len
                bestPos = i
            }
            ++i
        }
        return b.substring(bestPos - bestLen + 1, bestPos + 1)
    }

    class State {
        var length = 0
        var suffLink = 0
        var invSuffLinks: List<Integer> = ArrayList(0)
        var firstPos = -1
        var next = IntArray(128)

        init {
            Arrays.fill(next, -1)
        }
    }
}
