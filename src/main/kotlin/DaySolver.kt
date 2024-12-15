import java.io.File

typealias Cell = Pair<Int, Int>

fun Cell.add(other: Cell): Cell {
    return Cell(this.first + other.first, this.second + other.second)
}

fun Cell.isOutOfBounds(maxRow: Int, maxCol: Int): Boolean {
    return this.first < 0 || this.first >= maxRow || this.second < 0 || this.second >= maxCol
}

abstract class DaySolver(val filePath: String) {
    fun readInput(): List<String> {
        val file = File(filePath)
        return file.readLines()
    }

    abstract fun solvePartOne(input: List<String>) : String
    abstract fun solvePartTwo(input: List<String>) : String

    fun convertStringToNumberList(listStr: String, delimiters: String = " "): List<Long> {
        return listStr.split(delimiters)
            .filter { !it.isEmpty() }
            .map { x -> x.toLong() }
    }

    fun printSolution() {
        println("Part One: ${solvePartOne(readInput())}")
        println("Part Two: ${solvePartTwo(readInput())}")
    }
}