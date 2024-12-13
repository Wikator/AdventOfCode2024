fun main() {
    data class ClawMachine(val aButton: Coords, val bButton: Coords, val goal: Coords)
    
    fun getClawMachines(input: List<String>): List<ClawMachine> {
        fun getCoords(text: String): Coords {
            val regex = Regex("\\d+")
            val numbers = regex.findAll(text)
            return Coords(numbers.first().value.toInt(), numbers.last().value.toInt())
        }
        
        val windowed = input.windowed(3, 4)
        return windowed.map { clawMachine ->
            val aButton = getCoords(clawMachine[0])
            val bButton = getCoords(clawMachine[1])
            val goal = getCoords(clawMachine[2])
            ClawMachine(aButton, bButton, goal)
        }
    }

    fun part1(input: List<String>): Int {
        val clawMachines = getClawMachines(input)
        return clawMachines.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day13_test")
    part1(input).println()
    part2(input).println()
}
