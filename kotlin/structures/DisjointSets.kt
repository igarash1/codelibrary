package structures

// https://en.wikipedia.org/wiki/Disjoint-set_data_structure with path compression heuristic
// Complexity of operations is O(log(n)) on average
object DisjointSets {
    fun createSets(size: Int): IntArray {
        val p = IntArray(size)
        for (i in 0 until size) p[i] = i
        return p
    }

    fun root(p: IntArray, x: Int): Int {
        return if (x == p[x]) x else root(p, p[x]).also { p[x] = it }
    }

    fun unite(p: IntArray, a: Int, b: Int): Boolean {
        var a = a
        var b = b
        a = root(p, a)
        b = root(p, b)
        p[a] = b
        return a != b
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val p = createSets(10)
        System.out.println(false == (root(p, 0) == root(p, 9)))
        unite(p, 0, 9)
        System.out.println(true == (root(p, 0) == root(p, 9)))
    }
}
