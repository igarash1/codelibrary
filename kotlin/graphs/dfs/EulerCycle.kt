package graphs.dfs

import java.util.stream.Stream

object EulerCycle {
    fun eulerCycleUndirected(graph: Array<List<Integer>>, u: Int): List<Integer> {
        val usedEdges: Set<Long> = HashSet()
        val n = graph.size
        val curEdge = IntArray(n)
        val res: List<Integer> = ArrayList()
        dfs(graph, curEdge, usedEdges, res, u)
        Collections.reverse(res)
        return res
    }

    fun dfs(graph: Array<List<Integer>>, curEdge: IntArray, usedEdges: Set<Long?>, res: List<Integer?>, u: Int) {
        while (curEdge[u] < graph[u].size()) {
            val v: Int = graph[u][curEdge[u]++]
            if (usedEdges.add((Math.min(u, v) as Long shl 32) + Math.max(u, v))) dfs(graph, curEdge, usedEdges, res, v)
        }
        res.add(u)
    }

    fun eulerCycleUndirected2(graph: Array<List<Integer>>, u: Int): List<Integer> {
        var u = u
        val curEdge = IntArray(graph.size)
        val res: List<Integer> = ArrayList()
        val stack: Stack<Integer> = Stack()
        val usedEdges: Set<Long> = HashSet()
        stack.add(u)
        while (!stack.isEmpty()) {
            u = stack.pop()
            while (curEdge[u] < graph[u].size()) {
                val v: Int = graph[u][curEdge[u]++]
                if (usedEdges.add((Math.min(u, v) as Long shl 32) + Math.max(u, v))) {
                    stack.push(u)
                    u = v
                }
            }
            res.add(u)
        }
        Collections.reverse(res)
        return res
    }

    fun eulerCycleDirected(graph: Array<List<Integer>>, u: Int): List<Integer> {
        val n = graph.size
        val curEdge = IntArray(n)
        val res: List<Integer> = ArrayList()
        dfs(graph, curEdge, res, u)
        Collections.reverse(res)
        return res
    }

    fun dfs(graph: Array<List<Integer>>, curEdge: IntArray, res: List<Integer?>, u: Int) {
        while (curEdge[u] < graph[u].size()) {
            dfs(graph, curEdge, res, graph[u][curEdge[u]++])
        }
        res.add(u)
    }

    fun eulerCycleDirected2(graph: Array<List<Integer>>, v: Int): List<Integer> {
        var v = v
        val curEdge = IntArray(graph.size)
        val res: List<Integer> = ArrayList()
        val stack: Stack<Integer> = Stack()
        stack.add(v)
        while (!stack.isEmpty()) {
            v = stack.pop()
            while (curEdge[v] < graph[v].size()) {
                stack.push(v)
                v = graph[v][curEdge[v]++]
            }
            res.add(v)
        }
        Collections.reverse(res)
        return res
    }

    // Usage example
    fun main(args: Array<String?>?) {
        var n = 5
        var g: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        g[0].add(1)
        g[1].add(2)
        g[2].add(0)
        g[1].add(3)
        g[3].add(4)
        g[4].add(1)
        System.out.println(eulerCycleDirected(g, 0))
        System.out.println(eulerCycleDirected2(g, 0))
        n = 5
        g = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
        g[0].add(1)
        g[1].add(0)
        g[1].add(2)
        g[2].add(1)
        g[2].add(3)
        g[3].add(2)
        g[0].add(3)
        g[3].add(0)
        g[0].add(4)
        g[4].add(0)
        g[1].add(4)
        g[4].add(1)
        g[0].add(2)
        g[2].add(0)
        g[1].add(3)
        g[3].add(1)
        System.out.println(eulerCycleUndirected(g, 2))
        System.out.println(eulerCycleUndirected2(g, 2))
    }
}
