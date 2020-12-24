package numbertheory

import java.util.stream.Collectors.summingInt

object Factorization {
    // returns prime_divisor -> power
    // O(sqrt(n)) complexity
    fun factorize(n: Long): Map<Long, Integer> {
        var n = n
        val factors: List<Long> = ArrayList()
        var d: Long = 2
        while (d * d <= n) {
            while (n % d == 0L) {
                factors.add(d)
                n /= d
            }
            d++
        }
        if (n > 1) {
            factors.add(n)
        }
        return factors.stream().collect(Collectors.groupingBy(Function.identity(), summingInt { v -> 1 }))
    }

    fun getAllDivisors(n: Int): IntArray {
        val divisors: List<Integer> = ArrayList()
        var d = 1
        while (d * d <= n) {
            if (n % d == 0) {
                divisors.add(d)
                if (d * d != n) divisors.add(n / d)
            }
            d++
        }
        return divisors.stream().sorted().mapToInt(Integer::valueOf).toArray()
    }

    // returns divisor of n or -1 if failed: https://en.wikipedia.org/wiki/Pollard%27s_rho_algorithm#Algorithm
    // O(n^(1/4)) complexity
    fun pollard(n: Long): Long {
        val rnd = Random(1)
        var x: Long = Math.abs(rnd.nextLong()) % n
        var y = x
        while (true) {
            x = g(x, n)
            y = g(g(y, n), n)
            if (x == y) return -1
            val d = gcd(Math.abs(x - y), n)
            if (d != 1L) return d
        }
    }

    fun gcd(a: Long, b: Long): Long {
        return if (a == 0L) b else gcd(b % a, a)
    }

    fun g(x: Long, n: Long): Long {
        return (41 * x + 1) % n
    }

    // returns divisor of n: https://en.wikipedia.org/wiki/Fermat%27s_factorization_method
    fun ferma(n: Long): Long {
        var x = Math.sqrt(n) as Long
        var y: Long = 0
        var r = x * x - y * y - n
        while (true) {
            if (r == 0L) return if (x != y) x - y else x + y else if (r > 0) {
                r -= y + y + 1
                ++y
            } else {
                r += x + x + 1
                ++x
            }
        }
    }

    // Usage example
    fun main(args: Array<String?>?) {
        System.out.println(factorize(24))
        System.out.println(Arrays.toString(getAllDivisors(16)))
        val n = 1000003L * 100000037
        System.out.println(pollard(n))
        System.out.println(ferma(n))
    }
}
