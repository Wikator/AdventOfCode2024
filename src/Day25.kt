fun main() {
    data class Schematic(val first: Int, val second: Int, val third: Int, val fourth: Int, val fifth: Int) {
        infix fun fits(schematic: Schematic): Boolean = this.first + schematic.first <= 5
                && this.second + schematic.second <= 5 && this.third + schematic.third <= 5
                && this.fourth + schematic.fourth <= 5 && this.fifth + schematic.fifth <= 5
    }
    
    fun getLocksAndKeys(input: List<String>): Pair<Set<Schematic>, Set<Schematic>> {
        val schematics = input.windowed(7, 8)
        
        return schematics.fold(Pair(emptySet(), emptySet())) { acc, curr ->
            val first = curr.count { it[0] == '#' } - 1
            val second = curr.count { it[1] == '#' } - 1
            val third = curr.count { it[2] == '#' } - 1
            val fourth = curr.count { it[3] == '#' } - 1
            val fifth = curr.count { it[4] == '#' } - 1
            val schematic = Schematic(first, second, third, fourth, fifth)
            if (curr.first() == "#".repeat(5)) {
                Pair(acc.first + schematic, acc.second)
            } else {
                Pair(acc.first, acc.second + schematic)
            }
        }
    }

    fun part1(input: List<String>): Int {
        val (locks, keys) = getLocksAndKeys(input)
        return locks.sumOf { lock -> keys.count { key -> key fits lock } }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day25")
    part1(input).println()
    part2(input).println()
}
