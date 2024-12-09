fun main() {
    
    fun getBlocks(input: String): List<Int?> {
        var nextId = -1
        return input.flatMapIndexed { index, curr ->
            val nonEmptySpace = index % 2 == 0
            if (nonEmptySpace) nextId++
            List(curr.digitToInt()) { if (nonEmptySpace) nextId else null }
        }
    }
    
    fun calculateChecksum(blocks: List<Int?>): Long {
        val emptySpaces = blocks.count { it == null }
        val charactersToInsert = blocks.reversed().subList(0, emptySpaces).filterNotNull().toMutableList()
        
        return blocks.take(blocks.size - emptySpaces).foldIndexed(0L) { index, acc, curr ->
            if (curr != null) {
                acc + curr * index
            } else {
                val newCharacter = charactersToInsert.removeFirst()
                acc + newCharacter * index
            }
        }
    }

    fun part1(input: String): Long {
        val blocks = getBlocks(input)
        return calculateChecksum(blocks)
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day09")
    part1(input.first()).println()
    part2(input).println()
}
