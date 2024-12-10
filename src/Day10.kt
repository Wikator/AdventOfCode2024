fun main() {
    
    fun getNextCoords(coords: Coords): Set<Coords> =
        setOf(Coords(coords.x - 1, coords.y), Coords(coords.x + 1, coords.y),
            Coords(coords.x, coords.y - 1), Coords(coords.x, coords.y + 1))
    
    fun sumOfTwoDimensional(input: List<String>, operation: (Coords) -> Int): Int =
        input.indices.sumOf { i ->
            input[i].indices.sumOf { j ->
                if (input[i][j] == '0' ) operation(Coords(i, j)) else 0
            }
        }
    
    fun part1(input: List<String>): Int {
        
        fun findTrailheads(coords: Coords): Int {
            val foundTrailheads = mutableSetOf<Coords>()
            
            fun findTrailheadsHelper(currentCoords: Coords) {
                if (input[currentCoords.x][currentCoords.y] == '9') {
                    foundTrailheads.add(currentCoords)
                    return
                }

                val nextDigit = input[currentCoords.x][currentCoords.y] + 1
                for (nextCoords in getNextCoords(currentCoords)) {
                    if (!outOfBounds(input, nextCoords) && input[nextCoords.x][nextCoords.y] == nextDigit)
                        findTrailheadsHelper(nextCoords)
                }
            }
            
            findTrailheadsHelper(coords)
            return foundTrailheads.size
        }
        
        return sumOfTwoDimensional(input, ::findTrailheads)
    }

    fun part2(input: List<String>): Int {

        fun findSumOfRatings(coords: Coords): Int {

            fun findSumOfRatingsHelper(currentCoords: Coords, acc: Int): Int {
                if (input[currentCoords.x][currentCoords.y] == '9') {
                    return acc + 1
                }

                val nextDigit = input[currentCoords.x][currentCoords.y] + 1
                return getNextCoords(currentCoords).sumOf { nextCoords ->
                    if (!outOfBounds(input, nextCoords) && input[nextCoords.x][nextCoords.y] == nextDigit)
                        findSumOfRatingsHelper(nextCoords, acc)
                    else
                        0
                }
            }

            return findSumOfRatingsHelper(coords, 0)
        }

        return sumOfTwoDimensional(input, ::findSumOfRatings)
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
