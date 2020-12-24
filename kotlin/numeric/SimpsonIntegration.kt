package numeric

import java.util.function.DoubleFunction

object SimpsonIntegration {
    fun integrate(f: DoubleFunction<Double?>, a: Double, b: Double): Double {
        val eps = 1e-10
        val m = (a + b) / 2
        val am = simpsonIntegration(f, a, m)
        val mb = simpsonIntegration(f, m, b)
        val ab = simpsonIntegration(f, a, b)
        return if (Math.abs(am + mb - ab) < eps) ab else integrate(
            f,
            a,
            m
        ) + integrate(f, m, b)
    }

    fun simpsonIntegration(f: DoubleFunction<Double?>, a: Double, b: Double): Double {
        return (f.apply(a) + 4 * f.apply((a + b) / 2) + f.apply(b)) * (b - a) / 6
    }

    // Usage example
    fun main(args: Array<String?>?) {
        System.out.println(integrate(DoubleFunction<Double> { x -> Math.sin(x) }, 0.0, Math.PI / 2))
    }
}
