import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

data class Coords(val x: Int, val y:Int) {
    fun distance(coords: Coords) = Coords(this.x - coords.x, this.y - coords.y)
    
    fun adjacentCoords() = setOf(Coords(this.x - 1, this.y), Coords(this.x + 1, this.y),
        Coords(this.x, this.y - 1), Coords(this.x, this.y + 1))
}

data class LongCoords(val x: Long, val y:Long) {
    fun distance(coords: LongCoords) = LongCoords(this.x - coords.x, this.y - coords.y)

    fun adjacentCoords() = setOf(LongCoords(this.x - 1, this.y), LongCoords(this.x + 1, this.y),
        LongCoords(this.x, this.y - 1), LongCoords(this.x, this.y + 1))
}

fun outOfBounds(input: List<String>, coords: Coords): Boolean =
    coords.x < 0 || coords.x >= input.size || coords.y < 0 || coords.y >= input[coords.x].length

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("data/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
