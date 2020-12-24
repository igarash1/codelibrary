package parsing

object RecursiveDescentParser {
    fun main(args: Array<String?>?) {
        val lexer = Lexer("{ a = 10; do { a = a - 2;}  while (3 < a); }")
        val parser = Parser(lexer)
        val ast = parser.parse()
        val compiler = Compiler()
        val program: List<Object> = compiler.compile(ast)
        val vm = VirtualMachine()
        vm.run(program)
    }

    internal enum class Lexeme {
        NUM, ID, IF, ELSE, WHILE, DO, LBRA, RBRA, LPAR, RPAR, PLUS, MINUS, LESS, EQUAL, SEMICOLON, EOF
    }

    internal class Lexer(val s: String) {
        companion object {
            val SYMBOLS: Map<Character?, Lexeme> = HashMap()
            val WORDS: Map<String?, Lexeme> = HashMap()

            init {
                SYMBOLS.put('{', Lexeme.LBRA)
                SYMBOLS.put('}', Lexeme.RBRA)
                SYMBOLS.put('=', Lexeme.EQUAL)
                SYMBOLS.put(';', Lexeme.SEMICOLON)
                SYMBOLS.put('(', Lexeme.LPAR)
                SYMBOLS.put(')', Lexeme.RPAR)
                SYMBOLS.put('+', Lexeme.PLUS)
                SYMBOLS.put('-', Lexeme.MINUS)
                SYMBOLS.put('<', Lexeme.LESS)
            }

            init {
                WORDS.put("if", Lexeme.IF)
                WORDS.put("else", Lexeme.ELSE)
                WORDS.put("do", Lexeme.DO)
                WORDS.put("while", Lexeme.WHILE)
            }
        }

        var pos = 0
        var ch: Character? = ' '
        var value: Integer? = null
        var sym: Lexeme? = null
        fun error(message: String) {
            throw RuntimeException("Lexer error: $message")
        }

        fun readCh() {
            ch = if (pos < s.length()) s.charAt(pos++) else null
        }

        fun readNextToken() {
            value = null
            sym = null
            while (sym == null) {
                if (ch == null) {
                    sym = Lexeme.EOF
                } else if (Character.isSpaceChar(ch)) {
                    readCh()
                } else if (SYMBOLS.containsKey(ch)) {
                    sym = SYMBOLS[ch]
                    readCh()
                } else if (Character.isDigit(ch)) {
                    var `val` = 0
                    while (ch != null && Character.isDigit(ch)) {
                        `val` = `val` * 10 + ch - '0'.toInt()
                        readCh()
                    }
                    value = `val`
                    sym = Lexeme.NUM
                } else if (Character.isLetter(ch)) {
                    var ident: String? = ""
                    while (ch != null && Character.isLetter(ch)) {
                        ident += ch
                        readCh()
                    }
                    if (WORDS.containsKey(ident)) {
                        sym = WORDS[ident]
                    } else if (ident!!.length() === 1) {
                        sym = Lexeme.ID
                        value = ident.charAt(0) - 'a'
                    } else {
                        error("Unknown identifier: $ident")
                    }
                } else {
                    error("Unexpected symbol: $ch")
                }
            }
        }
    }

    internal enum class Nonterminal {
        VAR, CONST, ADD, SUB, LT, SET, IF1, IF2, WHILE, DO, EMPTY, SEQ, EXPR, PROG
    }

    internal class Node(var kind: Nonterminal, var value: Int, var op1: Node?, var op2: Node?) {
        var op3: Node? = null
    }

    internal class Parser(val lexer: Lexer) {
        fun error(message: String) {
            throw RuntimeException("Parser error: $message")
        }

        fun term(): Node {
            return if (lexer.sym == Lexeme.ID) {
                val node =
                    Node(Nonterminal.VAR, lexer.value, null, null)
                lexer.readNextToken()
                node
            } else if (lexer.sym == Lexeme.NUM) {
                val node = Node(
                    Nonterminal.CONST,
                    lexer.value,
                    null,
                    null
                )
                lexer.readNextToken()
                node
            } else {
                parenExpr()
            }
        }

