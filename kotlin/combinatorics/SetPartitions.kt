package combinatorics

import java.util.stream.Stream

object SetPartitions {
    fun nextRestrictedGrowthString(a: IntArray): Boolean {
        val n = a.size
        val b = IntArray(n)
        run {
            var i = 0
            while (i + 1 < n) {
                b[i + 1] = Math.max(a[i] + 1, b[i])
                i++
            }
        }
        var i = n - 1
        while (a[i] == b[i]) {
            if (--i < 0) return false
        }
        ++a[i]
        Arrays.fill(a, i + 1, n, 0)
        return true
    }

    fun toSets(a: IntArray): Array<IntArray> {
        val sets: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(a.size).toArray { _Dummy_.__Array__() }
        for (i in a.indices) {
            sets[a[i]].add(i)
        }
        return Arrays.stream(sets)
            .filter { s -> !s.isEmpty() }
            .map { s -> s.stream().mapToInt(Integer::intValue).toArray() }
            .toArray { _Dummy_.__Array__() }
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val a = IntArray(3)
        do {
            System.out.println(Arrays.deepToString(toSets(a)))
        } while (nextRestrictedGrowthString(a))
    }
}
