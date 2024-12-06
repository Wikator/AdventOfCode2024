enum class Position {
    Obstacle, Empty, GuardUp, GuardRight, GuardDown, GuardLeft
}

enum class Direction {
    Up, Right, Down, Left;

    fun moveRight(): Direction = when (this) {
        Up -> Right
        Right -> Down
        Down -> Left
        Left -> Up
    }
}

data class Coords(val x:Int, val y: Int)

data class GuardPosition(val coords: Coords, val direction: Direction) {

    fun getNextCoords(): Coords = when (direction) {
        Direction.Up -> Coords(coords.x - 1, coords.y)
        Direction.Right -> Coords(coords.x, coords.y + 1)
        Direction.Down -> Coords(coords.x + 1, coords.y)
        Direction.Left -> Coords(coords.x, coords.y - 1)
    }
    
    fun getPreviousCoords(): Coords = when (direction) {
        Direction.Up -> Coords(coords.x + 1, coords.y)
        Direction.Right -> Coords(coords.x, coords.y - 1)
        Direction.Down -> Coords(coords.x - 1, coords.y)
        Direction.Left -> Coords(coords.x, coords.y + 1)
    }
}

fun outOfBounds(positions: List<List<Position>>, coords: Coords): Boolean =
    coords.x < 0 || coords.x >= positions.size || coords.y < 0 || coords.y >= positions[coords.x].size

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
            }
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
        
        tailrec fun getVisitedPositions(guardPosition: GuardPosition, visited: Set<Coords>): Set<Coords> {
            val newCoords = guardPosition.getNextCoords()

            return if (outOfBounds(positions, newCoords)) {
                visited
            } else if (positions[newCoords.x][newCoords.y] == Position.Obstacle) {
                getVisitedPositions(GuardPosition(guardPosition.coords, guardPosition.direction.moveRight()), visited)
            } else {
                getVisitedPositions(GuardPosition(newCoords, guardPosition.direction), visited + newCoords)
            }
        }
        
        return getVisitedPositions(initialGuardPosition, emptySet()).size
    }

    fun part2(input: List<String>): Int {
        val positions = getPositions(input)
        val initialGuardPosition = getGuardPosition(positions)

        tailrec fun getGuardPath(guardPosition: GuardPosition, path: List<GuardPosition>): List<GuardPosition> {

            val newCoords = guardPosition.getNextCoords()

            return if (outOfBounds(positions, newCoords)) {
                path
            } else {
                val newGuardPosition = if (positions[newCoords.x][newCoords.y] == Position.Obstacle) {
                    GuardPosition(guardPosition.coords, guardPosition.direction.moveRight())
                } else {
                    GuardPosition(newCoords, guardPosition.direction)
                }

                getGuardPath(newGuardPosition, path + newGuardPosition)
            }
        }

        tailrec fun checkForInfiniteLoop(guardPosition: GuardPosition, extraObstacleCoords: Coords,
                                         visited: Set<GuardPosition>): Boolean {

            val newCoords = guardPosition.getNextCoords()

            return if (outOfBounds(positions, newCoords)) {
                false
            } else {
                val newGuardPosition = if (
                    newCoords == extraObstacleCoords ||
                    positions[newCoords.x][newCoords.y] == Position.Obstacle
                ) {
                    GuardPosition(guardPosition.coords, guardPosition.direction.moveRight())
                } else {
                    GuardPosition(newCoords, guardPosition.direction)
                }

                if (newGuardPosition in visited) {
                    true
                } else {
                    checkForInfiniteLoop(newGuardPosition, extraObstacleCoords, visited + newGuardPosition)
                }
            }
        }

        val guardPath = getGuardPath(initialGuardPosition, emptyList())
        val relevantNewObstaclePositions = guardPath.fold(emptySet<GuardPosition>()) { acc, curr ->
            if (acc.any { it.coords == curr.coords })
                acc
            else
                acc + curr
        }
        
        return relevantNewObstaclePositions.count { position ->
            val guardPosition = position.getPreviousCoords()
            checkForInfiniteLoop(GuardPosition(guardPosition, position.direction), position.coords, emptySet())
        }
    }

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
