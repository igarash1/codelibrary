package numbertheory

import java.util.Arrays

// f(a*b) = f(a)*f(b) | gcd(a,b)=1
interface MultiplicativeFunction {
    fun apply(prime: Long, exponent: Int, power: Long): Long
    operator fun get(x: Long): Long {
        var x = x
        var res: Long = 1
        var d: Long = 2
        while (d * d <= x) {
            if (x % d == 0L) {
                var exp = 0
                var power: Long = 1
                do {
                    ++exp
                    power *= d
                    x /= d
                } while (x % d == 0L)
                res *= apply(d, exp, power)
            }
            d++
        }
        if (x != 1L) res *= apply(x, 1, x)
        return res
    }

    fun generateValues(n: Int): LongArray {
        val divisor = generateDivisors(n)
        val res = LongArray(n + 1)
        res[1] = 1
        for (i in 2..n) {
            var j = i
            var exp = 0
            do {
                j /= divisor[i]
                ++exp
            } while (j % divisor[i] == 0)
            res[i] = res[j] * apply(divisor[i].toLong(), exp, (i / j).toLong())
        }
        return res
    }

    companion object {
        fun generateDivisors(n: Int): IntArray {
            val divisors: IntArray = IntStream.range(0, n + 1).toArray()
            var i = 2
            while (i * i <= n) {
                if (divisors[i] == i) {
                    var j = i * i
                    while (j <= n) {
                        divisors[j] = i
                        j += i
                    }
                }
                i++
            }
            return divisors
        }

        // Usage example
        fun main(args: Array<String?>?) {
            System.out.println(1L == MOBIUS[1])
            System.out.println(-1L == MOBIUS[2])
            System.out.println(Arrays.toString(MOBIUS.generateValues(10)))
        }

        val PHI: MultiplicativeFunction = MultiplicativeFunction { p: Long, exp: Int, power: Long -> power - power / p }
        val MOBIUS: MultiplicativeFunction =
            MultiplicativeFunction { p: Long, exp: Int, power: Long -> if (exp == 1) -1 else 0 }
        val DIVISOR_COUNT: MultiplicativeFunction = MultiplicativeFunction { p: Long, exp: Int, power: Long -> exp + 1 }
        val DIVISOR_SUM: MultiplicativeFunction =
            MultiplicativeFunction { p: Long, exp: Int, power: Long -> (power * p - 1) / (p - 1) }
    }
}
