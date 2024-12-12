import kotlin.math.abs

fun main() {
    fun getGroupedPlants(input: List<String>): Collection<List<Coords>> {
        val plots = input.flatMapIndexed { x, row ->
            row.mapIndexed { y, char -> Pair(char, Coords(x, y)) }
        }
    
        val groupedPlants = plots.groupBy(
            keySelector = { it.first },
            valueTransform = { it.second }
        )
    
        return groupedPlants.values
    }

    // Searches for coords of all plants in the same region as plants in currentPlantCoords.
    // currentPlantCoords should have initially plants all in the same region, or a single plant.
    tailrec fun getPlantRegion(plants: List<Coords>, currentPlantCoords: List<Coords>,
                               acc: MutableSet<Coords> = mutableSetOf()): Set<Coords> {
        
        if (currentPlantCoords.isEmpty()) return acc.toSet()

        val newCurrentCoords = currentPlantCoords.flatMap { curr ->
            val adjacentPlants = plants.filter { coords ->
                if (coords == curr) {
                    true
                } else {
                    val distance = coords.distance(curr)
                    (distance.x == 0 && abs(distance.y) == 1) || (abs(distance.x) == 1 && distance.y == 0)
                }
            }

            adjacentPlants.filter { coords -> acc.add(coords) }
        }

        return getPlantRegion(plants, newCurrentCoords, acc)
    }
    
    fun getRegions(plants: List<Coords>): List<Set<Coords>> {
        val regions = mutableListOf<Set<Coords>>()

        for (coords in plants) {
            if (regions.any { group -> group.any { it == coords } }) continue

            val newRegion = getPlantRegion(plants, listOf(coords))
            regions.add(newRegion)
        }
        
        return regions
    }
    
    fun part1(input: List<String>): Int {
        val groupedPlants = getGroupedPlants(input)

        return groupedPlants.sumOf { plants ->
            val regions = getRegions(plants)

            regions.sumOf { region ->
                val area = region.size
                val perimeter = region.sumOf { coords ->
                    val adjacentCoords = coords.adjacentCoords()
                    adjacentCoords.count { it !in region }
                }
                area * perimeter
            }
        }
    }

    fun part2(input: List<String>): Int {
        val groupedPlants = getGroupedPlants(input)

        return groupedPlants.sumOf { plot ->
            val regions = getRegions(plot)

            regions.sumOf { region ->
                val area = region.size

                val plantBorders = region.associateWith { coords ->
                    val adjacentCoords = coords.adjacentCoords()
                    adjacentCoords.filter { it !in region }
                }

                val perimeter = plantBorders.entries.sumOf { (plant, borders) ->
                    borders.count { curr ->
                        val coordsMovement = when {
                            curr.x < plant.x -> { coords: Coords -> Coords(coords.x, coords.y + 1) }
                            curr.x > plant.x -> { coords: Coords -> Coords(coords.x, coords.y - 1) }
                            curr.y < plant.y -> { coords: Coords -> Coords(coords.x - 1, coords.y) }
                            curr.y > plant.y -> { coords: Coords -> Coords(coords.x + 1, coords.y) }
                            else -> throw Error("This should not have happened!")
                        }

                        val relevantBorders = plantBorders[coordsMovement(plant)]
                        relevantBorders == null || coordsMovement(curr) !in relevantBorders
                    }
                }

                perimeter * area
            }
        }
    }

    val input = readInput("Day12")
    part1(input).println()
    part2(input).println()
}
