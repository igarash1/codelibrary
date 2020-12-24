package optimization

import java.util.Random

class SimulatedAnnealing : JFrame() {
    var rnd: Random = Random(1)
    var n: Int = rnd.nextInt(300) + 250
    var bestState: IntArray
    var x = DoubleArray(n)
    var y = DoubleArray(n)
    fun anneal() {
        val curState = IntArray(n)
        for (i in 0 until n) curState[i] = i
        var curEnergy = eval(curState)
        bestState = curState.clone()
        var bestEnergy = curEnergy
        var temperature = 0.1
        val coolingFactor = 0.999999
        while (temperature > 1e-4) {
            val i: Int = rnd.nextInt(n)
            val j: Int = (i + 1 + rnd.nextInt(n - 2)) % n
            val i1 = (i - 1 + n) % n
            val j1 = (j + 1) % n
            val delta = ((dist(x[curState[i1]], y[curState[i1]], x[curState[j]], y[curState[j]])
                    + dist(x[curState[i]], y[curState[i]], x[curState[j1]], y[curState[j1]])) - dist(
                x[curState[i1]], y[curState[i1]], x[curState[i]], y[curState[i]]
            )
                    - dist(x[curState[j]], y[curState[j]], x[curState[j1]], y[curState[j1]]))
            if (delta < 0 || Math.exp(-delta / temperature) > rnd.nextDouble()) {
                reverse(curState, i, j)
                curEnergy += delta
                if (bestEnergy > curEnergy) {
                    bestEnergy = curEnergy
                    System.arraycopy(curState, 0, bestState, 0, n)
                    repaint()
                }
            }
            temperature *= coolingFactor
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

    companion object {
        // http://en.wikipedia.org/wiki/2-opt
        fun reverse(p: IntArray, i: Int, j: Int) {
            var i = i
            var j = j
            val n = p.size
            // reverse order from i to j
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
            SimulatedAnnealing()
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
                (g as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                (g as Graphics2D).setStroke(BasicStroke(3))
                g.setColor(Color.BLUE)
                val w: Int = getWidth() - 5
                val h: Int = getHeight() - 30
                run {
                    var i = 0
                    var j = n - 1
                    while (i < n) {
                        g.drawLine(
                            (x[bestState[i]] * w).toInt(), ((1 - y[bestState[i]]) * h).toInt(),
                            (x[bestState[j]] * w).toInt(), ((1 - y[bestState[j]]) * h).toInt()
                        )
                        j = i++
                    }
                }
                g.setColor(Color.RED)
                for (i in 0 until n) g.drawOval((x[i] * w).toInt() - 1, ((1 - y[i]) * h).toInt() - 1, 3, 3)
                g.setColor(Color.BLACK)
                g.drawString(String.format("length: %.3f", eval(bestState)), 5, h + 20)
            }
        })
        setSize(Dimension(600, 600))
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        setVisible(true)
        Thread { anneal() }.start()
    }
}
