package numeric

class Fraction(a: Long, b: Long) : Comparable<Fraction?> {
    var a: Long
    var b: Long
    operator fun plus(o: Fraction): Fraction {
        return Fraction(a * o.b + o.a * b, b * o.b)
    }

    operator fun minus(o: Fraction): Fraction {
        return Fraction(a * o.b - o.a * b, b * o.b)
    }

    fun mul(o: Fraction): Fraction {
        return Fraction(a * o.a, b * o.b)
    }

    operator fun div(o: Fraction): Fraction {
        return Fraction(a * o.b, b * o.a)
    }

    @Override
    operator fun compareTo(o: Fraction): Int {
        return Long.compare(a * o.b, b * o.a)
    }

    @Override
    override fun toString(): String {
        return "$a/$b"
    }

    companion object {
        fun gcd(a: Long, b: Long): Long {
            return if (b == 0L) a else gcd(b, a % b)
        }

        // Usage example
        fun main(args: Array<String?>?) {
            val a = Fraction(1, 2)
            val b = Fraction(3, 5)
            System.out.println(a.plus(b))
            System.out.println(a.minus(b))
            System.out.println(a.mul(b))
            System.out.println(a.div(b))
        }
    }

    init {
        var a = a
        var b = b
        if (b < 0) {
            a = -a
            b = -b
        }
        val g = gcd(Math.abs(a), b)
        if (g != 0L) {
            a /= g
            b /= g
        }
        this.a = a
        this.b = b
    }
}
