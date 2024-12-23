fun main() {
    fun getLan(input: List<String>): Map<String, Set<String>> {
        val lan = mutableMapOf<String, MutableSet<String>>()
        
        for (line in input) {
            val splitLine = line.split('-')
            val computer1 = splitLine.first()
            val computer2 = splitLine.last()
            
            val computer1Connections = lan[computer1]
            
            if (computer1Connections == null) {
                lan[computer1] = mutableSetOf(computer2)
            } else {
                computer1Connections.add(computer2)
            }

            val computer2Connections = lan[computer2]

            if (computer2Connections == null) {
                lan[computer2] = mutableSetOf(computer1)
            } else {
                computer2Connections.add(computer1)
            }
        }
        
        return lan
    }
    
    fun getTwos(connections: List<String>): List<List<String>> {
        val result = mutableListOf<List<String>>()
        
        for (i in connections.indices) {
            for (j in (i+1 until connections.size)) {
                result.add(listOf(connections[i], connections[j]))
            }
        }
        
        return result
    }

    fun part1(input: List<String>): Int {
        val lan = getLan(input)
        val found = mutableMapOf<Triple<String, String, String>, Boolean>()
        
        for (connection in lan) {
            val threes = getTwos(connection.value.toList()).map { it + connection.key }
            
            for (three in threes) {
                if (!three.any { it.startsWith('t') }) continue
                
                val sortedThree = three.sorted()
                val triple = Triple(sortedThree[0], sortedThree[1], sortedThree[2])
                
                if (triple in found) continue
                
                val connection2 = lan[three[0]]!!
                val connection3 = lan[three[1]]!!
                
                if (!connection2.contains(three[1]) || !connection2.contains(three[2])) {
                    found[triple] = false
                    continue
                }

                if (!connection3.contains(three[0]) || !connection3.contains(three[2])) {
                    found[triple] = false
                    continue
                }

                found[triple] = true
            }
        }
        
        return found.count { it.value }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    val input = readInput("Day23")
    part1(input).println()
    part2(input).println()
}
