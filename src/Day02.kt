import kotlin.math.abs

enum class SortOrder {
    Ascending, Descending
}

fun main() {
    
    fun convertToIntegers(text: List<String>): List<List<Int>> =
        text.map { line ->
            val splitText = line.split(' ')
            splitText.map { number -> number.toInt() }
        }
    
    fun isSafe(numbers: List<Int>, problemDampener: Boolean): Boolean {
        val firstNumber = numbers.first()
        val order = if (firstNumber < numbers[1]) SortOrder.Ascending else SortOrder.Descending
        
        fun isSafeHelper(numbers: List<Int>, lastNumber: Int, canIgnore: Boolean): Boolean {
            fun detectedUnsafe(): Boolean =
                when (canIgnore) {
                    true -> isSafeHelper(numbers.drop(1), lastNumber, false)
                    false -> false
                }
            
            if (numbers.isEmpty()) return true
            
            val currentNumber = numbers.first()
            val distance = currentNumber - lastNumber
            val absoluteDistance = abs(distance)

            if (absoluteDistance < 1 || absoluteDistance > 3) return detectedUnsafe()
            
            when (order) {
                SortOrder.Ascending -> { if (distance <= 0) return detectedUnsafe() }
                SortOrder.Descending -> { if (distance >= 0) return detectedUnsafe() }
            }
            
            return isSafeHelper(numbers.drop(1), currentNumber, canIgnore)
        }
        
        return isSafeHelper(numbers.drop(1), firstNumber, problemDampener)
    }

    fun part1(input: List<String>): Int {
        val numbers = convertToIntegers(input)
        return numbers.fold(0) { acc, curr -> acc + if (isSafe(curr, false)) 1 else 0 }
    }

    fun part2(input: List<String>): Int {
        val numbers = convertToIntegers(input)
        return numbers.fold(0) { acc, curr ->
            acc + if (isSafe(curr, true) || isSafe(curr.reversed(), true)) 1 else 0
        }
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
