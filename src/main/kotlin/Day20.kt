import java.util.LinkedList
import java.util.Queue

class Day20(filePath: String) : DaySolver(filePath) {

    override fun solvePartOne(input: List<String>): String {
        return getCheatTimesaves(input, 2).filter { it >= 50 }.filter { it >= 100 }.size.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        return getCheatTimesaves(input, 20).filter { it >= 50 }.filter { it >= 100 }.size.toString()
    }

    fun getTileLocation(input: List<String>, tile: Char): Cell {
        val row = input.indexOfFirst { it.contains(tile) }
        val col = input[row].indexOf(tile)

        return Cell(row, col)
    }

    fun getShortestPath(input: List<String>, start: Cell, end: Cell): List<Cell> {
        val queue: Queue<PathList> = LinkedList()
        val visited = mutableSetOf<Cell>()
        queue.add(Pair(start, mutableListOf(start)))

        while (queue.isNotEmpty()) {
            val (cell, path) = queue.poll()

            if (cell == end) {
                return path
            }

            if (cell in visited) {
                continue
            }

            visited.add(cell)

            for (direction in directions) {
                val pathCopy = path.toMutableList()
                val neighbour = cell.add(direction)
                if (input[neighbour.row][neighbour.col] != '#') {
                    pathCopy.add(Cell(neighbour.row, neighbour.col))
                    queue.add(Pair(Cell(neighbour.row, neighbour.col), pathCopy))
                }
            }
        }

        return emptyList()
    }


    fun getCheatTimesaves(input: List<String>, cheatTime: Int): List<Int> {
        val raceTrack = getShortestPath(input, getTileLocation(input, 'S'), getTileLocation(input, 'E'))
        val timesaves = mutableListOf<Int>()
        val destination = raceTrack.size - 1

        for ((i, cell) in raceTrack.withIndex()) {
            if (i == destination) {
                break
            }

            for (j in i + 1 until raceTrack.size) {
                val distance = cell.getDistance(raceTrack[j])
                if (distance <= cheatTime && distance < j - i) {
                    timesaves.add(j - i - distance)
                }
            }
        }

        return timesaves
    }
}

fun main() {
    val path = "src/main/inputs/day20.in"
    val day = Day20(path)
    day.printSolution()
}
