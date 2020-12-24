package structures

import numeric.FFT
import optimization.Simplex

// https://cp-algorithms.com/data_structures/stack_queue_modification.html
class QueueMin {
    var s1: List<Integer> = ArrayList()
    var s2: List<Integer> = ArrayList()
    var min1: Int = Integer.MAX_VALUE
    fun min(): Int {
        return Math.min(if (s2.isEmpty()) Integer.MAX_VALUE else s2[s2.size() - 1], min1)
    }

    fun addLast(x: Int) {
        s1.add(x)
        min1 = Math.min(min1, x)
    }

    fun removeFirst() {
        while (!s1.isEmpty()) {
            val x: Int = s1.remove(s1.size() - 1)
            s2.add(if (s2.isEmpty()) x else Math.min(x, s2[s2.size() - 1]))
        }
        min1 = Integer.MAX_VALUE
        s2.remove(s2.size() - 1)
    }

    companion object {
        // Usage example
        fun main(args: Array<String?>?) {
            val q = QueueMin()
            q.addLast(2)
            q.addLast(3)
            System.out.println(2 == q.min())
            q.removeFirst()
            System.out.println(3 == q.min())
            q.addLast(1)
            System.out.println(1 == q.min())
        }
    }
}
