import kotlin.math.min

fun main() {
    
    fun getMaze(input: List<String>): Triple<List<List<Boolean>>, Coords, Coords> {
        var start = Coords(0, 0)
        var end = Coords(0, 0)
        
        val maze = input.mapIndexed { x, row ->
            row.mapIndexed { y, char ->
                when (char) {
                    '#' -> true
                    '.' -> false
                    'S' -> {
                        start = Coords(x, y)
                        false
                    }
                    'E' -> {
                        end = Coords(x, y)
                        false
                    }
                    else -> throw IllegalArgumentException("Unknown character")
                }
            }
        }
        
        return Triple(maze, start, end)
    }

    fun getNextCoords(coords: Coords, direction: Direction): Triple<Coords, Coords, Coords> = when (direction) {
        Direction.Up -> Triple(Coords(coords.x - 1, coords.y), Coords(coords.x, coords.y - 1),
            Coords(coords.x, coords.y + 1))

        Direction.Right -> Triple(Coords(coords.x, coords.y + 1), Coords(coords.x - 1, coords.y),
            Coords(coords.x + 1, coords.y))

        Direction.Down -> Triple(Coords(coords.x + 1, coords.y), Coords(coords.x, coords.y + 1),
            Coords(coords.x, coords.y - 1))

        Direction.Left -> Triple(Coords(coords.x, coords.y - 1), Coords(coords.x + 1, coords.y),
            Coords(coords.x - 1, coords.y))
    }
    
    fun part1(input: List<String>): Int {
        data class Arrow(val coords: Coords, val direction: Direction, val score: Int)
        
        val (maze, start, end) = getMaze(input)
        
        var result = Int.MAX_VALUE
        var arrows = setOf(Arrow(start, Direction.Right, 0))
        val visited = mutableMapOf<Pair<Coords, Direction>, Int>()
        
        while (arrows.isNotEmpty()) {
            val nextIteration = mutableSetOf<Arrow>()
            
            for (arrow in arrows) {
                if ((visited[Pair(arrow.coords, arrow.direction)] ?: Int.MAX_VALUE) <= arrow.score) continue
                
                if (arrow.coords == end) {
                    result = min(result, arrow.score)
                    continue
                }

                val (forwardCoords, sideCoords1, sideCoords2) = getNextCoords(arrow.coords, arrow.direction)
                visited[Pair(arrow.coords, arrow.direction)] = arrow.score

                if (!maze[forwardCoords.x][forwardCoords.y]) {
                    val forwardArrow = Arrow(forwardCoords, arrow.direction, arrow.score + 1)
                    nextIteration.add(forwardArrow)
                }
                
                if (!maze[sideCoords1.x][sideCoords1.y]) {
                    val leftArrow = Arrow(sideCoords1, arrow.direction.moveLeft(), arrow.score + 1001)
                    nextIteration.add(leftArrow)
                }

                if (!maze[sideCoords2.x][sideCoords2.y]) {
                    val rightArrow = Arrow(sideCoords2, arrow.direction.moveRight(), arrow.score + 1001)
                    nextIteration.add(rightArrow)
                }
            }

            arrows = nextIteration
        }
        
        return result
    }

    fun part2(input: List<String>): Int {
        data class Arrow(val coords: Coords, val direction: Direction, val score: Int, val visited: Set<Coords>)

        val (maze, start, end) = getMaze(input)

        var minPoints = Int.MAX_VALUE
        val result = mutableSetOf<Coords>()
        var arrows = setOf(Arrow(start, Direction.Right, 0, emptySet()))
        val visited = mutableMapOf<Pair<Coords, Direction>, Int>()

        while (arrows.isNotEmpty()) {
            val nextIteration = mutableSetOf<Arrow>()

            for (arrow in arrows) {
                if ((visited[Pair(arrow.coords, arrow.direction)] ?: Int.MAX_VALUE) < arrow.score) continue

                if (arrow.coords == end) {
                    if (arrow.score < minPoints) {
                        minPoints = arrow.score
                        result.clear()
                        result.addAll(arrow.visited)
                    } else if (arrow.score == minPoints) {
                        result.addAll(arrow.visited)
                    }
                        
                    continue
                }

                val (forwardCoords, sideCoords1, sideCoords2) = getNextCoords(arrow.coords, arrow.direction)
                visited[Pair(arrow.coords, arrow.direction)] = arrow.score

                if (!maze[forwardCoords.x][forwardCoords.y]) {
                    val forwardArrow = Arrow(forwardCoords, arrow.direction, arrow.score + 1, arrow.visited + arrow.coords)
                    nextIteration.add(forwardArrow)
                }

                if (!maze[sideCoords1.x][sideCoords1.y]) {
                    val leftArrow = Arrow(sideCoords1, arrow.direction.moveLeft(), arrow.score + 1001, arrow.visited + arrow.coords)
                    nextIteration.add(leftArrow)
                }

                if (!maze[sideCoords2.x][sideCoords2.y]) {
                    val rightArrow = Arrow(sideCoords2, arrow.direction.moveRight(), arrow.score + 1001, arrow.visited + arrow.coords)
                    nextIteration.add(rightArrow)
                }
            }

            arrows = nextIteration
        }

        return result.size + 1
    }

    val input = readInput("Day16")
    part1(input).println()
    part2(input).println()
}
