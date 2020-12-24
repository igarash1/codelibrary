package misc

import java.util.function.IntFunction

object FunctionCycleDetection {
    fun floyd(f: IntFunction<Integer?>, x0: Int): IntArray {
        var tortoise: Int = f.apply(x0)
        var hare: Int = f.apply(f.apply(x0))
        while (tortoise != hare) {
            tortoise = f.apply(tortoise)
            hare = f.apply(f.apply(hare))
        }
        var startPos = 0
        tortoise = x0
        while (tortoise != hare) {
            tortoise = f.apply(tortoise)
            hare = f.apply(hare)
            ++startPos
        }
        var length = 1
        hare = f.apply(tortoise)
        while (tortoise != hare) {
            hare = f.apply(hare)
            ++length
        }
        return intArrayOf(startPos, length)
    }

    fun brent(f: IntFunction<Integer?>, x0: Int): IntArray {
        var power = 1
        var length = 1
        var tortoise = x0
        var hare: Int = f.apply(x0)
        while (tortoise != hare) {
            if (power == length) {
                tortoise = hare
                power *= 2
                length = 0
            }
            hare = f.apply(hare)
            ++length
        }
        var startPos = 0
        tortoise = x0
        hare = x0
        for (i in 0 until length) hare = f.apply(hare)
        while (tortoise != hare) {
            tortoise = f.apply(tortoise)
            hare = f.apply(hare)
            ++startPos
        }
        return intArrayOf(startPos, length)
    }

    // random test
    fun main(args: Array<String?>?) {
        val rnd = Random(1)
        for (step in 0..99999) {
            val a: Int = rnd.nextInt(100)
            val b: Int = rnd.nextInt(100)
            val c: Int = rnd.nextInt(100) + 1
            val f: IntFunction<Integer?> = IntFunction<Integer> { x -> (a * x * x + b) % c }
            val floyd = floyd(f, 0)
            val brent = brent(f, 0)
            if (!Arrays.equals(floyd, brent)) throw RuntimeException()
        }
        System.out.println(floyd(IntFunction<Integer> { x -> (41 * x + 1) % 1000000 }, 0)[1])
    }
}
