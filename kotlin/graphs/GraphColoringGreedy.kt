package graphs

import java.util.stream.Stream

object GraphColoringGreedy {
    // similar to DSatur coloring
    fun color(graph: Array<List<Integer>>): IntArray {
        val n = graph.size
        val used: Array<BitSet?> = arrayOfNulls<BitSet>(n)
        val colors = IntArray(n)
        val q: PriorityQueue<Long> = PriorityQueue(n)
        for (i in 0 until n) {
            used[i] = BitSet()
            colors[i] = -1
            q.add(i as Long)
        }
        for (i in 0 until n) {
            var bestu: Int
            while (true) {
                bestu = q.remove().intValue()
                if (colors[bestu] == -1) break
            }
            val c: Int = used[bestu].nextClearBit(0)
            colors[bestu] = c
            for (v in graph[bestu]) {
                if (!used[v].get(c)) {
                    used[v].set(c)
                    if (colors[v] == -1) q.add(v - (used[v].cardinality() as Long shl 32))
                }
            }
        }
        return colors
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val n = 5
        val g: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        for (i in 0 until n) {
            for (j in 0 until n) {
                g[i].add((i + 1) % n)
                g[(i + 1) % n].add(i)
            }
        }
        System.out.println(Arrays.toString(color(g)))
    }
}
