data class Line(val goal: Long, val numbers: List<Int>)

fun main() {
    
    fun getLines(input: List<String>): List<Line> =
        input.map { line ->
            val splitLine = line.split(": ")
            val numbers = splitLine.last().split(' ').map { it.toInt() }
            Line(splitLine.first().toLong(), numbers)
        }
    
    fun concatenate(accumulator: Long, newNumber: Int): Long {
        val result = accumulator.toString() + newNumber.toString()
        return result.toLong()
    }

    fun bruteForce(goal: Long, remainingNumbers: List<Int>, concatOperator: Boolean, acc: Long = 0): Boolean {

        if (remainingNumbers.isEmpty()) {
            return goal == acc
        }

        val nextNumber = remainingNumbers.first()
        val newList = remainingNumbers.subList(1, remainingNumbers.size)
        
        val result = bruteForce(goal, newList, concatOperator, acc + nextNumber) ||
                bruteForce(goal, newList, concatOperator, acc * nextNumber)
        
        return when (concatOperator) {
            false -> result
            true -> result || bruteForce(goal, newList, true, concatenate(acc, nextNumber))
        }
    }
    
    fun part1(input: List<String>): Long {
        val lines = getLines(input)
        
        return lines.sumOf { line ->
            if (bruteForce(line.goal, line.numbers, false))
                line.goal
            else
                0
        }
    }

    fun part2(input: List<String>): Long {
        val lines = getLines(input)

        return lines.sumOf { line ->
            if (bruteForce(line.goal, line.numbers, true))
                line.goal
            else
                0
        }
    }

    val input = readInput("Day07")

    part1(input).println()
    part2(input).println()
}
