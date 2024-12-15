fun getInstructions(input: List<String>): List<Direction> =
    input.reversed().takeWhile { it != "" }.reversed().flatMap { row ->
        row.map { char ->
            when (char) {
                '^' -> Direction.Up
                '>' -> Direction.Right
                '<' -> Direction.Left
                'v' -> Direction.Down
                else -> throw IllegalArgumentException("Unknown character")
            }
        }
    }
