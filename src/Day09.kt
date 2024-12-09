fun main() {

    fun part1(input: String): Long {
        
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
            val charactersToInsert = blocks.reversed().subList(0, emptySpaces).filterNotNull()
            var nextElementToTake = 0

            return blocks.take(blocks.size - emptySpaces).foldIndexed(0L) { index, acc, curr ->
                acc + (curr ?: charactersToInsert[nextElementToTake++]) * index
            }
        }
        
        val blocks = getBlocks(input)
        return calculateChecksum(blocks)
    }

    fun part2(input: String): Long {
        data class File(var id: Int?, var size: Int)

        fun getFiles(input: String): List<File> {
            var nextId = -1
            return input.mapIndexed { index, curr ->
                val nonEmptySpace = index % 2 == 0
                if (nonEmptySpace) nextId++
                File(if (nonEmptySpace) nextId else null, curr.digitToInt())
            }
        }

        fun calculateChecksum(files: List<File>): Long {
            val elements = files.toMutableList()
            
            for (file in files.reversed()) {
                if (file.id == null) continue
                
                for (i in elements.indices) {
                    val candidate = elements[i]
                    if (candidate === file) break
                    if (candidate.id != null || candidate.size < file.size) continue
                    elements.add(i, file.copy())
                    candidate.size -= file.size
                    file.id = null
                    break
                }
            }

            var position = 0L // Miscalculates when this is not a LONG. Only took an entire day to figure out...
            return elements.fold(0L) { acc, curr ->
                acc + (0 until curr.size).sumOf { _ -> position++ * (curr.id ?: 0) }
            }
        }
        
        val files = getFiles(input)
        return calculateChecksum(files)
    }

    val input = readInput("Day09").first()
    part1(input).println()
    part2(input).println()
}
