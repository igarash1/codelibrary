package structures

class BinarySearchTree {
    var root: Node? = null

    class Node(var key: Int, var value: Int, var p: Node?) {
        var l: Node? = null
        var r: Node? = null
    }

    fun search(t: Node?, key: Int): Node? {
        if (t == null || t.key == key) return t
        return if (key < t.key) search(t.l, key) else search(t.r, key)
    }

    fun search(key: Int): Node? {
        return search(root, key)
    }

    fun insert(t: Node?, p: Node?, key: Int, value: Int): Node {
        var t = t
        if (t == null) {
            t = Node(key, value, p)
        } else {
            if (key < t.key) t.l = insert(t.l, t, key, value) else t.r = insert(t.r, t, key, value)
        }
        return t
    }

    fun insert(key: Int, value: Int) {
        root = insert(root, null, key, value)
    }

    fun replace(a: Node?, b: Node?) {
        if (a!!.p == null) root = b else if (a === a.p!!.l) a.p!!.l = b else a.p!!.r = b
        if (b != null) b.p = a.p
    }

    fun remove(t: Node?, key: Int) {
        if (t == null) return
        if (key < t.key) remove(t.l, key) else if (key > t.key) remove(t.r, key) else if (t.l != null && t.r != null) {
            var m = t.r
            while (m!!.l != null) m = m.l
            t.key = m.key
            t.value = m.value
            replace(m, m.r)
        } else if (t.l != null) {
            replace(t, t.l)
        } else if (t.r != null) {
            replace(t, t.r)
        } else {
            replace(t, null)
        }
    }

    fun remove(key: Int) {
        remove(root, key)
    }

    fun print(t: Node?) {
        if (t != null) {
            print(t.l)
            System.out.print(t.key.toString() + ":" + t.value + " ")
            print(t.r)
        }
    }

    fun print() {
        print(root)
        System.out.println()
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val tree = BinarySearchTree()
            tree.insert(3, 1)
            tree.insert(2, 2)
            tree.insert(4, 5)
            tree.print()
            tree.remove(2)
            tree.remove(3)
            tree.print()
            tree.remove(4)
        }
    }
}
