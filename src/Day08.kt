fun main() {
    
    fun getAntennas(input: List<String>): Map<Char, List<Coords>> {
        val indexedInput = input
            .flatMapIndexed { xIndex, row ->
                row
                    .mapIndexed { yIndex, character -> Pair(Coords(xIndex, yIndex), character)}
                    .filter { it.second != '.' }
            }
        
        return indexedInput.groupBy(
            keySelector = { it.second },
            valueTransform = { it.first }
        )
    }
    
    fun forEachConnectedAntenna(antennas: Collection<List<Coords>>, operation: (Coords, Coords) -> Unit) {
        for (sameFrequencyAntennas in antennas) {
            for (antenna1 in sameFrequencyAntennas) {
                for (antenna2 in sameFrequencyAntennas.filter { it != antenna1 }) {
                    operation(antenna1, antenna2)
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val antennas = getAntennas(input)
        val antiNodes = mutableSetOf<Coords>()
        
        forEachConnectedAntenna(antennas.values) { antenna1, antenna2 ->
            val distance = antenna1.distance(antenna2)
            val antiNode = Coords(antenna2.x + distance.x * 2, antenna2.y + distance.y * 2)

            if (!outOfBounds(input, antiNode)) {
                antiNodes.add(antiNode)
            }
        }
        
        return antiNodes.size
    }

    fun part2(input: List<String>): Int {
        val antennas = getAntennas(input)
        val antiNodes = mutableSetOf<Coords>()

        forEachConnectedAntenna(antennas.values) { antenna1, antenna2 ->
            val distance = antenna1.distance(antenna2)
            var multiplier = 1
            var antiNode = Coords(antenna2.x + distance.x, antenna2.y + distance.y)

            do {
                antiNodes.add(antiNode)
                multiplier++
                antiNode = Coords(antenna2.x + distance.x * multiplier, antenna2.y + distance.y * multiplier)
            } while (!outOfBounds(input, antiNode))
        }
        
        return antiNodes.size
    }

    val input = readInput("Day08")

    part1(input).println()
    part2(input).println()
}
