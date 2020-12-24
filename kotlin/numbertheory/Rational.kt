package numbertheory

import java.math.BigInteger

class Rational : Comparable<Rational?> {
    val num: BigInteger
    val den: BigInteger

    constructor(num: BigInteger, den: BigInteger) {
        val gcd: BigInteger = num.gcd(den)
        val g: BigInteger = if (den.signum() > 0) gcd else if (den.signum() < 0) gcd.negate() else BigInteger.ONE
        this.num = num.divide(g)
        this.den = den.divide(g)
    }

    constructor(num: Long, den: Long) : this(BigInteger.valueOf(num), BigInteger.valueOf(den)) {}
    constructor(num: Long) {
        this.num = BigInteger.valueOf(num)
        den = BigInteger.ONE
    }

    fun add(r: Rational): Rational {
        return Rational(num.multiply(r.den).add(r.num.multiply(den)), den.multiply(r.den))
    }

    fun sub(r: Rational): Rational {
        return Rational(num.multiply(r.den).subtract(r.num.multiply(den)), den.multiply(r.den))
    }

    fun mul(r: Rational): Rational {
        return Rational(num.multiply(r.num), den.multiply(r.den))
    }

    operator fun div(r: Rational): Rational {
        return Rational(num.multiply(r.den), den.multiply(r.num))
    }

    fun negate(): Rational {
        return Rational(num.negate(), den)
    }

    fun inverse(): Rational {
        return Rational(den, num)
    }

    fun abs(): Rational {
        return Rational(num.abs(), den)
    }

    fun signum(): Int {
        return num.signum()
    }

    fun doubleValue(): Double {
        return num.doubleValue() / den.doubleValue()
    }

    fun longValue(): Long {
        return num.longValue() / den.longValue()
    }

    operator fun compareTo(other: Rational): Int {
        return num.multiply(other.den).compareTo(other.num.multiply(den))
    }

    override fun equals(obj: Object): Boolean {
        return num.equals((obj as Rational).num) && den.equals((obj as Rational).den)
    }

    override fun hashCode(): Int {
        return num.hashCode() * 31 + den.hashCode()
    }

    override fun toString(): String {
        return num.toString() + "/" + den
    }

    companion object {
        val ZERO = Rational(0)
        val ONE = Rational(1)
        val POSITIVE_INFINITY: Rational = Rational(1, 0)
        val NEGATIVE_INFINITY: Rational = Rational(-1, 0)

        // Usage example
        fun main(args: Array<String?>?) {
            val a: Rational = Rational(1, 3)
            val b: Rational = Rational(1, 6)
            val c: Rational = Rational(1, 2)
            System.out.println(true == c.equals(a.add(b)))
            val d: Rational = Rational(1, -2)
            System.out.println(d)
        }
    }
}
