import java.util.*
import kotlin.math.min

class Day21(filePath: String) : DaySolver(filePath) {

    val numericKeypad = listOf(
        "789",
        "456",
        "123",
        "#0A"
    )

    val directionKeypad = listOf(
        "#^A",
        "<v>"
    )

    val arrowsList = "^>v<"

    private val directionPaths = mapOf(
        Pair('A', '<') to "v<<",
        Pair('A', '>') to "v",
        Pair('A', 'v') to "<v",
        Pair('A', '^') to "<",
        Pair('A', 'A') to "",

        Pair('<', 'A') to ">>^",
        Pair('<', '^') to ">^",
        Pair('<', 'v') to ">",
        Pair('<', '>') to ">>",
        Pair('<', '<') to "",

        Pair('>', 'A') to "^",
        Pair('>', '^') to "<^",
        Pair('>', 'v') to "<",
        Pair('>', '<') to "<<",
        Pair('>', '>') to "",

        Pair('^', 'A') to ">",
        Pair('^', '<') to "v<",
        Pair('^', '>') to "v>",
        Pair('^', 'v') to "v",
        Pair('^', '^') to "",

        Pair('v', 'A') to "^>",
        Pair('v', '>') to ">",
        Pair('v', '<') to "<",
        Pair('v', '^') to "<",
        Pair('v', 'v') to ""
    )

    override fun solvePartOne(input: List<String>): String {
        var sum = 0
        for (code in input) {
            var directionCode = getBestOptimalDirectionsCode(code)
            for (i in 1..2) {
                directionCode = getDirectionsOfDirections(directionCode)
            }
            sum += directionCode.length * code.dropLast(1).toInt()
        }
        return sum.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        var sum = 0L
        val cache = mutableMapOf<String, String>()

        for (code in input) {
            var directionCode = getBestOptimalDirectionsCode(code)
            val pieces = directionCode.split("A").map { it + "A" }.dropLast(1)

            var sequenceCounter = mutableMapOf<String, Long>()
            for (piece in pieces) {
                sequenceCounter[piece] = sequenceCounter.getOrDefault(piece, 0) + 1
            }

            for (i in 1..25) {
                val subsequencesCounter = mutableMapOf<String, Long>()
                for ((sequence, value) in sequenceCounter) {
                    val subsequenceDirections = getDirectionsOfDirections(sequence, cache)
                    val subPieces = subsequenceDirections.split('A').map { it + "A" }.dropLast(1)
                    for (subPiece in subPieces) {
                        subsequencesCounter[subPiece] = subsequencesCounter.getOrDefault(subPiece, 0) + value
                    }
                }
                sequenceCounter = subsequencesCounter
            }

            var subsequenceLength = 0L
            for ((key, value) in sequenceCounter) {
                subsequenceLength += key.length * value
            }


            sum += (code.dropLast(1).toInt()) * subsequenceLength
        }
        return sum.toString()
    }


    fun getTileLocation(grid: List<String>, tile: Char): Cell {
        val row = grid.indexOfFirst { it.contains(tile) }
        val col = grid[row].indexOf(tile)

        return Cell(row, col)
    }

    fun getShortestPaths(input: List<String>, start: Cell, end: Cell): List<String> {
        val queue: Queue<Triple<Cell, String, Int>> = LinkedList()
        val visited = mutableMapOf<Cell, Int>()
        val paths = mutableListOf<String>()

        queue.add(Triple(start, "", 0))
        var shortestDistance = Int.MAX_VALUE

        while (queue.isNotEmpty()) {
            val (cell, path, distance) = queue.poll()

            if (distance > shortestDistance) continue

            if (cell == end) {
                if (distance < shortestDistance) {
                    shortestDistance = distance
                    paths.clear()
                }
                paths.add(path)
                continue
            }

            if (cell in visited && visited[cell]!! < distance) continue
            visited[cell] = distance

            for (direction in directions.indices) {
                val newPath = path + arrowsList[direction]
                val neighbor = cell.add(directions[direction])

                if (!neighbor.isOutOfBounds(input.size, input[0].length) && input[neighbor.row][neighbor.col] != '#') {
                    queue.add(Triple(neighbor, newPath, distance + 1))
                }
            }
        }

        return paths
    }

    fun getDirectionsCodes(keypad: List<String>, code: String): List<String> {
        var current = getTileLocation(keypad, 'A')
        var paths = mutableListOf("")

        for (char in code) {
            val destination = getTileLocation(keypad, char)
            val currentPaths = getShortestPaths(keypad, current, destination)
            val newPaths = mutableListOf<String>()
            for (path in paths) {
                for (shortPath in currentPaths) {
                    newPaths.add(path + shortPath + 'A')
                }
            }
            paths = newPaths
            current = destination
        }

        return paths
    }

    fun getDirectionsOfDirections(directionCode: String, cache: MutableMap<String, String> = mutableMapOf()): String {
        if (directionCode in cache) {
            return cache[directionCode]!!
        }
        var result = ""
        var current = 'A'
        for (char in directionCode) {
            if (char != current)
                result += directionPaths[Pair(current, char)]
            result += 'A'
            current = char
        }

        cache[directionCode] = result
        return result
    }

    fun getBestOptimalDirectionsCode(code: String): String {
        val directionCodes = getDirectionsCodes(numericKeypad, code)
        var optimalCode = directionCodes[0]
        var minimumLength = Int.MAX_VALUE

        for (directionCode in directionCodes) {
            var currentDirections = getDirectionsOfDirections(directionCode)
            currentDirections = getDirectionsOfDirections(currentDirections)
            if (currentDirections.length < minimumLength) {
                minimumLength = currentDirections.length
                optimalCode = directionCode
            }
        }

        return optimalCode
    }
}

fun main() {
    val path = "src/main/inputs/day21.in"
    val day = Day21(path)
    day.printSolution()
}
