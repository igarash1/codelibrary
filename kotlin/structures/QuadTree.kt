package structures

// https://en.wikipedia.org/wiki/Quadtree
class QuadTree {
    class Node {
        var count = 0
        var topLeft: Node? = null
        var topRight: Node? = null
        var bottomLeft: Node? = null
        var bottomRight: Node? = null
    }

    var root: Node? = null

    // insert point (x,y)
    fun insert(x: Int, y: Int) {
        root = insert(root, 0, 0, maxx - 1, maxy - 1, x, y)
    }

    fun insert(node: Node?, ax: Int, ay: Int, bx: Int, by: Int, x: Int, y: Int): Node? {
        var node = node
        if (ax > x || x > bx || ay > y || y > by) return node
        if (node == null) node = Node()
        ++node.count
        if (ax == bx && ay == by) return node
        val mx = ax + bx shr 1
        val my = ay + by shr 1
        node.bottomLeft = insert(node.bottomLeft, ax, ay, mx, my, x, y)
        node.topLeft = insert(node.topLeft, ax, my + 1, mx, by, x, y)
        node.bottomRight = insert(node.bottomRight, mx + 1, ay, bx, my, x, y)
        node.topRight = insert(node.topRight, mx + 1, my + 1, bx, by, x, y)
        return node
    }

    // number of points in [x1,x2] x [y1,y2]
    fun count(x1: Int, y1: Int, x2: Int, y2: Int): Int {
        return count(root, 0, 0, maxx - 1, maxy - 1, x1, y1, x2, y2)
    }

    fun count(node: Node?, ax: Int, ay: Int, bx: Int, by: Int, x1: Int, y1: Int, x2: Int, y2: Int): Int {
        if (node == null || ax > x2 || x1 > bx || ay > y2 || y1 > by) return 0
        if (x1 <= ax && bx <= x2 && y1 <= ay && by <= y2) return node.count
        val mx = ax + bx shr 1
        val my = ay + by shr 1
        var res = 0
        res += count(node.bottomLeft, ax, ay, mx, my, x1, y1, x2, y2)
        res += count(node.topLeft, ax, my + 1, mx, by, x1, y1, x2, y2)
        res += count(node.bottomRight, mx + 1, ay, bx, my, x1, y1, x2, y2)
        res += count(node.topRight, mx + 1, my + 1, bx, by, x1, y1, x2, y2)
        return res
    }

    companion object {
        const val maxx = 1 shl 30
        const val maxy = 1 shl 30

        // Usage example
        fun main(args: Array<String?>?) {
            val t = QuadTree()
            t.insert(0, 0)
            t.insert(1, 0)
            t.insert(2, 0)
            t.insert(3, 0)
            System.out.println(4 == t.count(0, 0, 3, 0))
        }
    }
}
