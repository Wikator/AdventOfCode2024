import java.util.*

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
    
    fun getGraph(maze: List<List<Boolean>>): Map<Coords, Map<Direction, List<Pair<Pair<Direction, Coords>, Int>>>> =
        maze.flatMapIndexed { x, row ->
            row.mapIndexed { y, cell ->
                if (cell) {
                    Coords(x, y) to emptyMap()
                } else {
                    val upCoords = Coords(x - 1, y)
                    val rightCoords = Coords(x, y + 1)
                    val downCoords = Coords(x + 1, y)
                    val leftCoords = Coords(x, y - 1)
                    val upCoordsWall = maze[x - 1][y]
                    val rightCoordsWall = maze[x][y + 1]
                    val downCoordsWall = maze[x + 1][y]
                    val leftCoordsWall = maze[x][y - 1]
                    Coords(x, y) to mapOf(
                        Direction.Up to run {
                            val toAdd = mutableListOf<Pair<Pair<Direction, Coords>, Int>>()
                            if (!upCoordsWall) toAdd.add(Direction.Up to upCoords to 1)
                            if (!leftCoordsWall) toAdd.add(Direction.Left to leftCoords to 1001)
                            if (!rightCoordsWall) toAdd.add(Direction.Right to rightCoords to 1001)
                            toAdd.toList()
                        },
                        Direction.Right to run {
                            val toAdd = mutableListOf<Pair<Pair<Direction, Coords>, Int>>()
                            if (!rightCoordsWall) toAdd.add(Direction.Right to rightCoords to 1)
                            if (!upCoordsWall) toAdd.add(Direction.Up to upCoords to 1001)
                            if (!downCoordsWall) toAdd.add(Direction.Down to downCoords to 1001)
                            toAdd.toList()
                        },
                        Direction.Down to run {
                            val toAdd = mutableListOf<Pair<Pair<Direction, Coords>, Int>>()
                            if (!downCoordsWall) toAdd.add(Direction.Down to downCoords to 1)
                            if (!rightCoordsWall) toAdd.add(Direction.Right to rightCoords to 1001)
                            if (!leftCoordsWall) toAdd.add(Direction.Left to leftCoords to 1001)
                            toAdd.toList()
                        },
                        Direction.Left to run {
                            val toAdd = mutableListOf<Pair<Pair<Direction, Coords>, Int>>()
                            if (!leftCoordsWall) toAdd.add(Direction.Left to leftCoords to 1)
                            if (!downCoordsWall) toAdd.add(Direction.Down to downCoords to 1001)
                            if (!upCoordsWall) toAdd.add(Direction.Up to upCoords to 1001)
                            toAdd.toList()
                        }
                    )
                }

            }
        }.toMap()
    
    fun part1(input: List<String>): Int {

        fun dijkstra(graph: Map<Coords, Map<Direction, List<Pair<Pair<Direction, Coords>, Int>>>>,
                     start: Pair<Direction, Coords>, end: Coords): Int {

            val distances = mutableMapOf<Pair<Direction, Coords>, Int>().withDefault { Int.MAX_VALUE }
            val priorityQueue = PriorityQueue<Pair<Pair<Direction, Coords>, Int>>(compareBy { it.second })

            priorityQueue.add(start to 0)
            distances[start] = 0

            while (priorityQueue.isNotEmpty()) {
                val (node, currentDist) = priorityQueue.poll()

                if (node.second == end) return currentDist

                graph[node.second]?.get(node.first)?.forEach { (adjacent, weight) ->
                    val totalDist = currentDist + weight
                    if (totalDist < distances.getValue(adjacent)) {
                        distances[adjacent] = totalDist
                        priorityQueue.add(adjacent to totalDist)
                    }
                }
            }
            return Int.MAX_VALUE
        }
        
        val (maze, start, end) = getMaze(input)
        val graph = getGraph(maze)
        return dijkstra(graph, Direction.Right to start, end)
    }

    fun part2(input: List<String>): Int {
        
        data class Arrow(val coords: Coords, val direction: Direction, val score: Int, val visited: Set<Coords>)

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
