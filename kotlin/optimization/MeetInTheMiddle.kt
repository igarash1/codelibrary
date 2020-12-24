package optimization

import java.util.Arrays

object MeetInTheMiddle {
    fun sumsLowerBound(a: LongArray, b: Long): Long {
        val n = a.size
        val sizeL = 1 shl n / 2
        val sizeR = 1 shl n - n / 2
        val sumsL = LongArray(sizeL)
        val sumsR = LongArray(sizeR)
        for (i in 0 until sizeL) for (j in 0 until n / 2) if (i and (1 shl j) > 0) sumsL[i] += a[j]
        for (i in 0 until sizeR) for (j in 0 until n - n / 2) if (i and (1 shl j) > 0) sumsR[i] += a[j + n / 2]
        Arrays.sort(sumsL)
        Arrays.sort(sumsR)
        var left = 0
        var right = sizeR - 1
        var cur = Long.MIN_VALUE
        while (left < sizeL && right >= 0) {
            if (sumsL[left] + sumsR[right] <= b) {
                cur = Math.max(cur, sumsL[left] + sumsR[right])
                ++left
            } else {
                --right
            }
        }
        return cur
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val a = longArrayOf(1, 2, 5)
        System.out.println(3L == sumsLowerBound(a, 4))
    }
}
