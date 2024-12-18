import java.util.PriorityQueue

fun main() {
    
    fun getGraph(maze: List<List<Boolean>>): Map<Coords, List<Coords>> {
        
        fun outOfBounds(input: List<List<Boolean>>, coords: Coords): Boolean =
            coords.x < 0 || coords.x >= input.size || coords.y < 0 || coords.y >= input[coords.x].size
        
        return maze.indices.flatMap { i ->
            (0 until maze[i].size).map { j ->
                val coords = Coords(i, j)
                val adjacentCoords = coords.adjacentCoords()
                coords to adjacentCoords.filter { !outOfBounds(maze, it) && !maze[it.x][it.y] }
            }
        }.toMap()
    }
    
    fun dijkstra(graph: Map<Coords, List<Coords>>, start: Coords): Map<Coords, Int> {
        val distances = mutableMapOf<Coords, Int>().withDefault { Int.MAX_VALUE }
        val priorityQueue = PriorityQueue<Pair<Coords, Int>>(compareBy { it.second })
        val visited = mutableSetOf<Pair<Coords, Int>>()

        priorityQueue.add(start to 0)
        distances[start] = 0

        while (priorityQueue.isNotEmpty()) {
            val (node, currentDist) = priorityQueue.poll()
            if (visited.add(node to currentDist)) {
                graph[node]?.forEach { adjacent ->
                    val totalDist = currentDist + 1
                    if (totalDist < distances.getValue(adjacent)) {
                        distances[adjacent] = totalDist
                        priorityQueue.add(adjacent to totalDist)
                    }
                }
            }
        }
        
        return distances
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
        
        val maze = getMaze(input, 71, 1024)
        val graph = getGraph(maze)

        val distances = dijkstra(graph, Coords(0, 0))
        return distances[Coords(70, 70)] ?: Int.MAX_VALUE
    }

    fun part2(input: List<String>): String {
        val maze = List(71) { MutableList(71) { false } }
        
        for (i in (0 until Int.MAX_VALUE)) {
            val splitLine = input[i].split(',')
            val x = splitLine.last().toInt()
            val y = splitLine.first().toInt()
            maze[x][y] = true
            
            val graph = getGraph(maze)
            val distances = dijkstra(graph, Coords(0, 0))
            if (!distances.containsKey(Coords(70, 70))) return input[i]
        }
        
        throw Error("No solution found")
    }

    val input = readInput("Day18")
    part1(input).println()
    part2(input).println()
}
