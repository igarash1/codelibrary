package combinatorics

import java.util.Arrays

object BracketSequences {
    fun nextBracketSequence(s: CharArray): Boolean {
        val n = s.size
        var i = n - 1
        var balance = 0
        while (i >= 0) {
            balance += if (s[i] == '(') -1 else 1
            if (s[i] == '(' && balance > 0) {
                --balance
                val open = (n - i - 1 - balance) / 2
                val close = n - i - 1 - open
                s[i] = ')'
                Arrays.fill(s, i + 1, i + 1 + open, '(')
                Arrays.fill(s, i + 1 + open, i + open + close, ')')
                return true
            }
            i--
        }
        return false
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val s: CharArray = "((()))".toCharArray()
        do {
            System.out.println(String(s))
        } while (nextBracketSequence(s))
    }
}
