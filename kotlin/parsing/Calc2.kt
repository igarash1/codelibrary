package parsing

class Calc2 {
    enum class TOKEN {
        PLUS, MINUS, MUL, DIV, NUMBER, LP, RP, END, FUNCTION
    }

    fun expr(): Double {
        var left = term()
        while (true) {
            when (curr_tok) {
                TOKEN.PLUS -> left += term()
                TOKEN.MINUS -> left -= term()
                else -> return left
            }
        }
    }

    fun term(): Double {
        var left = prim()
        while (true) {
            when (curr_tok) {
                TOKEN.MUL -> left *= prim()
                TOKEN.DIV -> left /= prim()
                else -> return left
            }
        }
    }

    fun prim(): Double {
        get_token()
        return when (curr_tok) {
            TOKEN.NUMBER -> {
                val v = number_value
                get_token()
                v
            }
            TOKEN.MINUS -> -prim()
            TOKEN.LP -> {
                val e = expr()
                if (curr_tok != TOKEN.RP) error(")expected") else e
            }
            TOKEN.FUNCTION -> {
                val f = function_name
                get_token()
                if (curr_tok != TOKEN.LP) return error("( expected")
                val z = prim()
                if (curr_tok != TOKEN.RP) return error(") expected")
                if ("sin".equals(f)) {
                    Math.sin(z)
                } else {
                    error("unknown function")
                }
            }
            else -> error("primary expression expected")
        }
    }

    fun error(msg: String?): Double {
        error_msg = msg
        return Double.NaN
    }

    var curr_tok: TOKEN? = null
    var number_value = 0.0
    var function_name: String? = null
    var error_msg: String? = null
    var input: String? = null
    var pos = 0
    fun get_token() {
        if (pos >= input!!.length()) {
            curr_tok = TOKEN.END
            return
        }
        val x: Char = input.charAt(pos++)
        when (x) {
            '+' -> curr_tok = TOKEN.PLUS
            '-' -> curr_tok = TOKEN.MINUS
            '*' -> curr_tok = TOKEN.MUL
            '/' -> curr_tok = TOKEN.DIV
            '(' -> curr_tok = TOKEN.LP
            ')' -> curr_tok = TOKEN.RP
            else -> if (Character.isDigit(x) || x == '.') {
                curr_tok = TOKEN.NUMBER
                var s: String? = "" + x
                while (pos < input!!.length() && (Character.isDigit(input.charAt(pos)) || input.charAt(pos) === '.')) {
                    s += input.charAt(pos)
                    ++pos
                }
                number_value = Double.parseDouble(s)
            } else if (Character.isLetter(x)) {
                curr_tok = TOKEN.FUNCTION
                var s: String? = "" + x
                while (pos < input!!.length() && Character.isLetter(input.charAt(pos))) {
                    s += input.charAt(pos)
                    ++pos
                }
                function_name = s
            }
        }
    }

    companion object {
        fun main(args: Array<String?>?) {
            val calc = Calc2()
            calc.input = "sin(1)"
            calc.pos = 0
            val res = calc.expr()
            if (Double.isNaN(res)) {
                System.out.println(calc.error_msg)
            } else {
                System.out.println(res)
            }
        }
    }
}
