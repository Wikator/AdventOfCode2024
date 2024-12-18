import java.util.PriorityQueue

fun main() {

    fun outOfBounds(input: List<List<Boolean>>, coords: Coords): Boolean =
        coords.x < 0 || coords.x >= input.size || coords.y < 0 || coords.y >= input[coords.x].size
    
    fun getGraph(maze: List<List<Boolean>>): Map<Coords, List<Coords>> {
        
        return maze.indices.flatMap { i ->
            (0 until maze[i].size).map { j ->
                val coords = Coords(i, j)
                val adjacentCoords = coords.adjacentCoords()
                coords to adjacentCoords.filter { !outOfBounds(maze, it) && !maze[it.x][it.y] }
            }
        }.toMap()
    }

    fun part1(input: List<String>): Int {
        
        fun getMaze(input: List<String>, dimensions: Int, firstBytes: Int): List<List<Boolean>> {
            val maze = List(dimensions) { MutableList(dimensions) { false } }
            var byte = 0

            for (line in input) {
                byte++
                if (byte > firstBytes) continue

                val splitLine = line.split(',')
                val x = splitLine.last().toInt()
                val y = splitLine.first().toInt()
                maze[x][y] = true
            }

            return maze.map { it.toList() }
        }

        fun dijkstra(graph: Map<Coords, List<Coords>>, start: Coords, end: Coords): Int {
            val distances = mutableMapOf<Coords, Int>().withDefault { Int.MAX_VALUE }
            val priorityQueue = PriorityQueue<Pair<Coords, Int>>(compareBy { it.second })

            priorityQueue.add(start to 0)
            distances[start] = 0

            while (priorityQueue.isNotEmpty()) {
                val (node, currentDist) = priorityQueue.poll()
                if (node == end) return currentDist

                graph[node]?.forEach { adjacent ->
                    val totalDist = currentDist + 1
                    if (totalDist < distances.getValue(adjacent)) {
                        distances[adjacent] = totalDist
                        priorityQueue.add(adjacent to totalDist)
                    }
                }
            }

            return Int.MAX_VALUE
        }
        
        val maze = getMaze(input, 71, 1024)
        val graph = getGraph(maze)

        return dijkstra(graph, Coords(0, 0), Coords(70, 70))
    }

    fun part2(input: List<String>): String {
        val maze = List(71) { MutableList(71) { false } }
        
        fun findEnd(maze: List<List<Boolean>>, coords: Coords, end: Coords, visited: MutableSet<Coords>): Boolean {
            if (coords in visited || outOfBounds(maze, coords) || maze[coords.x][coords.y]) return false
            if (coords == end) return true
            
            val adjacentCoords = coords.adjacentCoords().toList()
            visited.add(coords)
            return findEnd(maze, adjacentCoords[0], end, visited) || findEnd(maze, adjacentCoords[1], end, visited)
                    || findEnd(maze, adjacentCoords[2], end, visited) || findEnd(maze, adjacentCoords[3], end, visited)
        }
        
        for (i in (0 until Int.MAX_VALUE)) {
            val line = input[i]
            val splitLine = line.split(',')
            val x = splitLine.last().toInt()
            val y = splitLine.first().toInt()
            maze[x][y] = true
            
            if (!findEnd(maze, Coords(0, 0), Coords(70, 70), mutableSetOf())) return line
        }
        
        throw Error("No solution found")
    }

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
