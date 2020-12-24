package geometry

class Vector2d(var x: Double, var y: Double) {
    fun conj(): Vector2d {
        return Vector2d(x, -y)
    }

    fun sub(b: Vector2d): Vector2d {
        return Vector2d(x - b.x, y - b.y)
    }

    fun add(b: Vector2d): Vector2d {
        return Vector2d(x + b.x, y + b.y)
    }

    fun mul(b: Vector2d): Vector2d {
        return Vector2d(x * b.x - y * b.y, x * b.y + y * b.x)
    }

    operator fun div(b: Vector2d): Vector2d {
        return this.mul(b.conj()).mul(1 / b.len2())
    }

    fun mul(b: Double): Vector2d {
        return Vector2d(x * b, y * b)
    }

    fun len2(): Double {
        return x * x + y * y
    }

    fun len(): Double {
        return Math.sqrt(x * x + y * y)
    }

    fun norm(): Vector2d {
        return if (len() == 0.0) Vector2d(0, 0) else mul(1 / len())
    }

    fun cross(b: Vector2d): Double {
        return x * b.y - y * b.x
    }

    fun dot(b: Vector2d): Double {
        return x * b.x + y * b.y
    }

    fun rot(): Vector2d {
        return Vector2d(-y, x)
    }

    fun proj(p: Vector2d): Double {
        return dot(p) / len()
    }

    fun rotate(p: Vector2d, angle: Double): Vector2d {
        return p.sub(this).mul(exp(Vector2d(0, angle))).add(this)
    }

    fun rotate2(p: Vector2d, angle: Double): Vector2d {
        var p = p
        p = p.sub(this)
        val cs: Double = Math.cos(angle)
        val sn: Double = Math.sin(angle)
        return Vector2d(p.x * cs - p.y * sn, p.x * sn + p.y * cs).add(this)
    }

    fun reflect(p: Vector2d, q: Vector2d): Vector2d {
        val s = q.sub(p)
        return sub(p).div(s).conj().mul(s).add(p)
    }

    @Override
    override fun toString(): String {
        return "Vector2d [x=$x, y=$y]"
    }

    companion object {
        fun polar(r: Double, theta: Double): Vector2d {
            return Vector2d(r * Math.cos(theta), r * Math.sin(theta))
        }

        fun exp(a: Vector2d): Vector2d {
            return polar(Math.exp(a.x), a.y)
        }

        // Usage example
        fun main(args: Array<String?>?) {
            val u = Vector2d(0, 0)
            val v = Vector2d(1, 0)
            val a = u.rotate(v, Math.PI * 1.0)
            val b = v.rot().rot()
            System.out.println(a)
            System.out.println(b)
        }
    }
}
