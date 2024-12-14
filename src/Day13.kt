fun main() {
    data class ClawMachine(val aButton: LongCoords, val bButton: LongCoords, val goal: LongCoords)
    data class LinearEquation(val a: Long, val b: Long, val c: Long)

    fun getClawMachines(input: List<String>, correction: Long): List<ClawMachine> {
        val regex = Regex("\\d+")
        
        fun getLongCoords(text: String, add: Long): LongCoords {
            val numbers = regex.findAll(text)
            return LongCoords(numbers.first().value.toLong() + add,
                numbers.last().value.toLong() + add)
        }

        val windowed = input.windowed(3, 4)
        return windowed.map { clawMachine ->
            val aButton = getLongCoords(clawMachine[0], 0L)
            val bButton = getLongCoords(clawMachine[1], 0L)
            val goal = getLongCoords(clawMachine[2], correction)
            ClawMachine(aButton, bButton, goal)
        }
    }
    
    fun getLowestAmountOfTokens(clawMachine: ClawMachine): Long {
        val equation1 = LinearEquation(clawMachine.aButton.x, clawMachine.bButton.x, clawMachine.goal.x)
        val equation2 = LinearEquation(clawMachine.aButton.y, clawMachine.bButton.y, clawMachine.goal.y)
        
        val rightSideA = equation2.c * equation1.b - equation1.c * equation2.b
        val leftSideA = equation2.a * equation1.b - equation2.b * equation1.a
        
        if (rightSideA % leftSideA != 0L) return 0L
        
        val a = rightSideA / leftSideA
        
        val rightSideB = equation1.c - equation1.a * a
        val leftSideB = equation1.b
        
        if (rightSideB % leftSideB != 0L) return 0L
        
        val b = rightSideB / leftSideB
        return a * 3 + b
    }
    
    fun part1(input: List<String>): Long {
        val clawMachines = getClawMachines(input, 0L)
        return clawMachines.sumOf { getLowestAmountOfTokens(it) }
    }

    fun part2(input: List<String>): Long {
        val clawMachines = getClawMachines(input, 10000000000000L)
        return clawMachines.sumOf { getLowestAmountOfTokens(it) }
    }

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}
