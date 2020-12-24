package graphs

import java.util.ArrayList

object GraphCycleDetection {
    fun findCycle(graph: Array<List<Integer>>): IntArray? {
        val n = graph.size
        val color = IntArray(n)
        val next = IntArray(n)
        for (u in 0 until n) {
            if (color[u] != 0) continue
            val cycleStart = dfs(graph, u, color, next)
            if (cycleStart != -1) {
                val cycle: List<Integer> = ArrayList()
                cycle.add(cycleStart)
                var i = next[cycleStart]
                while (i != cycleStart) {
                    cycle.add(i)
                    i = next[i]
                }
                cycle.add(cycleStart)
                return cycle.stream().mapToInt(Integer::intValue).toArray()
            }
        }
        return null
    }

    fun dfs(graph: Array<List<Integer>>, u: Int, color: IntArray, next: IntArray): Int {
        color[u] = 1
        for (v in graph[u]) {
            next[u] = v
            if (color[v] == 0) {
                val cycleStart = dfs(graph, v, color, next)
                if (cycleStart != -1) {
                    return cycleStart
                }
            } else if (color[v] == 1) {
                return v
            }
        }
        color[u] = 2
        return -1
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val graph: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(3).toArray { _Dummy_.__Array__() }
        graph[0].add(1)
        graph[1].add(2)
        graph[2].add(0)
        val cycle = findCycle(graph)
        System.out.println(Arrays.toString(cycle))
    }
}
