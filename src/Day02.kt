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
    
    fun getOrder(numbers: List<Int>): SortOrder =
        if (numbers[0] < numbers[1]) SortOrder.Ascending else SortOrder.Descending 
    
    fun isSafe(numbers: List<Int>, problemDampener: Boolean): Boolean {
        
        tailrec fun isSafeHelper(numbers: List<Int>, lastNumber: Int, order: SortOrder, canIgnore: Boolean): Boolean {
            if (numbers.isEmpty()) return true
            
            val currentNumber = numbers.first()
            val distance = currentNumber - lastNumber
            val absoluteDistance = abs(distance)

            if (absoluteDistance < 1 || absoluteDistance > 3) {
                if (canIgnore) return isSafeHelper(numbers.drop(1), lastNumber, order, false)
                return false
            }
            
            when (order) {
                SortOrder.Ascending -> if (distance <= 0) {
                    if (canIgnore) return isSafeHelper(numbers.drop(1), lastNumber, order, false)
                    return false
                }
                SortOrder.Descending -> if (distance >= 0) {
                    if (canIgnore) return isSafeHelper(numbers.drop(1), lastNumber, order, false)
                    return false
                }
            }
            
            return isSafeHelper(numbers.drop(1), currentNumber, order, canIgnore)
        }
        
        if (!problemDampener)
            return isSafeHelper(numbers.drop(1), numbers.first(), getOrder(numbers), false)
        
        val reversedNumbers = numbers.reversed()
        return isSafeHelper(numbers.drop(1), numbers.first(), getOrder(numbers), true)
                || isSafeHelper(reversedNumbers.drop(1), reversedNumbers.first(), getOrder(reversedNumbers), true)
    }

    fun part1(input: List<String>): Int {
        val numbers = convertToIntegers(input)
        return numbers.count { number -> isSafe(number, false) }
    }

    fun part2(input: List<String>): Int {
        val numbers = convertToIntegers(input)
        return numbers.count { number -> isSafe(number, true) }
    }

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
