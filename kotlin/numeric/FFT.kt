package numeric

import java.math.BigInteger

// Fast Fourier transform
// https://cp-algorithms.com/algebra/fft.html
// https://drive.google.com/file/d/1B9BIfATnI_qL6rYiE5hY9bh20SMVmHZ7/view
object FFT {
    // precondition: a.length is a power of 2 and a.length == b.length
    fun fft(a: DoubleArray, b: DoubleArray, inverse: Boolean) {
        val n = a.size
        val shift: Int = 32 - Integer.numberOfTrailingZeros(n)
        for (i in 1 until n) {
            val j: Int = Integer.reverse(i shl shift)
            if (i < j) {
                var temp = a[i]
                a[i] = a[j]
                a[j] = temp
                temp = b[i]
                b[i] = b[j]
                b[j] = temp
            }
        }
        var len = 2
        while (len <= n) {
            val halfLen = len shr 1
            val cs = DoubleArray(halfLen)
            val sn = DoubleArray(halfLen)
            for (i in 0 until halfLen) {
                val angle: Double = 2 * Math.PI * i / len * if (inverse) -1 else 1
                cs[i] = Math.cos(angle)
                sn[i] = Math.sin(angle)
            }
            var i = 0
            while (i < n) {
                for (j in 0 until halfLen) {
                    val uA = a[i + j]
                    val uB = b[i + j]
                    val vA = a[i + j + halfLen] * cs[j] - b[i + j + halfLen] * sn[j]
                    val vB = a[i + j + halfLen] * sn[j] + b[i + j + halfLen] * cs[j]
                    a[i + j] = uA + vA
                    b[i + j] = uB + vB
                    a[i + j + halfLen] = uA - vA
                    b[i + j + halfLen] = uB - vB
                }
                i += len
            }
            len = len shl 1
        }
        if (inverse) {
            for (i in 0 until n) {
                a[i] /= n
                b[i] /= n
            }
        }
    }

    fun multiplyBigint(a: IntArray, b: IntArray): IntArray {
        val need = a.size + b.size
        val n: Int = Integer.highestOneBit(need - 1) shl 1
        val pReal = DoubleArray(n)
        val pImag = DoubleArray(n)
        // p(x) = a(x) + i*b(x)
        for (i in a.indices) pReal[i] = a[i].toDouble()
        for (i in b.indices) pImag[i] = b[i].toDouble()
        fft(pReal, pImag, false)
        val abReal = DoubleArray(n)
        val abImag = DoubleArray(n)
        // a[w[k]] = (p[w[k]] + conj(p[w[n-k]])) / 2
        // b[w[k]] = (p[w[k]] - conj(p[w[n-k]])) / (2*i)
        // ab[w[k]] = (p[w[k]]*p[w[k]] - conj(p[w[n-k]]*p[w[n-k]])) / (4*i)
        for (i in 0 until n) {
            val j: Int = n - i and n - 1
            abReal[i] = (pReal[i] * pImag[i] + pReal[j] * pImag[j]) / 2
            abImag[i] = (pReal[j] * pReal[j] - pImag[j] * pImag[j] - (pReal[i] * pReal[i] - pImag[i] * pImag[i])) / 4
        }
        fft(abReal, abImag, true)
        val result = IntArray(need)
        var i = 0
        var carry = 0
        while (i < need) {
            result[i] = (abReal[i] + 0.5).toInt() + carry
            carry = result[i] / 10
            result[i] %= 10
            i++
        }
        return result
    }

    fun multiplyMod(a: IntArray, b: IntArray, m: Int): IntArray {
        val need = a.size + b.size - 1
        val n: Int = Math.max(1, Integer.highestOneBit(need - 1) shl 1)
        val aReal = DoubleArray(n)
        val aImag = DoubleArray(n)
        for (i in a.indices) {
            val x = (a[i] % m + m) % m
            aReal[i] = (x and (1 shl 15) - 1).toDouble()
            aImag[i] = (x shr 15).toDouble()
        }
        fft(aReal, aImag, false)
        val bReal = DoubleArray(n)
        val bImag = DoubleArray(n)
        for (i in b.indices) {
            val x = (b[i] % m + m) % m
            bReal[i] = (x and (1 shl 15) - 1).toDouble()
            bImag[i] = (x shr 15).toDouble()
        }
        fft(bReal, bImag, false)
        val faReal = DoubleArray(n)
        val faImag = DoubleArray(n)
        val fbReal = DoubleArray(n)
        val fbImag = DoubleArray(n)
        for (i in 0 until n) {
            val j: Int = n - i and n - 1
            val a1r = (aReal[i] + aReal[j]) / 2
            val a1i = (aImag[i] - aImag[j]) / 2
            val a2r = (aImag[i] + aImag[j]) / 2
            val a2i = (aReal[j] - aReal[i]) / 2
            val b1r = (bReal[i] + bReal[j]) / 2
            val b1i = (bImag[i] - bImag[j]) / 2
            val b2r = (bImag[i] + bImag[j]) / 2
            val b2i = (bReal[j] - bReal[i]) / 2
            faReal[i] = a1r * b1r - a1i * b1i - a2r * b2i - a2i * b2r
            faImag[i] = a1r * b1i + a1i * b1r + a2r * b2r - a2i * b2i
            fbReal[i] = a1r * b2r - a1i * b2i + a2r * b1r - a2i * b1i
            fbImag[i] = a1r * b2i + a1i * b2r + a2r * b1i + a2i * b1r
        }
        fft(faReal, faImag, true)
        fft(fbReal, fbImag, true)
        val res = IntArray(need)
        for (i in 0 until need) {
            val aa = (faReal[i] + 0.5).toLong()
            val bb = (fbReal[i] + 0.5).toLong()
            val cc = (faImag[i] + 0.5).toLong()
            res[i] = ((aa % m + (bb % m shl 15) + (cc % m shl 30)) % m).toInt()
        }
        return res
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..9999) {
            val n1: Int = rnd.nextInt(10) + 1
            var s1 = ""
            val a = IntArray(n1)
            for (i in 0 until n1) {
                val x: Int = rnd.nextInt(10)
                s1 = x + s1
                a[i] = x
            }
            val n2: Int = rnd.nextInt(10) + 1
            var s2 = ""
            val b = IntArray(n2)
            for (i in 0 until n2) {
                val x: Int = rnd.nextInt(10)
                s2 = x + s2
                b[i] = x
            }
            val res1 = multiplyBigint(a, b)
            val res2: IntArray = FFT_slow.multiplyBigint(a, b)
            var s = ""
            for (v in res1) {
                s = v + s
            }
            val mul: BigInteger = BigInteger(s1).multiply(BigInteger(s2))
            if (!Arrays.equals(res1, res2) || !mul.equals(BigInteger(s))) throw RuntimeException()
        }
        System.out.println(Arrays.toString(multiplyMod(intArrayOf(1, 2), intArrayOf(2, 15), 991992993)))
    }
}
