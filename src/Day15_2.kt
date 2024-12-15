enum class Square2 {
    Wall, LeftBox, RightBox, Empty
}

fun main() {

    fun getBoard(input: List<String>): Pair<List<MutableList<Square2>>, Coords> {
        val relevantInput = input.takeWhile { it != "" }
        var robotCoords = Coords(0, 0)

        val board = relevantInput.mapIndexed { x, line ->
            line.flatMapIndexed { y, char ->
                when (char) {
                    '#' -> listOf(Square2.Wall, Square2.Wall)
                    'O' -> listOf(Square2.LeftBox, Square2.RightBox)
                    '@' -> {
                        robotCoords = Coords(x, y * 2)
                        listOf(Square2.Empty, Square2.Empty)
                    }
                    '.' -> listOf(Square2.Empty, Square2.Empty)
                    else -> throw IllegalArgumentException("Unknown character")
                }
            }.toMutableList()
        }

        return Pair(board, robotCoords)
    }

    fun part2(input: List<String>): Int {
        val (board, initialRobotCoords) = getBoard(input)
        var robotCoords = initialRobotCoords
        val instructions = getInstructions(input)

        fun getNewCoords(coords: Coords, direction: Direction): Coords = when (direction) {
            Direction.Up -> Coords(coords.x - 1, coords.y)
            Direction.Right -> Coords(coords.x, coords.y + 1)
            Direction.Down -> Coords(coords.x + 1, coords.y)
            Direction.Left -> Coords(coords.x, coords.y - 1)
        }
        
        fun moveBox(leftBox: Coords, newLeftBox: Coords, newRightBox: Coords) {
            board[leftBox.x][leftBox.y] = Square2.Empty
            board[leftBox.x][leftBox.y + 1] = Square2.Empty
            board[newLeftBox.x][newLeftBox.y] = Square2.LeftBox
            board[newRightBox.x][newRightBox.y] = Square2.RightBox
        }

        fun moveBoxes(direction: Direction): Boolean {
            val initialPosition = getNewCoords(robotCoords, direction)
            
            fun nextBoxPosition(leftBox: Coords): Pair<Coords, Coords> = when (direction) {
                Direction.Up -> Pair(Coords(leftBox.x - 1, leftBox.y), Coords(leftBox.x - 1, leftBox.y + 1))
                Direction.Right -> Pair(Coords(leftBox.x, leftBox.y + 1), Coords(leftBox.x, leftBox.y + 2))
                Direction.Down -> Pair(Coords(leftBox.x + 1, leftBox.y), Coords(leftBox.x + 1, leftBox.y + 1))
                Direction.Left -> Pair(Coords(leftBox.x, leftBox.y - 1), Coords(leftBox.x, leftBox.y))
            }

            fun moveBoxesHelper(leftBox: Coords, dry: Boolean): Boolean {
                val (newLeftBox, newRightBox) = nextBoxPosition(leftBox)
                val newLeftBoxSquare = board[newLeftBox.x][newLeftBox.y]
                val newRightBoxSquare = board[newRightBox.x][newRightBox.y]
                
                return when (direction) {
                    Direction.Right -> when (newRightBoxSquare) {
                        Square2.Empty -> {
                            moveBox(leftBox, newLeftBox, newRightBox)
                            true
                        }
                        Square2.Wall -> {
                            false
                        }
                        Square2.LeftBox -> {
                            if (moveBoxesHelper(newRightBox, false)) {
                                moveBox(leftBox, newLeftBox, newRightBox)
                                true
                            } else {
                                false
                            }
                        }
                        else -> throw Error("This shouldn't have happened")
                    }
                    Direction.Left -> when (newLeftBoxSquare) {
                        Square2.Empty -> {
                            moveBox(leftBox, newLeftBox, newRightBox)
                            true
                        }
                        Square2.Wall -> {
                            false
                        }
                        Square2.RightBox -> {
                            if (moveBoxesHelper(Coords(newLeftBox.x, newLeftBox.y - 1), false)) {
                                moveBox(leftBox, newLeftBox, newRightBox)
                                true
                            } else {
                                false
                            }
                        }
                        else -> throw Error("This shouldn't have happened")
                    }
                    else -> when {
                        newLeftBoxSquare == Square2.Empty && newRightBoxSquare == Square2.Empty -> {
                            if (!dry) moveBox(leftBox, newLeftBox, newRightBox)
                            true
                        }
                        newLeftBoxSquare == Square2.Wall || newRightBoxSquare == Square2.Wall -> {
                            false
                        }
                        newLeftBoxSquare == Square2.RightBox && newRightBoxSquare == Square2.LeftBox -> {
                            if (moveBoxesHelper(Coords(newLeftBox.x, newLeftBox.y - 1), true) && moveBoxesHelper(newRightBox, true)) {
                                moveBoxesHelper(Coords(newLeftBox.x, newLeftBox.y - 1), dry)
                                moveBoxesHelper(newRightBox, dry)
                                if (!dry) moveBox(leftBox, newLeftBox, newRightBox)
                                true
                            } else {
                                false
                            }
                        }
                        newLeftBoxSquare == Square2.RightBox -> {
                            if (moveBoxesHelper(Coords(newLeftBox.x, newLeftBox.y - 1), dry)) {
                                if (!dry) moveBox(leftBox, newLeftBox, newRightBox)
                                true
                            } else {
                                false
                            }
                        }
                        newLeftBoxSquare == Square2.LeftBox -> {
                            if (moveBoxesHelper(newLeftBox, dry)) {
                                if (!dry) moveBox(leftBox, newLeftBox, newRightBox)
                                true
                            } else {
                                false
                            }
                        }
                        newRightBoxSquare == Square2.LeftBox -> {
                            if (moveBoxesHelper(newRightBox, dry)) {
                                if (!dry) moveBox(leftBox, newLeftBox, newRightBox)
                                true
                            } else {
                                false
                            }
                        }
                        else -> throw Error("This shouldn't have happened")
                    }
                }
            }

            val position = if (board[initialPosition.x][initialPosition.y] == Square2.LeftBox)
                initialPosition
            else
                Coords(initialPosition.x, initialPosition.y - 1)
                    
            return moveBoxesHelper(position, false)
        }
        
        for (instruction in instructions) {
            val newCoords = getNewCoords(robotCoords, instruction)
            when (board[newCoords.x][newCoords.y]) {
                Square2.Empty -> robotCoords = newCoords
                Square2.Wall -> {}
                Square2.LeftBox -> if (moveBoxes(instruction)) robotCoords = newCoords
                Square2.RightBox -> if (moveBoxes(instruction)) robotCoords = newCoords
            }
        }

        return board.indices.sumOf { x ->
            (0 until board[x].size).sumOf { y ->
                if (board[x][y] == Square2.LeftBox)
                    100 * x + y
                else
                    0
            }
        }
    }

    val input = readInput("Day15")
    part2(input).println()
}
