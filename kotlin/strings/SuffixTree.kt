package strings

import numeric.FFT
import optimization.Simplex

// https://stackoverflow.com/questions/9452701/ukkonens-suffix-tree-algorithm-in-plain-english/9513423#9513423
object SuffixTree {
    const val ALPHABET = "abcdefghijklmnopqrstuvwxyz0123456789\u0001\u0002"
    fun buildSuffixTree(s: CharSequence): Node {
        val n: Int = s.length()
        val a = ByteArray(n)
        for (i in 0 until n) a[i] = ALPHABET.indexOf(s.charAt(i)) as Byte
        val root = Node(0, 0, 0, null)
        var node: Node? = root
        var i = 0
        var tail = 0
        while (i < n) {
            var last: Node? = null
            while (tail >= 0) {
                var ch = node!!.children[a[i - tail].toInt()]
                while (ch != null && tail >= ch.end - ch.begin) {
                    tail -= ch.end - ch.begin
                    node = ch
                    ch = ch.children[a[i - tail].toInt()]
                }
                if (ch == null) {
                    node!!.children[a[i].toInt()] = Node(i, n, node.depth + node.end - node.begin, node)
                    if (last != null) last.suffixLink = node
                    last = null
                } else {
                    val afterTail = a[ch.begin + tail]
                    if (afterTail == a[i]) {
                        if (last != null) last.suffixLink = node
                        break
                    } else {
                        val splitNode = Node(ch.begin, ch.begin + tail, node!!.depth + node.end - node.begin, node)
                        splitNode.children[a[i].toInt()] = Node(i, n, ch.depth + tail, splitNode)
                        splitNode.children[afterTail.toInt()] = ch
                        ch.begin += tail
                        ch.depth += tail
                        ch.parent = splitNode
                        node.children[a[i - tail].toInt()] = splitNode
                        if (last != null) last.suffixLink = splitNode
                        last = splitNode
                    }
                }
                if (node === root) {
                    --tail
                } else {
                    node = node.suffixLink
                }
            }
            i++
            tail++
        }
        return root
    }

    // Usage example
    fun main(args: Array<String?>?) {
        val s1 = "aabcc"
        val s2 = "abc"
        // build generalized suffix tree
        val s = s1 + '\u0001' + s2 + '\u0002'.toInt()
        val tree = buildSuffixTree(s)
        val lcs = lcs(tree, s1.length(), s1.length() + s2.length() + 1)
        System.out.println(3 == lcs)
    }

    var lcsLength = 0
    var lcsBeginIndex = 0

    // traverse suffix tree to find longest common substring
    fun lcs(node: Node, i1: Int, i2: Int): Int {
        if (node.begin <= i1 && i1 < node.end) {
            return 1
        }
        if (node.begin <= i2 && i2 < node.end) {
            return 2
        }
        var mask = 0
        for (f in 0 until ALPHABET.length()) {
            if (node.children[f.toInt()] != null) {
                mask = mask or lcs(node.children[f.toInt()], i1, i2)
            }
        }
        if (mask == 3) {
            val curLength = node.depth + node.end - node.begin
            if (lcsLength < curLength) {
                lcsLength = curLength
                lcsBeginIndex = node.begin
            }
        }
        return mask
    }

    class Node internal constructor(
        var begin: Int, var end: Int, // distance in characters from root to this node
        var depth: Int, var parent: Node?
    ) {
        var children: Array<Node>
        var suffixLink: Node? = null

        init {
            children = arrayOfNulls(ALPHABET.length())
        }
    }
}
