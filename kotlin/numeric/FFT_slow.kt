package numeric

import numeric.FFT
import optimization.Simplex

// Fast Fourier transform
// https://cp-algorithms.com/algebra/fft.html
object FFT_slow {
    fun multiplyBigint(a: IntArray, b: IntArray): IntArray {
        val need = a.size + b.size
        val n: Int = Integer.highestOneBit(need - 1) shl 1
        val aReal = DoubleArray(n)
        val aImag = DoubleArray(n)
        val bReal = DoubleArray(n)
        val bImag = DoubleArray(n)
        for (i in a.indices) aReal[i] = a[i].toDouble()
        for (i in b.indices) bReal[i] = b[i].toDouble()
        FFT.fft(aReal, aImag, false)
        FFT.fft(bReal, bImag, false)
        for (i in 0 until n) {
            val real = aReal[i] * bReal[i] - aImag[i] * bImag[i]
            aImag[i] = aImag[i] * bReal[i] + bImag[i] * aReal[i]
            aReal[i] = real
        }
        FFT.fft(aReal, aImag, true)
        val result = IntArray(need)
        var i = 0
        var carry = 0
        while (i < need) {
            result[i] = (aReal[i] + 0.5).toInt() + carry
            carry = result[i] / 10
            result[i] %= 10
            i++
        }
        return result
    }
}
