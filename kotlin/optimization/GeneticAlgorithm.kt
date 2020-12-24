package optimization

import java.util.List

class GeneticAlgorithm : JFrame() {
    var rnd: Random = Random(1)
    var n: Int = rnd.nextInt(300) + 250
    var generation = 0
    var x = DoubleArray(n)
    var y = DoubleArray(n)
    var bestState: IntArray
    fun geneticAlgorithm() {
        bestState = IntArray(n)
        for (i in 0 until n) bestState[i] = i
        val populationLimit = 100
        val population = Population(populationLimit)
        val n = x.size
        for (i in 0 until populationLimit) population.chromosomes.add(Chromosome(optimize(getRandomPermutation(n))))
        val mutationRate = 0.3
        val generations = 10000
        generation = 0
        while (generation < generations) {
            val i = 0
            while (population.chromosomes.size() < population.populationLimit) {
                val i1: Int = rnd.nextInt(population.chromosomes.size())
                val i2: Int = (i1 + 1 + rnd.nextInt(population.chromosomes.size() - 1)) % population.chromosomes.size()
                val parent1 = population.chromosomes[i1]
                val parent2 = population.chromosomes[i2]
                val pair = crossOver(parent1.p, parent2.p)
                if (rnd.nextDouble() < mutationRate) {
                    mutate(pair[0])
                    mutate(pair[1])
                }
                population.chromosomes.add(Chromosome(optimize(pair[0])))
                population.chromosomes.add(Chromosome(optimize(pair[1])))
            }
            population.nextGeneration()
            bestState = population.chromosomes[0].p
            repaint()
            generation++
        }
    }

    fun crossOver(p1: IntArray, p2: IntArray): Array<IntArray> {
        val n = p1.size
        val i1: Int = rnd.nextInt(n)
        val i2: Int = (i1 + 1 + rnd.nextInt(n - 1)) % n
        val n1: IntArray = p1.clone()
        val n2: IntArray = p2.clone()
        val used1 = BooleanArray(n)
        val used2 = BooleanArray(n)
        run {
            var i = i1
            while (true) {
                n1[i] = p2[i]
                used1[n1[i]] = true
                n2[i] = p1[i]
                used2[n2[i]] = true
                if (i == i2) {
                    break
                }
                i = (i + 1) % n
            }
        }
        run {
            var i = (i2 + 1) % n
            while (i != i1) {
                if (used1[n1[i]]) {
                    n1[i] = -1
                } else {
                    used1[n1[i]] = true
                }
                if (used2[n2[i]]) {
                    n2[i] = -1
                } else {
                    used2[n2[i]] = true
                }
                i = (i + 1) % n
            }
        }
        var pos1 = 0
        var pos2 = 0
        for (i in 0 until n) {
            if (n1[i] == -1) {
                while (used1[pos1]) ++pos1
                n1[i] = pos1++
            }
            if (n2[i] == -1) {
                while (used2[pos2]) ++pos2
                n2[i] = pos2++
            }
        }
        return arrayOf(n1, n2)
    }

    fun mutate(p: IntArray) {
        val n = p.size
        val i: Int = rnd.nextInt(n)
        val j: Int = (i + 1 + rnd.nextInt(n - 1)) % n
        reverse(p, i, j)
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

    // try all 2-opt moves
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

    internal inner class Chromosome(val p: IntArray) : Comparable<Chromosome?> {
        var cost = Double.NaN
            get() = if (Double.isNaN(field)) eval(p).also { field = it } else field
            private set

        @Override
        operator fun compareTo(o: Chromosome): Int {
            return Double.compare(cost, o.cost)
        }
    }

    internal class Population(val populationLimit: Int) {
        var chromosomes: List<Chromosome> = ArrayList()
        fun nextGeneration() {
            Collections.sort(chromosomes)
            chromosomes = ArrayList(chromosomes.subList(0, (chromosomes.size() + 1) / 2))
        }
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
            GeneticAlgorithm()
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
                g.drawString(String.format("generation: %d", generation), 150, h + 20)
            }
        })
        setSize(Dimension(600, 600))
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
        setVisible(true)
        Thread { geneticAlgorithm() }.start()
    }
}
