import kotlin.math.abs

fun main() {
    
    // list1 refers to numbers on the left, list2 refers to numbers on the right

    fun getPairOfLists(input: List<String>): Pair<List<Int>, List<Int>> =
        input.fold(Pair(emptyList(), emptyList())) { (list1Acc, list2Acc), curr ->
            val splitText = curr.split("   ")
            Pair(list1Acc + splitText.first().toInt(), list2Acc + splitText.last().toInt())
        }
    
    fun part1(input: List<String>): Int {
        val (list1, list2) = getPairOfLists(input)
        val twoListsZipped = list1.sorted() zip list2.sorted()
        return twoListsZipped.fold(0) { acc, (list1Curr, list2Curr) -> acc + abs(list1Curr - list2Curr) }
    }

    fun part2(input: List<String>): Int {
        fun countOccurrences(list: List<Int>, element: Int): Int =
            list.fold(0) { acc, curr -> acc + if (curr == element) 1 else 0 }
        
        val (list1, list2) = getPairOfLists(input)
        return list1.fold(0) { acc, curr -> acc + curr * countOccurrences(list2, curr) }
    }
    
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
