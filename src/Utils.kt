import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

data class Coords(val x: Int, val y:Int) {
    fun distance(coords: Coords) = Coords(this.x - coords.x, this.y - coords.y)
}

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
