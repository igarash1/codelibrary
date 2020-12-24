package numbertheory

import java.math.BigInteger

object Euclid {
    fun gcd(a: Int, b: Int): Int {
        return if (b == 0) Math.abs(a) else gcd(b, a % b)
    }

    fun gcd2(a: Int, b: Int): Int {
        var a = a
        var b = b
        while (b != 0) {
            val t = b
            b = a % b
            a = t
        }
        return Math.abs(a)
    }

    fun lcm(a: Int, b: Int): Int {
        return Math.abs(a / gcd(a, b) * b)
    }

    // returns { gcd(a,b), x, y } such that gcd(a,b) = a*x + b*y
    fun euclid(a: Long, b: Long): LongArray {
        var a = a
        var b = b
        var x: Long = 1
        var y: Long = 0
        var x1: Long = 0
        var y1: Long = 1
        // invariant: a=a_orig*x+b_orig*y, b=a_orig*x1+b_orig*y1
        while (b != 0L) {
            val q = a / b
            val _x1 = x1
            val _y1 = y1
            val _b = b
            x1 = x - q * x1
            y1 = y - q * y1
            b = a - q * b
            x = _x1
            y = _y1
            a = _b
        }
        return if (a > 0) longArrayOf(a, x, y) else longArrayOf(-a, -x, -y)
    }

    fun euclid2(a: Long, b: Long): LongArray {
        if (b == 0L) return if (a > 0) longArrayOf(a, 1, 0) else longArrayOf(-a, -1, 0)
        val r = euclid2(b, a % b)
        return longArrayOf(r[0], r[2], r[1] - a / b * r[2])
    }

    fun mod(a: Int, m: Int): Int {
        var a = a
        a %= m
        return if (a >= 0) a else a + m
    }

    fun mod(a: Long, m: Int): Int {
        var a = a
        a %= m.toLong()
        return (if (a >= 0) a else a + m).toInt()
    }

    // precondition: m > 1 && gcd(a, m) = 1
    fun modInverse(a: Int, m: Int): Int {
        var a = a
        a = mod(a, m)
        return if (a == 0) 0 else mod(
            ((1 - modInverse(m, a).toLong() * m) / a).toInt(), m
        )
    }

    // precondition: m > 0 && gcd(a, m) = 1
    fun modInverse2(a: Int, m: Int): Int {
        return mod(euclid(a.toLong(), m.toLong())[1].toInt(), m)
    }

    // precondition: p is prime
    fun generateInverses(p: Int): IntArray {
        val res = IntArray(p)
        res[1] = 1
        for (i in 2 until p) res[i] = ((p - p / i) as Long * res[p % i] % p).toInt()
        return res
    }

    // returns x ≡ a[i] (mod p[i]), where gcd(p[i], p[j]) == 1
    fun garnerRestore(a: IntArray, p: IntArray): BigInteger {
        val x: IntArray = a.clone()
        for (i in x.indices) for (j in 0 until i) x[i] = mod(
            BigInteger.valueOf(p[j]).modInverse(BigInteger.valueOf(p[i])).longValue() * (x[i] - x[j]), p[i]
        )
        var res: BigInteger = BigInteger.valueOf(x[0])
        var m: BigInteger = BigInteger.ONE
        for (i in 1 until x.size) {
            m = m.multiply(BigInteger.valueOf(p[i - 1]))
            res = res.add(m.multiply(BigInteger.valueOf(x[i])))
        }
        return res
    }

    // returns x ≡ a[i] (mod p[i]), where gcd(p[i], p[j]) == 1
    fun simpleRestore(a: IntArray, p: IntArray): Int {
        var res = 0
        var i = 0
        var m = 1
        while (i < a.size) {
            while (res % p[i] != a[i]) res += m
            i++
            m *= p[i]
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (steps in 0..9999) {
            val a: Int = rnd.nextInt(20) - 10
            val b: Int = rnd.nextInt(20) - 10
            val xa: BigInteger = BigInteger.valueOf(a)
            val xb: BigInteger = BigInteger.valueOf(b)
            val gcd1 = gcd(a, b).toLong()
            val gcd2 = gcd2(a, b).toLong()
            val gcd: Long = xa.gcd(xb).longValue()
            val euclid1 = euclid(a.toLong(), b.toLong())
            val euclid2 = euclid2(a.toLong(), b.toLong())
            var inv1 = 0
            var inv2 = 0
            var inv = 0
            if (gcd == 1L && b > 0) {
                inv1 = modInverse(a, b)
                inv2 = modInverse2(a, b)
                inv = xa.modInverse(xb).intValue()
            }
            if (gcd1 != gcd || gcd2 != gcd || !Arrays.equals(
                    euclid1,
                    euclid2
                ) || euclid1[0] != gcd || inv1 != inv || inv2 != inv
            ) {
                System.err.println("$a $b")
            }
        }
        val a: Long = 6
        val b: Long = 9
        val res = euclid(a, b)
        System.out.println(
            res[1].toString() + " * (" + a + ") "
                    + " + " + res[2] + " * (" + b + ") = gcd(" + a + "," + b + ") = " + res[0]
        )
        System.out.println(Arrays.toString(generateInverses(7)))
        System.out.println(garnerRestore(intArrayOf(200000125, 300000333), intArrayOf(1000000007, 1000000009)))
    }
}
