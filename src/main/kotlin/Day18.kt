import java.util.*

class Day18(filePath: String) : DaySolver(filePath) {

    val N = 71

    override fun solvePartOne(input: List<String>): String {
        val fallingBytes = getFallingBytes(input)
        val size = 1024
        return getShortestPathToCorner(fallingBytes.slice(0 until size).toSet()).toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val fallingBytes = getFallingBytes(input)
        return getFirstBlockingByte(fallingBytes, 1024).toString()
    }

    fun getFallingBytes(input: List<String>): List<Cell> {
        val bytes = mutableListOf<Cell>()

        for (line in input) {
            val (row, col) = line.split(',').map { it.toInt() }
            bytes.add(Cell(row, col))
        }
        return bytes
    }

    fun getShortestPathToCorner(fallingBytes: Set<Cell>): Int {
        val visited = mutableSetOf<Cell>()
        val queue: Queue<PathList> = LinkedList()
        queue.add(PathList(Cell(0, 0), listOf(Cell(0, 0))))

        while (queue.isNotEmpty()) {
            val (cell, path) = queue.poll()

            if (cell == Cell(N - 1, N - 1)) {
                return path.size - 1
            }
            if (cell in visited) {
                continue
            }

            visited.add(cell)

            for (direction in directions) {
                val neighbour = cell.add(direction)
                if (neighbour !in visited && neighbour !in fallingBytes && !neighbour.isOutOfBounds(N, N)) {
                    val pathCopy = path.toMutableList()
                    pathCopy.add(neighbour)
                    queue.add(PathList(neighbour, pathCopy))
                }
            }
        }

        return -1
    }

    fun getFirstBlockingByte(fallingBytes: List<Cell>, start: Int = 0): Cell {
        for (i in start until fallingBytes.size) {
            if (getShortestPathToCorner(fallingBytes.slice(0..i).toSet()) == -1) {
                return fallingBytes[i]
            }
        }

        return Cell(-1, -1)
    }
}

fun main() {
    val path = "src/main/inputs/day18.in"
    val day = Day18(path)
    day.printSolution()
}
