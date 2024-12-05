data class IndexedNumber(val value: Int, val index: Int)

fun main() {
    
    fun getOrderingRules(input: List<String>): Map<Int, Set<Int>> {
        val orderingRules = input.takeWhile { it.isNotEmpty() }

        return orderingRules
            .map { it.split("|") }
            .groupBy(keySelector = { it.last().toInt() }, valueTransform = { it.first().toInt() }) 
            .mapValues { (_, list) -> list.toSet() }
    }

    fun getUpdates(input: List<String>): List<List<IndexedNumber>> {
        val updates = input
            .reversed()
            .takeWhile { it.isNotEmpty() }

        return updates
            .map { update ->
                update
                    .split(',')
                    .mapIndexed { index, number -> IndexedNumber(number.toInt(), index) }
            }
    }
    
    fun getMiddleNumbersSum(numberLists: List<List<IndexedNumber>>): Int =
        numberLists
            .fold(0) { acc, numbers ->
                val middleIndex = numbers.size / 2
                acc + numbers.filter { it.index == middleIndex }
                    .map { it.value }
                    .first()
            }
    
    fun isUpdateCorrect(update: List<IndexedNumber>, orderingRules: Map<Int, Set<Int>>): Boolean =
        update.all { number ->
            val numbersBefore = update.subList(0, number.index).map { it.value }.toSet()
            val requiredNumbersBefore = orderingRules[number.value] ?: emptySet()

            requiredNumbersBefore
                .all { update.none { number -> number.value == it } || numbersBefore.contains(it) }
        }
    
    fun part1(input: List<String>): Int {
        val orderingRules = getOrderingRules(input)
        val updates = getUpdates(input)
        
        val relevantUpdates = updates
            .filter { isUpdateCorrect(it, orderingRules) }

        return getMiddleNumbersSum(relevantUpdates)
    }

    fun part2(input: List<String>): Int {
        val orderingRules = getOrderingRules(input)
        val updates = getUpdates(input)

        val wrongUpdates = updates
            .filter { !isUpdateCorrect(it, orderingRules) }
        
        val updateComparator = Comparator<Int> { num1, num2 ->
            val num1NumbersBefore = orderingRules[num1]
            val num2NumbersBefore = orderingRules[num2]
            
            when {
                (num2NumbersBefore ?: emptySet()).contains(num1) -> -1
                (num1NumbersBefore ?: emptySet()).contains(num2) -> 1
                else -> 0
            }
        }
        
        val correctedUpdates = wrongUpdates
            .map { update ->
                update
                    .map { it.value }
                    .sortedWith(updateComparator)
                    .mapIndexed { index, number -> IndexedNumber(number, index) }
            }
        
        return getMiddleNumbersSum(correctedUpdates)
    }

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
