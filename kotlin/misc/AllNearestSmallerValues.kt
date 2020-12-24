package misc

object AllNearestSmallerValues {
    // https://en.wikipedia.org/wiki/All_nearest_smaller_values
    fun nsv(a: IntArray): IntArray {
        val n = a.size
        val p = IntArray(n)
        for (i in 0 until n) {
            var j: Int = i - 1
            while (j != -1 && a[j] >= a[i]) {
                j = p[j]
            }
            p[i] = j
        }
        return p
    }

    fun maxInscribedRectangle(heights: IntArray): Long {
        val n = heights.size
        val rheights = IntArray(n)
        for (i in 0 until n) {
            rheights[i] = heights[n - 1 - i]
        }
        val lnsv = nsv(heights)
        val rnsv = nsv(rheights)
        var res: Long = 0
        for (i in 0 until n) {
            val a = lnsv[i] + 1
            val b = n - 1 - rnsv[n - 1 - i] - 1
            val cur = (b - a + 1).toLong() * heights[i]
            res = Math.max(res, cur)
        }
        return res
    }

    fun maxInscribedRectangle2(heights: IntArray): Long {
        var res: Long = 0
        val spos: Stack<Integer> = Stack()
        val sh: Stack<Integer> = Stack()
        sh.push(0)
        for (i in 0..heights.size) {
            val h = if (i < heights.size) heights[i] else 0
            var pos = i
            while (sh.peek() > h) {
                pos = spos.pop()
                res = Math.max(res, sh.pop() as Long * (i - pos))
            }
            spos.push(pos)
            sh.push(h)
        }
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        System.out.println(maxInscribedRectangle(intArrayOf(1, 2, 3)))
        System.out.println(maxInscribedRectangle2(intArrayOf(1, 2, 3)))
        System.out.println(Arrays.toString(nsv(intArrayOf(1, 1, 3, 2))))
        val rnd = Random(1)
        for (step in 0..999) {
            val n: Int = rnd.nextInt(10) + 1
            val h: IntArray = rnd.ints(n, 0, 10).toArray()
            val res1 = maxInscribedRectangle(h)
            val res2 = maxInscribedRectangle2(h)
            if (res1 != res2) throw RuntimeException()
        }
    }
}
