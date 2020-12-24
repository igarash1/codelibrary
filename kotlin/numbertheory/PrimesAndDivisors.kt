package numbertheory

import java.util.Arrays

object PrimesAndDivisors {
    // Generates prime numbers up to n in O(n*log(log(n))) time
    fun generatePrimes(n: Int): IntArray {
        val prime = BooleanArray(n + 1)
        Arrays.fill(prime, 2, n + 1, true)
        run {
            var i = 2
            while (i * i <= n) {
                if (prime[i]) {
                    var j = i * i
                    while (j <= n) {
                        prime[j] = false
                        j += i
                    }
                }
                i++
            }
        }
        val primes = IntArray(n + 1)
        var cnt = 0
        for (i in prime.indices) if (prime[i]) primes[cnt++] = i
        return Arrays.copyOf(primes, cnt)
    }

    // Generates prime numbers up to n in O(n) time
    fun generatePrimesLinearTime(n: Int): IntArray {
        val lp = IntArray(n + 1)
        val primes = IntArray(n + 1)
        var cnt = 0
        for (i in 2..n) {
            if (lp[i] == 0) {
                lp[i] = i
                primes[cnt++] = i
            }
            var j = 0
            while (j < cnt && primes[j] <= lp[i] && i * primes[j] <= n) {
                lp[i * primes[j]] = primes[j]
                ++j
            }
        }
        return Arrays.copyOf(primes, cnt)
    }

    fun isPrime(n: Long): Boolean {
        if (n <= 1) return false
        var i: Long = 2
        while (i * i <= n) {
            if (n % i == 0L) return false
            i++
        }
        return true
    }

    fun numberOfPrimeDivisors(n: Int): IntArray {
        val divisors = IntArray(n + 1)
        Arrays.fill(divisors, 2, n + 1, 1)
        var i = 2
        while (i * i <= n) {
            if (divisors[i] == 1) {
                var j = i
                while (j * i <= n) {
                    divisors[i * j] = divisors[j] + 1
                    j++
                }
            }
            ++i
        }
        return divisors
    }

    // Generates minimum prime divisor of all numbers up to n in O(n) time
    fun generateMinDivisors(n: Int): IntArray {
        val lp = IntArray(n + 1)
        lp[1] = 1
        val primes = IntArray(n + 1)
        var cnt = 0
        for (i in 2..n) {
            if (lp[i] == 0) {
                lp[i] = i
                primes[cnt++] = i
            }
            var j = 0
            while (j < cnt && primes[j] <= lp[i] && i * primes[j] <= n) {
                lp[i * primes[j]] = primes[j]
                ++j
            }
        }
        return lp
    }

    // Generates prime divisor of all numbers up to n
    fun generateDivisors(n: Int): IntArray {
        val divisors: IntArray = IntStream.range(0, n + 1).toArray()
        var i = 2
        while (i * i <= n) {
            if (divisors[i] == i) {
                var j = i * i
                while (j <= n) {
                    divisors[j] = i
                    j += i
                }
            }
            i++
        }
        return divisors
    }

    // Euler's totient function
    fun phi(n: Int): Int {
        var n = n
        var res = n
        var i = 2
        while (i * i <= n) {
            if (n % i == 0) {
                while (n % i == 0) n /= i
                res -= res / i
            }
            i++
        }
        if (n > 1) res -= res / n
        return res
    }

    // Euler's totient function
    fun generatePhi(n: Int): IntArray {
        val res: IntArray = IntStream.range(0, n + 1).toArray()
        for (i in 1..n) {
            var j = i + i
            while (j <= n) {
                res[j] -= res[i]
                j += i
            }
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        var n = 31
        val primes1 = generatePrimes(n)
        val primes2 = generatePrimesLinearTime(n)
        System.out.println(Arrays.toString(primes1))
        System.out.println(Arrays.toString(primes2))
        System.out.println(Arrays.equals(primes1, primes2))
        System.out.println(Arrays.toString(numberOfPrimeDivisors(n)))
        System.out.println(Arrays.toString(generateMinDivisors(n)))
        System.out.println(Arrays.toString(generateDivisors(n)))
        n = 1000
        val phi = generatePhi(n)
        val PHI: LongArray = MultiplicativeFunction.PHI.generateValues(n)
        for (i in 0..n) {
            if (phi[i] != phi(i) || phi[i] != PHI[i]) {
                System.err.println(i)
            }
        }
    }
}
