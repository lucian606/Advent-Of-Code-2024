class Day4(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        var words = 0

        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == 'X') {
                    words += countWords(input, i, j)
                }
            }
        }

        return words.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        var words = 0

        for (i in input.indices) {
            for (j in input[i].indices) {
                if (input[i][j] == 'A' && isXmas(input, i, j)) {
                    words++
                }
            }
        }

        return words.toString()
    }

    fun getCharAt(grid: List<String>, row: Int, col: Int): Char {
        return if (isOutOfBounds(row, col, grid.size, grid[0].length)) '-' else grid[row][col]
    }

    fun isOutOfBounds(row: Int, col: Int, maxRow: Int, maxCol: Int): Boolean {
        return row < 0 || row >= maxRow || col < 0 || col >= maxCol
    }

    fun countWords(grid: List<String>, row: Int, col: Int): Int {
        var words = 0
        var word = ""

        for (i in row..row + 3) {
            word += getCharAt(grid, i, col)
        }

        if (word == "XMAS") {
            words++
        }

        word = ""
        for (i in row downTo row - 3) {
            word += getCharAt(grid, i, col)
        }
        if (word == "XMAS") {
            words++
        }

        word = ""
        for (j in col .. col + 3) {
            word += getCharAt(grid, row, j)
        }
        if (word == "XMAS") {
            words++
        }

        word = ""
        for (j in col downTo col - 3) {
            word += getCharAt(grid, row, j)
        }
        if (word == "XMAS") {
            words++
        }

        word = ""
        for (i in 0..3) {
            word += getCharAt(grid, row - i, col - i)
        }
        if (word == "XMAS") {
            words++
        }

        word = ""
        for (i in 0..3) {
            word += getCharAt(grid, row + i, col + i)
        }
        if (word == "XMAS") {
            words++
        }

        word = ""
        for (i in 0..3) {
            word += getCharAt(grid, row + i, col - i)
        }
        if (word == "XMAS") {
            words++
        }

        word = ""
        for (i in 0..3) {
            word += getCharAt(grid, row - i, col + i)
        }
        if (word == "XMAS") {
            words++
        }

        return words
    }

    fun isXmas(grid: List<String>, row: Int, col: Int): Boolean {
        if (getCharAt(grid, row, col) != 'A')
            return false

        var words = 0
        if ("" + getCharAt(grid, row - 1, col - 1) + getCharAt(grid, row, col) + getCharAt(grid, row + 1, col + 1) == "MAS") {
            words++
        }

        if ("" + getCharAt(grid, row + 1, col - 1) + getCharAt(grid, row, col) + getCharAt(grid, row - 1, col + 1) == "MAS") {
            words++
        }

        if ("" + getCharAt(grid, row - 1, col + 1) + getCharAt(grid, row, col) + getCharAt(grid, row + 1, col - 1) == "MAS") {
            words++
        }

        if ("" + getCharAt(grid, row + 1, col + 1) + getCharAt(grid, row, col) + getCharAt(grid, row - 1, col - 1) == "MAS") {
            words++
        }

        return if (words >= 2) true else false
    }
}

fun main() {
    val path = "src/main/inputs/day4.in"
    val day = Day4(path)
    day.printSolution()
}
