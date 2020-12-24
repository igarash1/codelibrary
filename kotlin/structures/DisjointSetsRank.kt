package structures

class DisjointSetsRank(size: Int) {
    var p: IntArray
    var rank: IntArray
    fun root(x: Int): Int {
        return if (x == p[x]) x else root(p[x]).also { p[x] = it }
    }

    fun unite(a: Int, b: Int) {
        var a = a
        var b = b
        a = root(a)
        b = root(b)
        if (a == b) return
        if (rank[a] < rank[b]) {
            p[a] = b
        } else {
            p[b] = a
            if (rank[a] == rank[b]) ++rank[a]
        }
    }

    companion object {
        fun main(args: Array<String?>?) {
            val ds = DisjointSetsRank(10)
            System.out.println(false == (ds.root(0) == ds.root(9)))
            ds.unite(0, 9)
            System.out.println(true == (ds.root(0) == ds.root(9)))
        }
    }

    init {
        p = IntArray(size)
        for (i in 0 until size) p[i] = i
        rank = IntArray(size)
    }
}
