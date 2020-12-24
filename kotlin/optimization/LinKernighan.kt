package optimization

import java.util.Random

// https://en.wikipedia.org/wiki/Lin–Kernighan_heuristic
class LinKernighan : JFrame() {
    var rnd: Random = Random(1)
    var n: Int = rnd.nextInt(300) + 250
    var x = DoubleArray(n)
    var y = DoubleArray(n)
    var bestState: IntArray?
    var bestDist = Double.POSITIVE_INFINITY
    fun linKernighan() {
        //        int[] curState = optimize(getRandomPermutation(n));
        var curState: IntArray = IntStream.range(0, n).toArray()
        var curDist = eval(curState)
        updateBest(curState, curDist)
        var improved = true
        while (improved) {
            improved = false
            var rev = -1
            while (rev <= 1) {
                for (i in 0 until n) {
                    val p = IntArray(n)
                    for (j in 0 until n) p[j] = curState[(i + rev * j + n) % n]
                    val added = Array(n) { BooleanArray(n) }
                    val cost = eval(p)
                    var delta = -dist(x[p[n - 1]], y[p[n - 1]], x[p[0]], y[p[0]])
                    for (k in 0 until n) {
                        var best = Double.POSITIVE_INFINITY
                        var bestPos = -1
                        for (j in 1 until n - 2) {
                            if (added[p[j]][p[j + 1]]) continue
                            val addedEdge = dist(x[p[n - 1]], y[p[n - 1]], x[p[j]], y[p[j]])
                            if (delta + addedEdge > 0) continue
                            val removedEdge = dist(x[p[j]], y[p[j]], x[p[j + 1]], y[p[j + 1]])
                            val cur = addedEdge - removedEdge
                            if (best > cur) {
                                best = cur
                                bestPos = j
                            }
                        }
                        if (bestPos == -1) break
                        added[p[n - 1]][p[bestPos]] = true
                        added[p[bestPos]][p[n - 1]] = true
                        delta += best
                        reverse(p, bestPos + 1, n - 1)
                        val closingEdge = dist(x[p[n - 1]], y[p[n - 1]], x[p[0]], y[p[0]])
                        if (curDist > cost + delta + closingEdge) {
                            curDist = cost + delta + closingEdge
                            curState = p.clone()
                            updateBest(curState, curDist)
                            improved = true
                            break
                        }
                    }
                }
                rev += 2
            }
        }
        updateBest(curState, curDist)
    }

    fun updateBest(curState: IntArray, curDist: Double) {
        if (bestDist > curDist) {
            bestDist = curDist
            bestState = curState.clone()
            repaint()
        }
    }

    fun eval(state: IntArray): Double {
        var res = 0.0
        var i = 0
        var j = state.size - 1
        while (i < state.size) {
            res += dist(x[state[i]], y[state[i]], x[state[j]], y[state[j]])
            j = i++
        }
        return res
    }

    fun getRandomPermutation(n: Int): IntArray {
        val res = IntArray(n)
        for (i in 0 until n) {
            val j: Int = rnd.nextInt(i + 1)
            res[i] = res[j]
            res[j] = i
        }
        return res
    }

    fun optimize(p: IntArray): IntArray {
        val res: IntArray = p.clone()
        var improved = true
        while (improved) {
            improved = false
            for (i in 0 until n) {
                for (j in 0 until n) {
                    if (i == j || (j + 1) % n == i) continue
                    val i1: Int = (i - 1 + n) % n
                    val j1: Int = (j + 1) % n
                    val delta = ((dist(x[res[i1]], y[res[i1]], x[res[j]], y[res[j]])
                            + dist(x[res[i]], y[res[i]], x[res[j1]], y[res[j1]])) - dist(
                        x[res[i1]], y[res[i1]], x[res[i]], y[res[i]]
                    )
                            - dist(x[res[j]], y[res[j]], x[res[j1]], y[res[j1]]))
                    if (delta < -1e-9) {
                        reverse(res, i, j)
                        improved = true
                    }
                }
            }
        }
        return res
    }

    companion object {
        // reverse order from i to j
        fun reverse(p: IntArray, i: Int, j: Int) {
            var i = i
            var j = j
            val n = p.size
            while (i != j) {
                val t = p[j]
                p[j] = p[i]
                p[i] = t
                i = (i + 1) % n
                if (i == j) break
                j = (j - 1 + n) % n
            }
        }

        fun dist(x1: Double, y1: Double, x2: Double, y2: Double): Double {
            val dx = x1 - x2
            val dy = y1 - y2
            return Math.sqrt(dx * dx + dy * dy)
        }

        fun main(args: Array<String?>?) {
            LinKernighan()
        }
    }

    init {
        for (i in 0 until n) {
            x[i] = rnd.nextDouble()
            y[i] = rnd.nextDouble()
        }
    }

    // visualization code
    init {
        setContentPane(object : JPanel() {
            protected fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                if (bestState == null) return
                (g as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                (g as Graphics2D).setStroke(BasicStroke(3))
                val w: Int = getWidth() - 5
                val h: Int = getHeight() - 30
                var i = 0
                var j = n - 1
                while (i < n) {
                    g.drawLine(
                        (x[bestState!![i]] * w).toInt(), ((1 - y[bestState!![i]]) * h).toInt(),
                        (x[bestState!![j]] * w).toInt(), ((1 - y[bestState!![j]]) * h).toInt()
                    )
                    j = i++
                }
                g.drawString(String.format("length: %.3f", eval(bestState!!)), 5, h + 20)
            }
        })
        setSize(Dimension(600, 600))
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        setVisible(true)
        Thread { linKernighan() }.start()
    }
}
