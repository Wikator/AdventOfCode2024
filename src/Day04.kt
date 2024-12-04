fun main() {
    
    fun getVerticalLines(input: List<String>): List<String> {
        tailrec fun getVerticalLinesHelper(lines: List<String>, accumulator: List<String>): List<String> {
            if (lines.isEmpty()) return accumulator
            val currentLine = lines.first()
            val newAccumulator = accumulator.mapIndexed { index, curr -> curr + currentLine[index] }
            return getVerticalLinesHelper(lines.drop(1), newAccumulator)           
        }
        
        return getVerticalLinesHelper(input.drop(1), input.first().toList().map { c -> c.toString() })
    }
    
    fun getDiagonals(horizontalLines: List<String>, verticalLines: List<String>): List<String> {
        fun getTopRightToBottomLeftDiagonals(lines: List<String>): List<String> =
            lines.first().mapIndexed { index, character ->
                val textToAppend = (1..index).map { i ->
                    lines[i][index - i]
                }

                character + textToAppend.joinToString("")
            }
        
        val diagonals1 = getTopRightToBottomLeftDiagonals(horizontalLines)
        val diagonals2 = getTopRightToBottomLeftDiagonals(verticalLines.map { line ->
            line.drop(1).reversed() }.reversed())
        val diagonals3 = getTopRightToBottomLeftDiagonals(horizontalLines.reversed())
        val diagonals4 = getTopRightToBottomLeftDiagonals(verticalLines.map { line ->
            line.substring(0, line.length - 1) }.reversed())
        
        return diagonals1 + diagonals2 + diagonals3 + diagonals4
    }

    fun part1(horizontalLines: List<String>): Int {
        val regex = Regex("(?=(XMAS|SAMX))")
        val verticalLines = getVerticalLines(horizontalLines)
        val diagonals = getDiagonals(horizontalLines, verticalLines)
        
        val horizontalCount = horizontalLines.sumOf { line -> regex.findAll(line).count() }
        val verticalCount = verticalLines.sumOf { line -> regex.findAll(line).count() }
        val diagonalCount = diagonals.sumOf { line -> regex.findAll(line).count() }
        
        return horizontalCount + verticalCount + diagonalCount
    }

    fun part2(input: List<String>): Int {
        val width = input.first().length
        val height = input.size
        
        return (1 until height - 1).sumOf { i ->
            (1 until width - 1).count { j ->
                if (input[i][j] != 'A') {
                    false
                } else {
                    val mas1 = (input[i - 1][j - 1] == 'S' && input[i + 1][j + 1] == 'M')
                            || (input[i - 1][j - 1] == 'M' && input[i + 1][j + 1] == 'S')

                    val mas2 = (input[i - 1][j + 1] == 'S' && input[i + 1][j - 1] == 'M')
                            || (input[i - 1][j + 1] == 'M' && input[i + 1][j - 1] == 'S')

                    mas2 && mas1
                }
            }
        }
    }

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