        fun summa(): Node {
            var node = term()
            while (lexer.sym == Lexeme.PLUS || lexer.sym == Lexeme.MINUS) {
                val kind = if (lexer.sym == Lexeme.PLUS) Nonterminal.ADD else Nonterminal.SUB
                lexer.readNextToken()
                node = Node(kind, 0, node, term())
            }
            return node
        }

        fun test(): Node {
            var node = summa()
            if (lexer.sym == Lexeme.LESS) {
                lexer.readNextToken()
                node = Node(Nonterminal.LT, 0, node, summa())
            }
            return node
        }

        fun expr(): Node {
            if (lexer.sym != Lexeme.ID) {
                return test()
            }
            var node = test()
            if (node.kind == Nonterminal.VAR && lexer.sym == Lexeme.EQUAL) {
                lexer.readNextToken()
                node = Node(Nonterminal.SET, 0, node, expr())
            }
            return node
        }

        fun parenExpr(): Node {
            if (lexer.sym != Lexeme.LPAR) {
                error("'(' expected")
            }
            lexer.readNextToken()
            val node = expr()
            if (lexer.sym != Lexeme.RPAR) {
                error("')' expected")
            }
            lexer.readNextToken()
            return node
        }

        fun statement(): Node {
            var node: Node
            if (lexer.sym == Lexeme.IF) {
                node = Node(Nonterminal.IF1, 0, null, null)
                lexer.readNextToken()
                node.op1 = parenExpr()
                node.op2 = statement()
                if (lexer.sym == Lexeme.ELSE) {
                    node.kind = Nonterminal.IF2
                    lexer.readNextToken()
                    node.op3 = statement()
                }
            } else if (lexer.sym == Lexeme.WHILE) {
                node = Node(Nonterminal.WHILE, 0, null, null)
                lexer.readNextToken()
                node.op1 = parenExpr()
                node.op2 = statement()
            } else if (lexer.sym == Lexeme.DO) {
                node = Node(Nonterminal.DO, 0, null, null)
                lexer.readNextToken()
                node.op1 = statement()
                if (lexer.sym != Lexeme.WHILE) {
                    error("'while' expected")
                }
                lexer.readNextToken()
                node.op2 = parenExpr()
                if (lexer.sym != Lexeme.SEMICOLON) {
                    error("';' expected")
                }
            } else if (lexer.sym == Lexeme.SEMICOLON) {
                node = Node(Nonterminal.EMPTY, 0, null, null)
                lexer.readNextToken()
            } else if (lexer.sym == Lexeme.LBRA) {
                node = Node(Nonterminal.EMPTY, 0, null, null)
                lexer.readNextToken()
                while (lexer.sym != Lexeme.RBRA) {
                    node = Node(Nonterminal.SEQ, 0, node, statement())
                }
                lexer.readNextToken()
            } else {
                node = Node(Nonterminal.EXPR, 0, expr(), null)
                if (lexer.sym != Lexeme.SEMICOLON) {
                    error("';' expected")
                }
                lexer.readNextToken()
            }
            return node
        }

        fun parse(): Node {
            lexer.readNextToken()
            val node = Node(Nonterminal.PROG, 0, statement(), null)
            if (lexer.sym != Lexeme.EOF) {
                error("Invalid statement syntax")
            }
            return node
        }
    }

    internal enum class Command {
        IFETCH, ISTORE, IPUSH, IPOP, IADD, ISUB, ILT, JZ, JNZ, JMP, HALT
    }

