import java.util.LinkedList

class Day10(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        return getSumOfAllTrailheadScores(input, true).toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        return getSumOfAllTrailheadScores(input, false).toString()
    }

    fun getTrailheadScore(input: List<String>, start: Cell, trackVisited: Boolean): Int {
        val (startRow, startCol) = start
        if (input[startRow][startCol] != '0') {
            return 0
        }
        var score = 0
        val queue = LinkedList<Cell>()
        val visited = mutableSetOf<Cell>()
        queue.add(start)

        while (queue.isNotEmpty()) {
            val (currentRow, currentCol) = queue.pop()
            val currentValue = input[currentRow][currentCol]
            if (trackVisited && Cell(currentRow, currentCol) in visited) {
                continue
            }
            visited.add(Cell(currentRow, currentCol))
            if (currentValue == '9') {
                score++
            } else {
                if (currentRow > 0 && input[currentRow - 1][currentCol] == currentValue + 1) {
                    queue.add(Cell(currentRow - 1, currentCol))
                }
                if (currentRow < input.size - 1 && input[currentRow + 1][currentCol] == currentValue + 1) {
                    queue.add(Cell(currentRow + 1, currentCol))
                }
                if (currentCol > 0 && input[currentRow][currentCol - 1] == currentValue + 1) {
                    queue.add(Cell(currentRow, currentCol - 1))
                }
                if (currentCol < input[0].length - 1 && input[currentRow][currentCol + 1] == currentValue + 1) {
                    queue.add(Cell(currentRow, currentCol + 1))
                }
            }
        }

        return score
    }

    fun getSumOfAllTrailheadScores(input: List<String>, trackVisited: Boolean): Int {
        var score = 0

        for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] == '0') {
                    score += getTrailheadScore(input, Cell(row, col), trackVisited)
                }
            }
        }

        return score
    }
}

fun main() {
    val path = "src/main/inputs/day10.in"
    val day = Day10(path)
    day.printSolution()
}
