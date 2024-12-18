fun main() {
    fun getMaze(input: List<String>, dimensions: Int, firstBytes: Int): List<List<Boolean>> {
        val maze = List(dimensions) { MutableList(dimensions) { false } }
        var byte = 0
        
        for (line in input) {
            byte++
            if (byte > firstBytes) continue
            
            val splitLine = line.split(',')
            val x = splitLine.last().toInt()
            val y = splitLine.first().toInt()
            maze[x][y] = true
        }
        
        return maze.map { it.toList() }
    }
    
    fun part1(input: List<String>): Int {
        val maze = getMaze(input, 7, 12)
        
        for (line in maze) {
            for (char in line) {
                print(if (char) '#' else '.')
            }
            println()
        }
        
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day18_test")
    part1(input).println()
    part2(input).println()
}
