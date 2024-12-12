import kotlin.math.abs

fun main() {
    data class Plot(val plant: Char, val coords: Coords)

    fun getPlots(input: List<String>): Collection<List<Coords>> {
        val plots = input.flatMapIndexed { x, row ->
            row.mapIndexed { y, char -> Plot(char, Coords(x, y))}
        }

        val groupedPlots = plots.groupBy(
            keySelector = { it.plant },
            valueTransform = { it.coords }
        )

        return groupedPlots.values
    }

    tailrec fun getAdjacentPlants(plots: List<Coords>, currentCoords: List<Coords>, acc: MutableSet<Coords>): Set<Coords> {
        if (currentCoords.isEmpty()) return acc.toSet()

        val newCurrentCoords = currentCoords.flatMap { curr ->
            val adjacentPlots = plots.filter { coords ->
                if (coords == curr) {
                    true
                } else {
                    val distance = coords.distance(curr)
                    (distance.x == 0 && abs(distance.y) == 1) || (abs(distance.x) == 1 && distance.y == 0)
                }
            }

            adjacentPlots.filter { coords -> acc.add(coords) }
        }

        return getAdjacentPlants(plots, newCurrentCoords, acc)
    }


    fun part1(input: List<String>): Int {
        val plots = getPlots(input)

        return plots.sumOf { plot ->
            val groups = mutableListOf<Set<Coords>>()

            for (coords in plot) {
                if (groups.any { group -> group.any { it == coords } }) continue

                val newGroup = getAdjacentPlants(plot, listOf(coords), mutableSetOf())
                groups.add(newGroup)
            }

            groups.sumOf { group ->
                val area = group.size
                area * group.sumOf { coords ->
                    val adjacentCoords = coords.adjacentCoords()
                    adjacentCoords.count { it !in group }
                }
            }
        }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
