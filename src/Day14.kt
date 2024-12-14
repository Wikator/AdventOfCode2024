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

    fun part1(input: List<String>): Int {
        val robots = getRobots(input)
        
        val width = 101
        val height = 103
        
        for (i in (0 until 100)) {
            for (robot in robots) {
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
        return input.size
    }

    val input = readInput("Day14")
    part1(input).println()
    part2(input).println()
}
