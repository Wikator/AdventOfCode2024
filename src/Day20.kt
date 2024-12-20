import kotlin.math.abs

fun main() {
    
    fun getRacetrack(input: List<String>): List<List<Int?>> {
        fun findStartCoords(): Coords {
            for (i in input.indices) {
                for (j in (0 until input[i].length)) {
                    if (input[i][j] == 'S') return Coords(i, j)
                }
            }
            
            throw IllegalArgumentException("No S found")
        }
        
        tailrec fun moveNext(coords: Coords, stepCount: Int, raceTrack: List<MutableList<Int?>>): List<List<Int?>> {
            if (input[coords.x][coords.y] == 'E') return raceTrack

            for (nextCoords in coords.adjacentCoords()) {
                if (raceTrack[nextCoords.x][nextCoords.y] == null && input[nextCoords.x][nextCoords.y] != '#') {
                    raceTrack[nextCoords.x][nextCoords.y] = stepCount
                    return moveNext(nextCoords, stepCount + 1, raceTrack)
                }
            }
            
            throw Error("Dead end")
        }
        
        val racetrack = List(input.size) { MutableList<Int?>(input.first().length) { null } }
        val startCoords = findStartCoords()
        racetrack[startCoords.x][startCoords.y] = 0
        return moveNext(startCoords, 1, racetrack)
    }

    fun outOfBounds(input: List<List<Int?>>, coords: Coords): Boolean =
        coords.x < 0 || coords.x >= input.size || coords.y < 0 || coords.y >= input[coords.x].size
    
    fun getPossibleCheatsCount(racetrack: List<List<Int?>>, getPossibleCheatEnds: (Coords) -> List<Coords>): Int =
        racetrack.foldIndexed(0) { x, rowAcc, row ->
            rowAcc + row.foldIndexed(0) { y, stepAcc, step ->
                stepAcc + if (step == null) {
                    0
                } else {
                    val coords = Coords(x, y)
                    val adjacentCoords = getPossibleCheatEnds(coords)
                    adjacentCoords.count { new ->
                        if (outOfBounds(racetrack, new)) {
                            false
                        } else {
                            val newStep = racetrack[new.x][new.y]
                            newStep != null && newStep - step - (abs(coords.x - new.x) + abs(coords.y - new.y)) >= 100
                        }
                    }
                }
            }
        }
    
    fun part1(input: List<String>): Int {
        val racetrack = getRacetrack(input)

        fun Coords.adjacentCoords(distance: Int) = listOf(Coords(this.x - distance, this.y), Coords(this.x + distance, this.y),
            Coords(this.x, this.y - distance), Coords(this.x, this.y + distance))
        
        return getPossibleCheatsCount(racetrack) { coords -> coords.adjacentCoords(2) }
    }

    fun part2(input: List<String>): Int {
        val racetrack = getRacetrack(input)

        fun Coords.coordsInRange(range: Int): List<Coords> =
            (this.x - range .. this.x + range).flatMap { i ->
                val length = range - abs(this.x - i)
                (this.y - length .. this.y + length).map { j -> Coords(i, j) }
            }

        return getPossibleCheatsCount(racetrack) { coords -> coords.coordsInRange(20) }
    }

    val input = readInput("Day20")
    part1(input).println()
    part2(input).println()
}
