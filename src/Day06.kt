enum class Position {
    Obstacle, Empty, GuardUp, GuardRight, GuardDown, GuardLeft
}

fun main() {

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

    fun outOfBounds(input: List<List<Position>>, coords: Coords): Boolean =
        coords.x < 0 || coords.x >= input.size || coords.y < 0 || coords.y >= input[coords.x].size

    fun part1(input: List<String>): Int {
        val positions = getPositions(input)
        val initialGuardPosition = getGuardPosition(positions)
        
        fun getVisitedPositions(initialGuardPosition: GuardPosition): Set<Coords> {
            
            val visited = mutableSetOf<Coords>()
            var guardPosition = initialGuardPosition
            
            while (true) {
                val newCoords = guardPosition.getNextCoords()
                
                if (outOfBounds(positions, newCoords)) return visited.toSet()
                
                val newGuardPosition = if (positions[newCoords.x][newCoords.y] == Position.Obstacle) {
                    GuardPosition(guardPosition.coords, guardPosition.direction.moveRight())
                } else {
                    val newGuardPosition = GuardPosition(newCoords, guardPosition.direction)
                    visited.add(newGuardPosition.coords)
                    newGuardPosition
                }

                guardPosition = newGuardPosition
            }
        }
        
        return getVisitedPositions(initialGuardPosition).size
    }

    fun part2(input: List<String>): Int {
        val positions = getPositions(input)
        val initialGuardPosition = getGuardPosition(positions)

        fun getGuardPath(initialGuardPosition: GuardPosition): List<GuardPosition> {
            
            val path = mutableListOf<GuardPosition>()
            var guardPosition = initialGuardPosition
            
            while (true) {
                val newCoords = guardPosition.getNextCoords()
                
                if (outOfBounds(positions, newCoords)) return path.toList()

                val newGuardPosition = if (positions[newCoords.x][newCoords.y] == Position.Obstacle) {
                    GuardPosition(guardPosition.coords, guardPosition.direction.moveRight())
                } else {
                    GuardPosition(newCoords, guardPosition.direction)
                }

                path.add(newGuardPosition)
                guardPosition = newGuardPosition
            }
        }

        fun checkForInfiniteLoop(initialGuardPosition: GuardPosition, extraObstacleCoords: Coords): Boolean {

            val visited = mutableSetOf<GuardPosition>()
            var currentGuardPosition = initialGuardPosition
            
            while (true) {
                val newCoords = currentGuardPosition.getNextCoords()

                if (outOfBounds(positions, newCoords)) return false
                
                val newGuardPosition = if (
                    newCoords == extraObstacleCoords ||
                    positions[newCoords.x][newCoords.y] == Position.Obstacle
                ) {
                    GuardPosition(currentGuardPosition.coords, currentGuardPosition.direction.moveRight())
                } else {
                    GuardPosition(newCoords, currentGuardPosition.direction)
                }

                if (newGuardPosition in visited) return true
  
                visited.add(newGuardPosition)
                currentGuardPosition = newGuardPosition
            }
        }

        val guardPath = getGuardPath(initialGuardPosition)
        val relevantNewObstaclePositions = guardPath.fold(mutableMapOf<Coords, GuardPosition>()) { acc, curr ->
            acc.apply { putIfAbsent(curr.coords, curr) }
        }.values.toSet()
        
        return relevantNewObstaclePositions.count { position ->
            val guardPosition = position.getPreviousCoords()
            checkForInfiniteLoop(GuardPosition(guardPosition, position.direction), position.coords)
        }
    }

    val input = readInput("Day06")

    part1(input).println()
    part2(input).println()
}
