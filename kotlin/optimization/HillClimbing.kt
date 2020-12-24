package optimization

import java.util.function.BiFunction

object HillClimbing {
    fun findMinimum(f: BiFunction<Double?, Double?, Double?>): Double {
        var curX = 0.0
        var curY = 0.0
        var curF: Double = f.apply(curX, curY)
        var step = 1e6
        while (step > 1e-7) {
            var bestF = curF
            var bestX = curX
            var bestY = curY
            var find = false
            for (i in 0..5) {
                val a: Double = 2 * Math.PI * i / 6
                val nextX: Double = curX + step * Math.cos(a)
                val nextY: Double = curY + step * Math.sin(a)
                val nextF: Double = f.apply(nextX, nextY)
                if (bestF > nextF) {
                    bestF = nextF
                    bestX = nextX
                    bestY = nextY
                    find = true
                }
            }
            if (!find) {
                step /= 2.0
            } else {
                curX = bestX
                curY = bestY
                curF = bestF
            }
        }
        System.out.println("$curX $curY")
        return curF
    }

    // Usage example
    fun main(args: Array<String?>?) {
        System.out.println(findMinimum(BiFunction<Double, Double, Double> { x, y -> (x - 2) * (x - 2) + (y - 3) * (y - 3) }))
    }
}
