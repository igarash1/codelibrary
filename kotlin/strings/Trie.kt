package strings

// https://en.wikipedia.org/wiki/Trie
object Trie {
    fun insertString(root: TrieNode?, s: String) {
        var cur = root
        for (ch in s.toCharArray()) {
            if (cur!!.children[ch.toInt()] == null) {
                cur.children[ch.toInt()] = TrieNode()
            }
            cur = cur.children[ch.toInt()]
        }
        cur!!.leaf = true
    }

    fun printSorted(node: TrieNode, s: String) {
        for (ch in node.children.indices) {
            val child = node.children[ch.toInt()]
            if (child != null) printSorted(child, s + ch)
        }
        if (node.leaf) {
            System.out.println(s)
        }
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val root = TrieNode()
        insertString(root, "hello")
        insertString(root, "world")
        insertString(root, "hi")
        printSorted(root, "")
    }

    class TrieNode {
        var children = arrayOfNulls<TrieNode>(128)
        var leaf = false
    }
}
