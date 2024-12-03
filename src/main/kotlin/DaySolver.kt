import java.io.File

abstract class DaySolver(val filePath: String) {
    fun readInput(): List<String> {
        val file = File(filePath)
        return file.readLines()
    }

    abstract fun solvePartOne(input: List<String>) : String
    abstract fun solvePartTwo(input: List<String>) : String

    fun convertStringToNumberList(listStr: String): List<Long> {
        return listStr.split(" ")
            .filter { !it.isEmpty() }
            .map { x -> x.toLong() }
    }

    fun printSolution() {
        println("Part One: ${solvePartOne(readInput())}")
        println("Part Two: ${solvePartTwo(readInput())}")
    }
}