fun main() {
    
    fun getAvailableTowels(input: List<String>): List<String> = input.first().split(", ")
    fun getDesiredDesigns(input: List<String>): List<String> = input.drop(2)
    
    fun getPossibleTowelLengths(desiredDesign: String, availableTowels: List<String>): List<List<Int>> {
        val windowedDesign = desiredDesign.windowed(desiredDesign.length, 1, true)
        return windowedDesign
            .map { design -> availableTowels
                .filter { design.startsWith(it) }.map { it.length }
            }
    }

    fun part1(input: List<String>): Int {
        val availableTowels = getAvailableTowels(input)
        val desiredDesigns = getDesiredDesigns(input)

        fun canNumbersAdd(numbers: List<List<Int>>): Boolean {

            for (number in numbers.first()) {
                if (number == numbers.size) return true
                if (canNumbersAdd(numbers.subList(number, numbers.size))) return true
            }

            return false
        }
        
        return desiredDesigns.count { desiredDesign ->
            val possibleTowelsLengths = getPossibleTowelLengths(desiredDesign, availableTowels)
            canNumbersAdd(possibleTowelsLengths)
        }
    }

    fun part2(input: List<String>): Long {
        val availableTowels = getAvailableTowels(input)
        val desiredDesigns = getDesiredDesigns(input)
        
        val cache = mutableMapOf<List<List<Int>>, Long>()

        fun possibleArrangements(numbers: List<List<Int>>): Long =
            cache[numbers] ?: run { 
                val result = numbers.first().sumOf { number ->
                    if (number == numbers.size) {
                        1
                    } else {
                        possibleArrangements(numbers.subList(number, numbers.size))
                    }
                }
                
                cache[numbers] = result
                result
            }

        return desiredDesigns.sumOf { desiredDesign ->
            val possibleTowelsLengths = getPossibleTowelLengths(desiredDesign, availableTowels)
            possibleArrangements(possibleTowelsLengths)
        }
    }

    val input = readInput("Day19")
    part1(input).println()
    part2(input).println()
}
