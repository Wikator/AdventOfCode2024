enum class Position {
    Obstacle, Empty, GuardUp, GuardRight, GuardDown, GuardLeft
}

enum class Direction {
    Up, Right, Down, Left
}

data class Coords(val x:Int, val y: Int)

data class GuardPosition(val coords: Coords, val direction: Direction)

fun moveRight(direction: Direction): Direction =
    when (direction) {
        Direction.Up -> Direction.Right
        Direction.Right -> Direction.Down
        Direction.Down -> Direction.Left
        Direction.Left -> Direction.Up
    }

fun main() {

    fun getPositions(input: List<String>): List<List<Position>> =
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
    
    fun getGuardPosition(positions: List<List<Position>>): GuardPosition {
        for (i in positions.indices) {
            for (j in positions[i].indices) {
                when {
                    positions[i][j] == Position.GuardUp -> return GuardPosition(Coords(i, j), Direction.Up)
                    positions[i][j] == Position.GuardRight -> return GuardPosition(Coords(i, j), Direction.Right)
                    positions[i][j] == Position.GuardDown -> return GuardPosition(Coords(i, j), Direction.Down)
                    positions[i][j] == Position.GuardLeft -> return GuardPosition(Coords(i, j), Direction.Left)
                }
            }
        }
        
        throw Exception("No guard found")
    }

    fun part1(input: List<String>): Int {
        val positions = getPositions(input)
        val initialGuardPosition = getGuardPosition(positions)
        
        fun outOfBounds(coords: Coords): Boolean =
            coords.x < 0 || coords.x >= positions.size || coords.y < 0 || coords.y >= positions[coords.x].size
        
        tailrec fun getAllGuardPositions(guardPosition: GuardPosition, visited: Set<Coords>): Set<Coords> {

            val newCoords = when (guardPosition.direction) {
                Direction.Up -> Coords(guardPosition.coords.x - 1, guardPosition.coords.y)
                Direction.Right -> Coords(guardPosition.coords.x, guardPosition.coords.y + 1)
                Direction.Down -> Coords(guardPosition.coords.x + 1, guardPosition.coords.y)
                Direction.Left -> Coords(guardPosition.coords.x, guardPosition.coords.y - 1)
            }

            return if (outOfBounds(newCoords)) {
                visited
            } else if (positions[newCoords.x][newCoords.y] == Position.Obstacle) {
                getAllGuardPositions(GuardPosition(guardPosition.coords, moveRight(guardPosition.direction)), visited)
            } else {
                getAllGuardPositions(GuardPosition(newCoords, guardPosition.direction), visited + newCoords)
            }
        }
        
        return getAllGuardPositions(initialGuardPosition, emptySet()).size
    }

    fun part2(input: List<String>): Int {
        val positions = getPositions(input).map { it.toMutableList() }
        val initialGuardPosition = getGuardPosition(positions)


        fun outOfBounds(coords: Coords): Boolean =
            coords.x < 0 || coords.x >= positions.size || coords.y < 0 || coords.y >= positions[coords.x].size

        tailrec fun checkForInfiniteLoop(guardPosition: GuardPosition, visited: Set<GuardPosition>): Boolean {

            val newCoords = when (guardPosition.direction) {
                Direction.Up -> Coords(guardPosition.coords.x - 1, guardPosition.coords.y)
                Direction.Right -> Coords(guardPosition.coords.x, guardPosition.coords.y + 1)
                Direction.Down -> Coords(guardPosition.coords.x + 1, guardPosition.coords.y)
                Direction.Left -> Coords(guardPosition.coords.x, guardPosition.coords.y - 1)
            }

            return if (outOfBounds(newCoords)) {
                false
            } else if (positions[newCoords.x][newCoords.y] == Position.Obstacle) {
                val newGuardPosition = GuardPosition(guardPosition.coords, moveRight(guardPosition.direction))
                if (newGuardPosition in visited) {
                    true
                } else {
                    checkForInfiniteLoop(newGuardPosition, visited + newGuardPosition)
                }
            } else {
                val newGuardPosition = GuardPosition(newCoords, guardPosition.direction)
                if (newGuardPosition in visited) {
                    true
                } else {
                    checkForInfiniteLoop(newGuardPosition, visited + newGuardPosition)
                }

            }
        }

        var count = 0



        for (i in positions.indices) {
            for (j in positions[i].indices) {
                if (positions[i][j] == Position.Empty) {
                    println("Testing row $i column $j")
                    positions[i][j] = Position.Obstacle
                    if (checkForInfiniteLoop(initialGuardPosition, emptySet())) count++

                    positions[i][j] = Position.Empty
                }
            }
        }
        
        return count
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
