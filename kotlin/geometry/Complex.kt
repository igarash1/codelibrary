package geometry

import numeric.FFT
import optimization.Simplex

class Complex {
    val x: Double
    val y: Double

    constructor(x: Double) {
        this.x = x
        y = 0.0
    }

    constructor(x: Double, y: Double) {
        this.x = x
        this.y = y
    }

    fun conj(): Complex {
        return Complex(x, -y)
    }

    fun sub(b: Complex): Complex {
        return Complex(x - b.x, y - b.y)
    }

    fun add(b: Complex): Complex {
        return Complex(x + b.x, y + b.y)
    }

    fun mul(b: Complex): Complex {
        return Complex(x * b.x - y * b.y, x * b.y + y * b.x)
    }

    operator fun div(b: Complex): Complex {
        return this.mul(b.conj()).mul(1 / b.len2())
    }

    fun mul(b: Double): Complex {
        return Complex(x * b, y * b)
    }

    fun len2(): Double {
        return x * x + y * y
    }

    fun abs(): Double {
        return Math.sqrt(x * x + y * y)
    }

    fun norm(): Complex {
        return if (abs() == 0.0) Complex(0, 0) else mul(1 / abs())
    }

    fun cross(b: Complex): Double {
        return x * b.y - y * b.x
    }

    fun cross2(b: Complex): Double {
        return conj().mul(b).y
    }

    fun dot(b: Complex): Double {
        return x * b.x + y * b.y
    }

    fun dot2(b: Complex): Double {
        return conj().mul(b).x
    }

    fun arg(): Double {
        return Math.atan2(y, x)
    }

    fun rot90(): Complex {
        return Complex(-y, x)
    }

    fun rotate(p: Complex, angle: Double): Complex {
        return p.sub(this).mul(exp(Complex(0, angle))).add(this)
    }

    fun rotate2(p: Complex, angle: Double): Complex {
        var p = p
        p = p.sub(this)
        val cs: Double = Math.cos(angle)
        val sn: Double = Math.sin(angle)
        return Complex(p.x * cs - p.y * sn, p.x * sn + p.y * cs).add(this)
    }

    fun reflect(p: Complex, q: Complex): Complex {
        val s = q.sub(p)
        return sub(p).div(s).conj().mul(s).add(p)
    }

    fun proj(p: Complex): Double {
        return dot(p) / abs()
    }

    @Override
    override fun toString(): String {
        return "Complex [x=$x, y=$y]"
    }

    companion object {
        fun polar(r: Double, theta: Double): Complex {
            return Complex(r * Math.cos(theta), r * Math.sin(theta))
        }

        fun exp(a: Complex): Complex {
            return polar(Math.exp(a.x), a.y)
        }

        fun angle(a: Complex, p: Complex, b: Complex): Double {
            var a = a
            var b = b
            a = a.sub(p)
            b = b.sub(p)
            return Math.atan2(a.cross(b), a.dot(b))
        }

        // Usage example
        fun main(args: Array<String?>?) {
            var z = Complex(3, 2)
            z = z.div(z)
            System.out.println(z)
            System.out.println()
            val u = Complex(0, 0)
            val v = Complex(1, 0)
            val a = u.rotate(v, Math.PI * 1.0)
            val b = v.rot90().rot90()
            System.out.println(a)
            System.out.println(b)
        }
    }
}
