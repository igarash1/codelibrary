package misc

object BinarySearch {
    // 000[1]11
    // warning: overflows in lines 1-4
    // invariant: f[lo] == false, f[hi] == true
    fun binarySearchFirstTrueSimple(f: IntPredicate, fromInclusive: Int, toInclusive: Int): Int {
        var lo = fromInclusive - 1
        var hi = toInclusive + 1
        while (hi - lo > 1) {
            val mid = (lo + hi) / 2
            if (!f.test(mid)) {
                lo = mid
            } else {
                hi = mid
            }
        }
        return hi
    }

    // 000[1]11
    // correct binary search
    fun binarySearchFirstTrue(f: IntPredicate, fromInclusive: Int, toExclusive: Int): Int {
        var lo = fromInclusive
        var hi = toExclusive
        while (lo < hi) {
            // int mid = lo + ((hi - lo) >>> 1);
            val mid = (lo and hi) + (lo xor hi shr 1)
            if (!f.test(mid)) {
                lo = mid + 1
            } else {
                hi = mid
            }
        }
        return hi
    }

    fun binarySearch(f: DoublePredicate, lo: Double, hi: Double): Double {
        var lo = lo
        var hi = hi
        for (step in 0..999) {
            val mid = (lo + hi) / 2
            if (!f.test(mid)) {
                lo = mid
            } else {
                hi = mid
            }
        }
        return hi
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..99999) {
            val n: Int = rnd.nextInt(20)
            val b = BooleanArray(n)
            val firstTrue: Int = rnd.nextInt(n + 1)
            Arrays.fill(b, firstTrue, n, true)
            val res1 = binarySearchFirstTrueSimple(IntPredicate { i -> b[i] }, 0, n - 1)
            val res2 = binarySearchFirstTrue(IntPredicate { i -> b[i] }, 0, n)
            if (res1 != firstTrue || res1 != res2) throw RuntimeException()
        }
        System.out.println(Math.sqrt(2) === binarySearch(DoublePredicate { x -> x * x >= 2 }, 0.0, 2.0))
    }
}
