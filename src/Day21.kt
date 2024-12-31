import java.util.PriorityQueue

fun main() {

    data class DirectedNode(val node: Char, val direction: Char)

    fun getKeypads(): Pair<Map<Char, List<DirectedNode>>, Map<Char, List<DirectedNode>>> {
        val up = '^'
        val down = 'v'
        val left = '<'
        val right = '>'
        val activate = 'A'

//          +---+---+
//          | ^ | A |
//      +---+---+---+
//      | < | v | > |
//      +---+---+---+
        val directionalKeypad = mapOf(
            up to listOf(DirectedNode(activate, right), DirectedNode(down, down)),
            activate to listOf(DirectedNode(up, left), DirectedNode(right, down)),
            right to listOf(DirectedNode(activate, up), DirectedNode(down, left)),
            down to listOf(DirectedNode(left, left), DirectedNode(up, up), DirectedNode(right, right)),
            left to listOf(DirectedNode(down, right))
        )

//      +---+---+---+
//      | 7 | 8 | 9 |
//      +---+---+---+
//      | 4 | 5 | 6 |
//      +---+---+---+
//      | 1 | 2 | 3 |
//      +---+---+---+
//          | 0 | A |
//          +---+---+
        val numericalKeyboard = mapOf(
            'A' to listOf(DirectedNode('0', left), DirectedNode('3', up)),
            '0' to listOf(DirectedNode('A', right), DirectedNode('2', up)),
            '1' to listOf(DirectedNode('2', right), DirectedNode('4', up)),
            '2' to listOf(
                DirectedNode('0', down),
                DirectedNode('1', left),
                DirectedNode('3', right),
                DirectedNode('5', up)
            ),
            '3' to listOf(DirectedNode('A', down), DirectedNode('2', left), DirectedNode('6', up)),
            '4' to listOf(DirectedNode('1', down), DirectedNode('5', right), DirectedNode('7', up)),
            '5' to listOf(
                DirectedNode('2', down),
                DirectedNode('4', left),
                DirectedNode('6', right),
                DirectedNode('8', up)
            ),
            '6' to listOf(DirectedNode('3', down), DirectedNode('5', left), DirectedNode('9', up)),
            '7' to listOf(DirectedNode('4', down), DirectedNode('8', right)),
            '8' to listOf(DirectedNode('5', down), DirectedNode('7', left), DirectedNode('9', right)),
            '9' to listOf(DirectedNode('6', down), DirectedNode('8', left))
        )


        return Pair(directionalKeypad, numericalKeyboard)
    }

    fun dijkstra(graph: Map<Char, List<DirectedNode>>, start: Char, end: Char): Int {
        val distances = mutableMapOf<Char, Int>()
        val priorityQueue = PriorityQueue<Pair<Char, Int>>(compareBy { it.second })

        priorityQueue.add(start to 0)
        distances[start] = 0

        while (priorityQueue.isNotEmpty()) {
            val (node, distance) = priorityQueue.poll()

            if (node == end)
                return distance

            graph[node]?.forEach { (adjacent, _) ->
                val totalDistance = distance + 1

                if (totalDistance < (distances[adjacent]?: Int.MAX_VALUE)) {
                    distances[adjacent] = totalDistance
                    priorityQueue.add(adjacent to totalDistance)
                }
            }
        }

        throw Error("Not found")
    }

    fun dijkstra(graph: Map<Char, List<DirectedNode>>, penalties: Map<Char, Map<Char, Long>>,
                 start: Char, end: Char): Long {
        if (start == end) return 1L

        val distances = mutableMapOf<DirectedNode, Long>()
        val priorityQueue = PriorityQueue<Pair<DirectedNode, Long>>(compareBy { it.second })

        priorityQueue.add(DirectedNode(start, 'A') to 0L)
        distances[DirectedNode(start, 'A')] = 0L

        while (priorityQueue.isNotEmpty()) {
            val (nodeFrom, distance) = priorityQueue.poll()

            graph[nodeFrom.node]?.forEach { nodeTo ->
                val totalDistance = distance + penalties[nodeFrom.direction]!![nodeTo.direction]!!

                val previousDistance = distances[nodeTo]
                if (previousDistance == null || totalDistance < previousDistance) {
                    distances[nodeTo] = totalDistance
                    priorityQueue.add(nodeTo to totalDistance)
                }
            }
        }

        return distances.minOf { (nodeFrom, distance) ->
            val endNodeDirection = graph[nodeFrom.node]!!.firstOrNull { it.node == end }?.direction

            if (endNodeDirection == null) {
                Long.MAX_VALUE
            } else {
                val newDistance = penalties[nodeFrom.direction]!![endNodeDirection]
                distance + newDistance!! + penalties[endNodeDirection]!!['A']!!
            }
        }
    }

    fun getRobotPath(penalties: Map<Char, Map<Char, Long>>, sequence: String): Long {
        var currentPosition = 'A'
        val distance = sequence.sumOf { destination ->
            val distance = penalties[currentPosition]!![destination]!!
            currentPosition = destination
            distance
        }

        return distance
    }

    fun solve(input: List<String>, directionalKeypads: Int): Long {
        val (directionalKeypad, numericalKeypad) = getKeypads()

        val directionalKeypadButtons = directionalKeypad.keys
        val robot1Distances = directionalKeypadButtons.associateWith { start ->
            directionalKeypadButtons.associateWith { end ->
                dijkstra(directionalKeypad, start, end) + 1L
            }
        }

        val robotXDistances = (0 until directionalKeypads - 1).fold(robot1Distances) { path, _ ->
            directionalKeypadButtons.associateWith { start ->
                directionalKeypadButtons.associateWith { end ->
                    dijkstra(directionalKeypad, path, start, end)
                }
            }
        }

        val keys = numericalKeypad.keys
        val numericalRobotDistances = keys.associateWith { start ->
            keys.associateWith { end ->
                dijkstra(numericalKeypad, robotXDistances, start, end)
            }
        }

        return input.sumOf { code ->
            val distance = getRobotPath(numericalRobotDistances, code)
            val numericalValue = code.filter { it.isDigit() }.toInt()
            distance * numericalValue
        }
    }

    fun part1(input: List<String>): Int {
        return solve(input, 2).toInt()
    }

    fun part2(input: List<String>): Long {
        return solve(input, 25)
    }

    val input = readInput("Day21")
    part1(input).println()
    part2(input).println()
}
