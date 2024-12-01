import kotlin.math.abs

fun main() {

    fun getPairOfLists(input: List<String>): Pair<List<Int>, List<Int>> =
        input.fold(Pair(emptyList(), emptyList())) { acc, curr ->
            val splitText = curr.split("   ")
            Pair(acc.first + splitText.first().toInt(), acc.second + splitText.last().toInt())
        }
    
    fun part1(input: List<String>): Int {
        val twoLists = getPairOfLists(input)
        val twoListsZipped = twoLists.first.sorted().zip(twoLists.second.sorted())
        return twoListsZipped.fold(0) { acc, curr -> acc + abs(curr.first - curr.second) }
    }

    fun part2(input: List<String>): Int {
        fun countOccurrences(list: List<Int>, element: Int): Int =
            list.fold(0) { acc, curr -> acc + if (curr == element) 1 else 0}
        
        val twoLists = getPairOfLists(input)
        return twoLists.first.fold(0) { acc, curr -> acc + curr * countOccurrences(twoLists.second, curr) }
    }
    
    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
