fun main() {

    tailrec fun getStonesCount(stones: List<Long>, blinksLeft: Int): Int {
        if (blinksLeft == 0) return stones.size

        val newStones = stones.flatMap { stone ->
            if (stone == 0L) {
                listOf(1L)
            } else {
                val stoneAsString = stone.toString()
                if (stoneAsString.length % 2 == 0) {
                    val newStone1 = (stoneAsString.substring(0, stoneAsString.length / 2)).toLong()
                    val newStone2 = (stoneAsString.substring(stoneAsString.length / 2)).toLong()
                    listOf(newStone1, newStone2)
                } else {
                    listOf(stone * 2024)
                }
            }
        }

        return getStonesCount(newStones, blinksLeft - 1)
    }

    fun part1(input: String): Int {
        val stones = input.split(' ').map { it.toLong() }.toMutableList()
        return getStonesCount(stones, 25)
    }

    fun part2(input: String): Int {
        val stones = input.split(' ').map { it.toLong() }.toMutableList()
        return getStonesCount(stones, 75)
    }

    val input = readInput("Day11_test").first()
    part1(input).println()
    part2(input).println()
}
