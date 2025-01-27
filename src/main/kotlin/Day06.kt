class Day6(filePath: String) : DaySolver(filePath) {

    data class Position(val cell: Cell, val directionIndex: Int)

    override fun solvePartOne(input: List<String>): String {
        return getGuardPattern(input).map { it.cell }.toSet().size.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val start = getStart(input)
        val guardPattern = getGuardPattern(input).filterNot { it.cell == start }

        return guardPattern.filter { canObstacleCreateLoop(input, it.cell) }.map { it.cell }.toSet().size.toString()
    }

    fun getStart(input: List<String>): Cell {
        val startRowString = input.first{ it.contains('^')}
        val row = input.indexOf(startRowString)
        val col = startRowString.indexOf('^')

        return Cell(row, col)
    }

    fun getGuardPattern(input: List<String>): Set<Position> {
        val start = getStart(input)
        var currentPosition = Position(start, 0)
        val visited = linkedSetOf(currentPosition)

        var finishedPattern = false
        while (!finishedPattern) {
            val currentCell = currentPosition.cell
            val currentDirection = currentPosition.directionIndex

            val nextCell = currentCell.add(directions[currentDirection])
            val nextRow = nextCell.row
            val nextCol = nextCell.col

            if (isOutOfBounds(nextRow, nextCol, input.size, input[0].length)) {
                finishedPattern = true
            } else {
                currentPosition = if (input[nextRow][nextCol] == '#') {
                    Position(currentCell, (currentDirection + 1) % 4)
                } else {
                    Position(nextCell, currentDirection)
                }

                visited.add(currentPosition)
            }
        }

        return visited
    }

    fun isOutOfBounds(row: Int, col: Int, maxRow: Int, maxCol: Int): Boolean {
        return row < 0 || row >= maxRow || col < 0 || col >= maxCol
    }

    fun canObstacleCreateLoop(input: List<String>, obstacle: Cell): Boolean {
        val inputWithObstacle = input.mapIndexed { index, row ->
            if (index == obstacle.row) {
                row.replaceCharAt(obstacle.col, '#')
            } else {
                row
            }
        }

        return isGuardStuck(inputWithObstacle)
    }

    fun String.replaceCharAt(index: Int, newChar: Char): String {
        return this.slice(0 until index) + newChar + this.slice(index + 1 until this.length)
    }

    fun isGuardStuck(input: List<String>): Boolean {
        val start = getStart(input)
        var currentPosition = Position(start, 0)
        val visited = linkedSetOf(currentPosition)

        var finishedPattern = false
        while (!finishedPattern) {
            val currentCell = currentPosition.cell
            val currentDirection = currentPosition.directionIndex

            val nextCell = currentCell.add(directions[currentDirection])
            val nextRow = nextCell.row
            val nextCol = nextCell.col

            if (isOutOfBounds(nextRow, nextCol, input.size, input[0].length)) {
                finishedPattern = true
            } else {
                currentPosition = if (input[nextRow][nextCol] == '#') {
                    Position(currentCell, (currentDirection + 1) % 4)
                } else {
                    Position(nextCell, currentDirection)
                }

                if (currentPosition in visited) {
                    return true
                }

                visited.add(currentPosition)
            }
        }

        return false
    }
}

fun main() {
    val path = "src/main/inputs/day6.in"
    val day = Day6(path)
    day.printSolution()
}
