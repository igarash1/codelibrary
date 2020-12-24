package parsing

import javax.script.ScriptEngine

class ExpressionParserRecursiveDescent {
    var s: String? = null
    var pos = 0
    var token = 0.toChar()
    var tokenValue = 0.0
    fun readToken() {
        while (true) {
            if (pos == s!!.length()) {
                token = 255.toChar()
                return
            }
            val c: Char = s.charAt(pos++)
            if ("+-*/^()\n".indexOf(c) !== -1) {
                token = c
                return
            }
            if (Character.isSpaceChar(c)) continue
            if (Character.isDigit(c) || c == '.') {
                var operand: String? = "" + c
                while (pos < s!!.length() && (Character.isDigit(s.charAt(pos)) || s.charAt(pos) === '.')) operand += s.charAt(
                    pos++
                )
                tokenValue = Double.parseDouble(operand)
                token = 'n'
                return
            }
            throw RuntimeException("Bad character: $c")
        }
    }

    fun skip(ch: Int) {
        if (token.toInt() != ch) throw RuntimeException("Bad character: $token, expected: $ch")
        readToken()
    }

    // Original grammar:
    // Е -> Е + Т | Е-Т | Т
    // Т -> T*F | T/F | F
    // F -> number | (Е)
    //
    // After left recursion elimination:
    // Е -> ТЕ'
    // Е'-> +ТЕ' | -ТЕ' | е
    // Т -> FT'
    // T'-> *FT' | /FT' | е
    // F -> number | (Е)
    // factor ::= number | '(' expression ')'
    fun factor(): Double {
        if (token == 'n') {
            val v = tokenValue
            skip('n'.toInt())
            return v
        }
        skip('('.toInt())
        val v = expression()
        skip(')'.toInt())
        return v
    }

    // term ::= factor | term '*' factor | term '/' factor
    // Т -> FT'
    // T'-> *FT' | /FT' | е
    fun term(): Double {
        var v = factor()
        while (true) {
            if (token == '*') {
                skip('*'.toInt())
                v *= factor()
            } else if (token == '/') {
                skip('/'.toInt())
                v /= factor()
            } else return v
        }
    }

    // expression ::= term | expression '+' term | expression '-' term
    // Е -> ТЕ'
    // Е'-> +ТЕ' | -ТЕ' | е
    fun expression(): Double {
        var v = term()
        while (true) {
            if (token == '+') {
                skip('+'.toInt())
                v += term()
            } else if (token == '-') {
                skip('-'.toInt())
                v -= term()
            } else return v
        }
    }

    companion object {
        @kotlin.Throws(Exception::class)
        fun main(args: Array<String?>?) {
            val engine: ScriptEngine = ScriptEngineManager().getEngineByName("JavaScript")
            val exp = "1+2*3*4+3*(2+2)-100\n"
            System.out.println(engine.eval(exp))
            val parser = ExpressionParserRecursiveDescent()
            parser.s = exp
            parser.readToken()
            while (parser.token.toInt() != 255) {
                if (parser.token == '\n') {
                    parser.skip('\n'.toInt())
                    System.out.println()
                    continue
                }
                System.out.printf("%.5f", parser.expression())
            }
        }
    }
}
