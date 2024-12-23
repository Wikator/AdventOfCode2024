fun main() {

    fun getStonesCount(startingStones: List<Long>, blinks: Int): Long {
        val cachedFinalStoneCounts = mutableMapOf<Long, MutableMap<Int, Long>>()

        fun getStonesCountHelper(stones: List<Long>, blinksLeft: Int): Long =
            if (blinksLeft == 0) {
                stones.size.toLong()
            } else {
                stones.sumOf { stone ->
                    val finalStoneCounts = cachedFinalStoneCounts.getOrPut(stone) { mutableMapOf() }
                    finalStoneCounts[blinksLeft] ?: run {
                        val finalStoneCount = when {
                            stone == 0L -> getStonesCountHelper(listOf(1L), blinksLeft - 1)
                            stone.toString().length % 2 == 0 -> {
                                val stoneStr = stone.toString()
                                val middleIndex = stoneStr.length / 2
                                val newStone1 = stoneStr.substring(0, middleIndex).toLong()
                                val newStone2 = stoneStr.substring(middleIndex).toLong()
                                getStonesCountHelper(listOf(newStone1, newStone2), blinksLeft - 1)
                            }
                            else -> getStonesCountHelper(listOf(stone * 2024), blinksLeft - 1)
                        }
                        finalStoneCounts[blinksLeft] = finalStoneCount
                        finalStoneCount
                    }
                }
            }

        return getStonesCountHelper(startingStones, blinks)
    }

    fun part1(input: String): Long {
        val stones = input.split(' ').map { it.toLong() }
        return getStonesCount(stones, 25)
    }

    fun part2(input: String): Long {
        val stones = input.split(' ').map { it.toLong() }
        return getStonesCount(stones, 75)
    }

    val input = readInput("Day11").first()
    part1(input).println()
    part2(input).println()
}
