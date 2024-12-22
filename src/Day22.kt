fun main() {
    
    fun nextSecretNumber(number: Long): Long {
        val modulo = 16777216L
        val step1 = (number).xor(number * 64) % modulo
        val step2 = step1.xor(step1 / 32) % modulo
        return (step2 * 2048).xor(step2) % modulo
    }

    fun part1(input: List<String>): Long {
        tailrec fun nextFinalSecretNumber(number: Long, count: Int): Long {
            if (count == 0) return number
            return nextFinalSecretNumber(nextSecretNumber(number), count - 1)
        }
        
        return input.sumOf { line ->
            nextFinalSecretNumber(line.toLong(), 2000)
        }
    }

    fun part2(input: List<String>): Int {
        data class Sequence(val first: Int, val second: Int, val third: Int, val fourth: Int)
        
        val onesDigits = input.map { line ->
            var currentNumber = line.toLong()
            val result = mutableListOf((currentNumber % 10).toInt())
            
            for (i in (0 until 1999)) {
                currentNumber = nextSecretNumber(currentNumber)
                result.add((currentNumber % 10).toInt())
            }
            
            result.toList()
        }
        
        val sequencesToBananas = onesDigits.map { digits ->
            val map = mutableMapOf<Sequence, Int>()
            val currentSequence = ArrayDeque<Int>()
            var lastDigit: Int? = null
            
            for (digit in digits) {
                if (lastDigit == null) {
                    lastDigit = digit
                    continue
                }
                
                val newDigit = digit - lastDigit
                if (currentSequence.size == 3) {
                    val first = currentSequence.removeFirst()
                    val sequence = Sequence(first, currentSequence[0], currentSequence[1], newDigit)
                    map.putIfAbsent(sequence, digit)
                }
                
                currentSequence.add(newDigit)
                lastDigit = digit
            }
            
            map.toMap()
        }
        
        val keys = sequencesToBananas.flatMap { it.keys }.toSet()
        return keys.maxOf {
            sequencesToBananas.sumOf { map ->
                map[it] ?: 0
            }
        }
    }

    val input = readInput("Day22")
    part1(input).println()
    part2(input).println()
}
