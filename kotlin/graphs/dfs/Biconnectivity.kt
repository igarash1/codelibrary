package graphs.dfs

import java.util.stream.Stream

class Biconnectivity {
    class Edge(var u: Int, var v: Int) {
        override fun toString(): String {
            return "($u,$v)"
        }
    }

    var graph: Array<List<Integer>>
    var visited: BooleanArray
    var stack: Stack<Integer>? = null
    var stackEdges: Stack<Edge>? = null
    var time = 0
    var tin: IntArray
    var up: IntArray
    var edgeBiconnectedComponents: List<List<Integer>>? = null
    var vertexBiconnectedComponents: List<List<Edge>>? = null
    var cutPoints: List<Integer>? = null
    var bridges: List<Edge>? = null
    fun biconnectivity(graph: Array<List<Integer>>) {
        val n = graph.size
        this.graph = graph
        visited = BooleanArray(n)
        stack = Stack()
        stackEdges = Stack()
        time = 0
        tin = IntArray(n)
        up = IntArray(n)
        edgeBiconnectedComponents = ArrayList()
        vertexBiconnectedComponents = ArrayList()
        cutPoints = ArrayList()
        bridges = ArrayList()
        for (u in 0 until n) if (!visited[u]) dfs(u, -1)
    }

    fun dfs(u: Int, p: Int) {
        visited[u] = true
        tin[u] = time++
        up[u] = tin[u]
        stack.add(u)
        var children = 0
        var cutPoint = false
        for (v in graph[u]) {
            if (v == p) continue
            if (visited[v]) {
                if (tin[u] > tin[v]) {
                    stackEdges.add(Edge(u, v))
                }
                up[u] = Math.min(up[u], tin[v])
            } else {
                stackEdges.add(Edge(u, v))
                dfs(v, u)
                up[u] = Math.min(up[u], up[v])
                if (tin[u] <= up[v]) {
                    cutPoint = true
                    val component: List<Edge> = ArrayList()
                    while (true) {
                        val e: Edge = stackEdges.pop()
                        component.add(e)
                        if (e.u == u && e.v == v) break
                    }
                    vertexBiconnectedComponents.add(component)
                }
                if (tin[u] < up[v]) // or (up[v] == tin[v])
                    bridges.add(Edge(u, v))
                ++children
            }
        }
        if (p == -1) cutPoint = children >= 2
        if (cutPoint) cutPoints.add(u)
        if (tin[u] == up[u]) {
            val component: List<Integer> = ArrayList()
            while (true) {
                val x: Int = stack.pop()
                component.add(x)
                if (x == u) break
            }
            edgeBiconnectedComponents.add(component)
        }
    }

    companion object {
        // tree of edge-biconnected components
        fun ebcTree(graph: Array<List<Integer>>, components: List<List<Integer>>?): Array<List<Integer>> {
            val comp = IntArray(graph.size)
            for (i in 0 until components!!.size()) for (u in components!![i]) comp[u] = i
            val g: Array<List<Integer>> =
                Stream.generate { ArrayList() }.limit(components!!.size()).toArray { _Dummy_.__Array__() }
            for (u in graph.indices) for (v in graph[u]) if (comp[u] != comp[v]) g[comp[u]].add(comp[v])
            return g
        }

        // Usage example
        fun main(args: Array<String?>?) {
            val edges = arrayOf(
                intArrayOf(0, 1),
                intArrayOf(1, 2),
                intArrayOf(0, 2),
                intArrayOf(2, 3),
                intArrayOf(1, 4),
                intArrayOf(4, 5),
                intArrayOf(5, 1)
            )
            // int[][] edges = {{0, 1}};
            val n: Int = Arrays.stream(edges).mapToInt { e -> Math.max(e.get(0), e.get(1)) }.max().getAsInt() + 1
            val graph: Array<List<Integer>> = Stream.generate { ArrayList() }.limit(n).toArray { _Dummy_.__Array__() }
            for (edge in edges) {
                graph[edge[0]].add(edge[1])
                graph[edge[1]].add(edge[0])
            }
            val bc = Biconnectivity()
            bc.biconnectivity(graph)
            System.out.println("edge-biconnected components:" + bc.edgeBiconnectedComponents)
            System.out.println("vertex-biconnected components:" + bc.vertexBiconnectedComponents)
            System.out.println("cut points: " + bc.cutPoints)
            System.out.println("bridges:" + bc.bridges)
            System.out.println(
                "edge-biconnected condensation tree:" + Arrays.toString(ebcTree(graph, bc.edgeBiconnectedComponents))
            )
        }
    }
}
