enum class Position {
    Obstacle, Empty, GuardUp, GuardRight, GuardDown, GuardLeft
}

data class Coords(val x:Int, val y: Int)

fun moveGuardRight(position: Position): Position =
    when (position) {
        Position.GuardUp -> Position.GuardRight
        Position.GuardRight -> Position.GuardDown
        Position.GuardDown -> Position.GuardLeft
        Position.GuardLeft -> Position.GuardUp
        else -> throw IllegalArgumentException("Not a guard")
    }

fun main() {

    fun getPositions(input: List<String>): List<MutableList<Position>> =
        input.map { text ->
            text.map { char ->
                when (char) {
                    '.' -> Position.Empty
                    '#' -> Position.Obstacle
                    '^' -> Position.GuardUp
                    '>' -> Position.GuardRight
                    'V' -> Position.GuardDown
                    '<' -> Position.GuardLeft
                    else -> throw IllegalArgumentException("Unknown character")
                }
            }.toMutableList()
        }
    
    fun getGuardPosition(positions: List<MutableList<Position>>): Coords {
        for (i in positions.indices) {
            for (j in positions[i].indices) {
                if (
                    positions[i][j] in listOf(
                        Position.GuardUp,
                        Position.GuardRight,
                        Position.GuardDown,
                        Position.GuardLeft
                    )
                ) {
                    return Coords(i, j)
                }
            }
        }
        
        throw Exception("No guard found")
    }

    fun part1(input: List<String>): Int {
        val positions = getPositions(input)
        val nextGuard = getGuardPosition(positions)
        
        fun outOfBounds(coords: Coords): Boolean =
            coords.x < 0 || coords.x >= positions.size || coords.y < 0 || coords.y >= positions[coords.x].size
        
        tailrec fun getAllGuardPositions(coords: Coords, visited: Set<Coords>): Set<Coords> {
            
            val guard = positions[coords.x][coords.y]
            val newCoords = when (guard) {
                Position.GuardUp -> Coords(coords.x - 1, coords.y)
                Position.GuardRight -> Coords(coords.x, coords.y + 1)
                Position.GuardDown -> Coords(coords.x + 1, coords.y)
                Position.GuardLeft -> Coords(coords.x, coords.y - 1)
                else -> throw IllegalArgumentException("Not a guard")
            }

            if (outOfBounds(newCoords)) {
                return visited
            } else if (positions[newCoords.x][newCoords.y] == Position.Obstacle) {
                positions[coords.x][coords.y] = moveGuardRight(guard)
                return getAllGuardPositions(coords, visited)
            } else {
                positions[newCoords.x][newCoords.y] = guard
                positions[coords.x][coords.y] = Position.Empty
                return getAllGuardPositions(newCoords, visited + newCoords)
            }
        }
        
        return getAllGuardPositions(nextGuard, emptySet()).size
    }

    fun part2(input: List<String>): Int {
        val positions = getPositions(input)
        val nextGuard = getGuardPosition(positions)
        val originalGuard = positions[nextGuard.x][nextGuard.y]

        fun outOfBounds(coords: Coords): Boolean =
            coords.x < 0 || coords.x >= positions.size || coords.y < 0 || coords.y >= positions[coords.x].size

        tailrec fun checkForInfiniteLoop(coords: Coords, firstIter: Boolean): Boolean {
            val guard = positions[coords.x][coords.y]
            
            if (!firstIter && coords.x == nextGuard.x && coords.y == nextGuard.y && guard == originalGuard) {
                positions[coords.x][coords.y] = Position.Empty
                return true
            }
            
            val newCoords = when (guard) {
                Position.GuardUp -> Coords(coords.x - 1, coords.y)
                Position.GuardRight -> Coords(coords.x, coords.y + 1)
                Position.GuardDown -> Coords(coords.x + 1, coords.y)
                Position.GuardLeft -> Coords(coords.x, coords.y - 1)
                else -> throw IllegalArgumentException("Not a guard")
            }

            if (outOfBounds(newCoords)) {
                positions[coords.x][coords.y] = Position.Empty
                println("No")
                return false
            } else if (positions[newCoords.x][newCoords.y] == Position.Obstacle) {
                positions[coords.x][coords.y] = moveGuardRight(guard)
                return checkForInfiniteLoop(coords, false)
            } else {
                positions[newCoords.x][newCoords.y] = guard
                positions[coords.x][coords.y] = Position.Empty
                return checkForInfiniteLoop(newCoords, false)
            }
        }

        var count = 0

        for (i in positions.indices) {
            for (j in positions[i].indices) {
                if (positions[i][j] != Position.Obstacle && positions[i][j] != originalGuard) {
                    positions[i][j] = Position.Obstacle
                    println(i)
                    println(j)
                    if (checkForInfiniteLoop(nextGuard, true)) count++

                    positions[i][j] = Position.Empty
                    positions[nextGuard.x][nextGuard.y] = originalGuard
                }
            }
        }
        
        return count
    }

    val input = readInput("Day06_test")
    part1(input).println()
    part2(input).println()
}
