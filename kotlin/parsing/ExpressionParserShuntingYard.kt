package parsing

import java.util.LinkedList

object ExpressionParserShuntingYard {
    fun isDelim(c: Char): Boolean {
        return c == ' '
    }

    fun isOperator(c: Char): Boolean {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '%'
    }

    fun priority(op: Char): Int {
        return when (op) {
            '+', '-' -> 1
            '*', '/', '%' -> 2
            else -> -1
        }
    }

    fun processOperator(st: LinkedList<Integer?>, ops: LinkedList<Character?>) {
        val op: Char = ops.removeLast()
        val r: Int = st.removeLast()
        val l: Int = st.removeLast()
        when (op) {
            '+' -> st.add(l + r)
            '-' -> st.add(l - r)
            '*' -> st.add(l * r)
            '/' -> st.add(l / r)
            '%' -> st.add(l % r)
        }
    }

    fun eval(s: String): Int {
        val st: LinkedList<Integer?> = LinkedList()
        val ops: LinkedList<Character?> = LinkedList()
        var i = 0
        while (i < s.length()) {
            val c: Char = s.charAt(i)
            if (isDelim(c)) {
                i++
                continue
            }
            if (c == '(') ops.add('(') else if (c == ')') {
                while (ops.getLast() !== '(') processOperator(st, ops)
                ops.removeLast()
            } else if (isOperator(c)) {
                while (!ops.isEmpty() && priority(c) <= priority(ops.getLast())) processOperator(st, ops)
                ops.add(c)
            } else {
                var operand: String? = ""
                while (i < s.length() && Character.isDigit(s.charAt(i))) operand += s.charAt(i++)
                --i
                st.add(Integer.parseInt(operand))
            }
            i++
        }
        while (!ops.isEmpty()) processOperator(st, ops)
        return st.get(0)
    }

    @kotlin.Throws(Exception::class)
    fun main(args: Array<String?>?) {
        val engine: ScriptEngine = ScriptEngineManager().getEngineByName("JavaScript")
        val exp = "1+2*3*4+3*(2+2)-100"
        System.out.println(eval(exp))
        System.out.println(engine.eval(exp) as Integer == eval(exp))
    }
}
