package structures

class DoublyLinkedList(maxSize: Int) {
    var next: IntArray
    var prev: IntArray
    fun insert(x: Int, insertAfter: Int) {
        prev[x] = insertAfter
        next[x] = next[insertAfter]
        prev[next[x]] = x
        next[prev[x]] = x
    }

    fun remove(x: Int) {
        next[prev[x]] = next[x]
        prev[next[x]] = prev[x]
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val n = 10
            val list = DoublyLinkedList(n)
            for (i in 1..n) {
                list.insert(i, i - 1)
            }
            list.remove(1)
            list.remove(10)
            list.remove(5)
            var i = list.next[0]
            while (i != 0) {
                System.out.print("$i ")
                i = list.next[i]
            }
            System.out.println()
        }
    }

    init {
        // 0 - dummy element (head)
        next = IntArray(maxSize + 1)
        prev = IntArray(maxSize + 1)
    }
}
