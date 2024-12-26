import kotlin.math.pow

fun main() {
    fun getStartingValues(input: List<String>): MutableMap<String, Boolean> {
        val relevantText = input.takeWhile { it != "" }
        
        return relevantText.associate { text ->
            val splitText = text.split(": ")
            splitText.first() to (splitText.last() == "1")
        }.toMutableMap()
    }
    
    fun part1(input: List<String>): Long {
        val values = getStartingValues(input)
        var next = true
        
        while (next) {
            next = false
            
            for (line in input.reversed().takeWhile { it != "" }.reversed()) {
                val splitLine = line.split(" ")
                
                if (splitLine[4] in values) continue
                
                val firstValue = values[splitLine[0]]
                val secondValue = values[splitLine[2]]
                
                if (firstValue == null || secondValue == null) {
                    next = true
                    continue
                }
                
                val newValue = when (splitLine[1]) {
                    "AND" -> firstValue && secondValue
                    "OR" -> firstValue || secondValue
                    "XOR" -> firstValue xor secondValue
                    else -> throw IllegalArgumentException("Invalid operation")
                }

                values[splitLine[4]] = newValue
            }
        }

        return values
            .filter { it.key.startsWith('z') }
            .toList()
            .sortedBy { it.first }
            .foldIndexed(0L) { index, acc, curr ->
                acc + if (curr.second) (2.0.pow(index)).toLong() else 0L
            }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day24")
    part1(input).println()
    part2(input).println()
}
