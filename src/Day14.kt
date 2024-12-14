fun main() {
    data class Robot(var position: Coords, val velocity: Coords)
    
    fun getRobots(input: List<String>): List<Robot> {
        val regex = Regex("-?\\d+")
        
        return input.map { text ->
            val found = regex.findAll(text)
            val numbers = found.map { f -> f.value.toInt() }.toList()
            Robot(Coords(numbers[1], numbers[0]), Coords(numbers[3], numbers[2]))
        }
    }
    
    fun moveRobot(robot: Robot, height: Int, width: Int) {
        val newX = run {
            val x = robot.position.x + robot.velocity.x
            when {
                x < 0 -> x + height
                x >= height -> x - height
                else -> x
            }
        }
        val newY = run {
            val y = robot.position.y + robot.velocity.y
            when {
                y < 0 -> y + width
                y >= width -> y - width
                else -> y
            }
        }

        robot.position = Coords(newX, newY)
    }

    fun part1(input: List<String>): Int {
        val robots = getRobots(input)
        
        val width = 101
        val height = 103
        
        for (i in (0 until 100)) {
            robots.forEach { moveRobot(it, height, width) }
        }

        val middleHeight = height / 2
        val middleWidth = width / 2
        
        val q1 = robots.count { it.position.x < middleHeight && it.position.y < middleWidth }
        val q2 = robots.count { it.position.x < middleHeight && it.position.y > middleWidth }
        val q3 = robots.count { it.position.x > middleHeight && it.position.y < middleWidth }
        val q4 = robots.count { it.position.x > middleHeight && it.position.y > middleWidth }
        return q1 * q2 * q3 * q4
    }

    fun part2(input: List<String>): Int {
        val robots = getRobots(input)

        val width = 101
        val height = 103

        fun fullLine(start: Int, end: Int, row: Int): Boolean =
            (start..end).all { y -> robots.any { it.position.x == row && it.position.y == y } }

        for (seconds in (1 until Int.MAX_VALUE)) {
            robots.forEach { moveRobot(it, height, width) }
            
            for (robot in robots) {
                var start = robot.position.y
                var end = robot.position.y
                var curr = robot.position.x
                var newLayer = false
                
                do {
                    start--
                    end++
                    curr++
                    
                    if (fullLine(start, end, curr)) {
                        newLayer = false
                        continue
                    }

                    start++
                    end--

                    if (fullLine(start, end, curr)) {
                        if (newLayer) return seconds
                        break
                    }

                    while (start < end) {
                        start++
                        end--

                        if (fullLine(start, end, curr)) {
                            newLayer = true
                            break
                        }
                    }
                } while (start > 0 && end < width && curr < height && start < end)
            }
        }
        
        return 0
    }

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