    internal class Compiler {
        fun compile(node: Node?): List<Object> {
            val program: List<Object> = ArrayList()
            when (node!!.kind) {
                Nonterminal.VAR -> {
                    program.add(Command.IFETCH)
                    program.add(node.value)
                }
                Nonterminal.CONST -> {
                    program.add(Command.IPUSH)
                    program.add(node.value)
                }
                Nonterminal.ADD -> {
                    compile(node.op1)
                    compile(node.op2)
                    program.add(Command.IADD)
                }
                Nonterminal.SUB -> {
                    compile(node.op1)
                    compile(node.op2)
                    program.add(Command.ISUB)
                }
                Nonterminal.LT -> {
                    compile(node.op1)
                    compile(node.op2)
                    program.add(Command.ILT)
                }
                Nonterminal.SET -> {
                    compile(node.op2)
                    program.add(Command.ISTORE)
                    program.add(node.op1!!.value)
                }
                Nonterminal.IF1 -> {
                    compile(node.op1)
                    program.add(Command.JZ)
                    val addr: Int = program.size()
                    program.add(0)
                    compile(node.op2)
                    program.set(addr, program.size())
                }
                Nonterminal.IF2 -> {
                    compile(node.op1)
                    program.add(Command.JZ)
                    val addr1: Int = program.size()
                    program.add(0)
                    compile(node.op2)
                    program.add(Command.JMP)
                    val addr2: Int = program.size()
                    program.add(0)
                    program.set(addr1, program.size())
                    compile(node.op3)
                    program.set(addr2, program.size())
                }
                Nonterminal.WHILE -> {
                    val addr1: Int = program.size()
                    compile(node.op1)
                    program.add(Command.JZ)
                    val addr2: Int = program.size()
                    program.add(0)
                    compile(node.op2)
                    program.add(Command.JMP)
                    program.add(addr1)
                    program.set(addr2, program.size())
                }
                Nonterminal.DO -> {
                    val addr: Int = program.size()
                    compile(node.op1)
                    compile(node.op2)
                    program.add(Command.JNZ)
                    program.add(addr)
                }
                Nonterminal.SEQ -> {
                    compile(node.op1)
                    compile(node.op2)
                }
                Nonterminal.EXPR -> {
                    compile(node.op1)
                    program.add(Command.IPOP)
                }
                Nonterminal.PROG -> {
                    compile(node.op1)
                    program.add(Command.HALT)
                }
            }
            return program
        }
    }

    internal class VirtualMachine {
        fun run(program: List<Object>) {
            val `var` = IntArray(26)
            val stack: Stack<Integer> = Stack()
            var pc = 0
            m1@ while (true) {
                val op = program[pc] as Command
                val arg = if (pc < program.size() - 1 && program[pc + 1] is Integer) program[pc + 1] else 0
                when (op) {
                    Command.IFETCH -> {
                        stack.push(`var`[arg])
                        pc += 2
                    }
                    Command.ISTORE -> {
                        `var`[arg] = stack.pop()
                        pc += 2
                    }
                    Command.IPUSH -> {
                        stack.push(arg)
                        pc += 2
                    }
                    Command.IPOP -> {
                        stack.push(arg)
                        stack.pop()
                        pc += 1
                    }
                    Command.IADD -> {
                        stack.push(stack.pop() + stack.pop())
                        pc += 1
                    }
                    Command.ISUB -> {
                        stack.push(-stack.pop() + stack.pop())
                        pc += 1
                    }
                    Command.ILT -> {
                        stack.push(if (stack.pop() > stack.pop()) 1 else 0)
                        pc += 1
                    }
                    Command.JZ -> if (stack.pop() === 0) {
                        pc = arg
                    } else {
                        pc += 2
                    }
                    Command.JNZ -> if (stack.pop() !== 0) {
                        pc = arg
                    } else {
                        pc += 2
                    }
                    Command.JMP -> pc = arg
                    Command.HALT -> break@m1
                }
            }
            System.out.println("Execution finished.")
            for (i in `var`.indices) {
                if (`var`[i] != 0) {
                    System.out.println(('a'.toInt() + i) as Char.toString() + " = " + `var`[i])
                }
            }
        }
    }
}
