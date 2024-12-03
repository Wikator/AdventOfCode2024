fun main() {
    
    fun addMultipliedNumbers(pairs: Sequence<Pair<Int, Int>>): Int =
        pairs.fold(0) { acc, (num1, num2) -> acc + num1 * num2 }
    
    fun getInstructions(text: String): Sequence<Pair<Int, Int>> {
        val instructionRegex = Regex("mul\\([0-9][0-9]?[0-9]?,[0-9][0-9]?[0-9]?\\)")
        val threeDigitNumberRegex = Regex("[0-9][0-9]?[0-9]?")
        
        val matches = instructionRegex.findAll(text)
        return matches.map { match ->
            val numbers = threeDigitNumberRegex.findAll(match.value)
            Pair(numbers.first().value.toInt(), numbers.last().value.toInt())
        }
    }
    
    fun part1(input: String): Int {
        val instructions = getInstructions(input)
        return addMultipliedNumbers(instructions)
    }

    fun part2(input: String): Int {
        fun getEnabledInstructions(program: String): Sequence<Pair<Int, Int>> {
            val splitTextByDo = program.split("do()")
            val enabledInstructions = splitTextByDo.map { text ->
                val splitText = text.split("don't()")
                splitText.first()
            }
            val combinedEnabledInstructions = enabledInstructions.joinToString()
            return getInstructions(combinedEnabledInstructions)
        }

        val instructions = getEnabledInstructions(input)
        return addMultipliedNumbers(instructions)
    }

    val input = readInput("Day03")
    val combinedInput = input.joinToString()
    part1(combinedInput).println()
    part2(combinedInput).println()
}
