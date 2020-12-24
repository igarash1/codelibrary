package geometry

import java.util.Random

class RandomPolygon : JFrame() {
    companion object {
        fun getRandomPolygon(n: Int, maxWidth: Int, maxHeight: Int): Array<IntArray> {
            val rnd = Random(1)
            val x = IntArray(n)
            val y = IntArray(n)
            var p = IntArray(n)
            while (true) {
                for (i in 0 until n) {
                    x[i] = rnd.nextInt(maxWidth)
                    y[i] = rnd.nextInt(maxHeight)
                    p[i] = i
                }
                var improved = true
                while (improved) {
                    improved = false
                    for (i in 0 until n) {
                        for (j in 0 until n) {
                            val p1: IntArray = p.clone()
                            reverse(p1, i, j)
                            if (len(x, y, p) > len(x, y, p1)) {
                                p = p1
                                improved = true
                            }
                        }
                    }
                }
                val tx: IntArray = x.clone()
                val ty: IntArray = y.clone()
                for (i in 0 until n) {
                    x[i] = tx[p[i]]
                    y[i] = ty[p[i]]
                }
                var ok = true
                for (i in 0 until n) {
                    val x1 = (x[(i - 1 + n) % n] - x[i]).toLong()
                    val y1 = (y[(i - 1 + n) % n] - y[i]).toLong()
                    val x2 = (x[(i + 1) % n] - x[i]).toLong()
                    val y2 = (y[(i + 1) % n] - y[i]).toLong()
                    ok = ok and (x1 * y2 - x2 * y1 != 0L || x1 * x2 + y1 * y2 <= 0)
                }
                var i2 = 0
                var i1 = n - 1
                while (i2 < n) {
                    var j2 = 0
                    var j1 = n - 1
                    while (j2 < n) {
                        ok = ok and (i1 == j1 || i1 == j2 || i2 == j1 || !isCrossOrTouchIntersect(
                            x[i1].toLong(), y[i1].toLong(), x[i2].toLong(), y[i2].toLong(), x[j1]
                                .toLong(), y[j1].toLong(), x[j2].toLong(), y[j2].toLong()
                        ))
                        j1 = j2++
                    }
                    i1 = i2++
                }
                if (ok) return arrayOf(x, y)
            }
        }

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

        fun len(x: IntArray, y: IntArray, p: IntArray): Double {
            var res = 0.0
            var i = 0
            var j = p.size - 1
            while (i < p.size) {
                val dx = (x[p[i]] - x[p[j]]).toDouble()
                val dy = (y[p[i]] - y[p[j]]).toDouble()
                res += Math.sqrt(dx * dx + dy * dy)
                j = i++
            }
            return res
        }

        fun isCrossOrTouchIntersect(
            x1: Long,
            y1: Long,
            x2: Long,
            y2: Long,
            x3: Long,
            y3: Long,
            x4: Long,
            y4: Long
        ): Boolean {
            if (Math.max(x1, x2) < Math.min(x3, x4) || Math.max(x3, x4) < Math.min(x1, x2) || Math.max(
                    y1,
                    y2
                ) < Math.min(y3, y4) || Math.max(y3, y4) < Math.min(y1, y2)
            ) return false
            val z1 = (x2 - x1) * (y3 - y1) - (y2 - y1) * (x3 - x1)
            val z2 = (x2 - x1) * (y4 - y1) - (y2 - y1) * (x4 - x1)
            val z3 = (x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)
            val z4 = (x4 - x3) * (y2 - y3) - (y4 - y3) * (x2 - x3)
            return (z1 <= 0 || z2 <= 0) && (z1 >= 0 || z2 >= 0) && (z3 <= 0 || z4 <= 0) && (z3 >= 0 || z4 >= 0)
        }

        fun main(args: Array<String?>?) {
            RandomPolygon()
        }
    }

    // visualization code
    init {
        setContentPane(object : JPanel() {
            protected fun paintComponent(g: Graphics) {
                super.paintComponent(g)
                (g as Graphics2D).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
                (g as Graphics2D).setStroke(BasicStroke(3))
                val xy = getRandomPolygon(Random().nextInt(100) + 3, getWidth() - 20, getHeight() - 50)
                g.setColor(Color.BLUE)
                val n: Int = xy[0].length
                run {
                    var i = 0
                    var j = n - 1
                    while (i < n) {
                        g.drawLine(xy[0][i], xy[1][i], xy[0][j], xy[1][j])
                        j = i++
                    }
                }
                g.setColor(Color.RED)
                for (i in 0 until n) g.drawOval(xy[0][i] - 1, xy[1][i] - 1, 3, 3)
            }
        })
        setSize(Dimension(600, 600))
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        setVisible(true)
        object : Thread() {
            fun run() {
                while (true) {
                    try {
                        Thread.sleep(200)
                    } catch (e: InterruptedException) {
                    }
                    repaint()
                }
            }
        }.start()
    }
}
