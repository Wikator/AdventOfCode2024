enum class Square1 {
    Wall, Box, Empty
}

fun main() {
    
    fun getBoard(input: List<String>): Pair<List<MutableList<Square1>>, Coords> {
        val relevantInput = input.takeWhile { it != "" }
        var robotCoords = Coords(0, 0)
        
        val board = relevantInput.mapIndexed { x, line ->
            line.mapIndexed { y, char ->
                when (char) {
                    '#' -> Square1.Wall
                    'O' -> Square1.Box
                    '@' -> {
                        robotCoords = Coords(x, y)
                        Square1.Empty
                    }
                    '.' -> Square1.Empty
                    else -> throw IllegalArgumentException("Unknown character")
                }
            }.toMutableList()
        }
        
        return Pair(board, robotCoords)
    }

    fun part1(input: List<String>): Int {
        val (board, initialRobotCoords) = getBoard(input)
        var robotCoords = initialRobotCoords
        val instructions = getInstructions(input)

        fun getNewCoords(coords: Coords, direction: Direction): Coords = when (direction) {
            Direction.Up -> Coords(coords.x - 1, coords.y)
            Direction.Right -> Coords(coords.x, coords.y + 1)
            Direction.Down -> Coords(coords.x + 1, coords.y)
            Direction.Left -> Coords(coords.x, coords.y - 1)
        }
        
        fun moveBoxes(direction: Direction): Boolean {
            val initialPosition = getNewCoords(robotCoords, direction)
            
            tailrec fun moveBoxesHelper(position: Coords): Boolean = when (board[position.x][position.y]) {
                Square1.Wall -> false
                Square1.Box -> moveBoxesHelper(getNewCoords(position, direction))
                Square1.Empty -> {
                    board[initialPosition.x][initialPosition.y] = Square1.Empty
                    board[position.x][position.y] = Square1.Box
                    true
                }
            }
            
            return moveBoxesHelper(initialPosition)
        }
        
        for (instruction in instructions) {
            val newCoords = getNewCoords(robotCoords, instruction)

            when (board[newCoords.x][newCoords.y]) {
                Square1.Empty -> robotCoords = newCoords
                Square1.Wall -> {}
                Square1.Box -> if (moveBoxes(instruction)) robotCoords = newCoords
            }
        }
        
        return board.indices.sumOf { x ->
            (0 until board[x].size).sumOf { y ->
                if (board[x][y] == Square1.Box)
                    100 * x + y
                else
                    0
            }
        }
    }

    val input = readInput("Day15")
    part1(input).println()
}
