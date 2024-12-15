import java.lang.Math.*

private typealias Matrix = List<List<Long>>

class Day13(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        var sum = 0L

        for (i in input.indices step 4) {
            val equations =  getEquationMatrix(input.slice(i..i + 2))
            val (a, b) = solveEquation(equations)
            if (abs(a) > 100 || abs(b) > 100) {
                continue
            }
            sum += ((3L * abs(a)) + (1L * abs(b)))
        }

        return sum.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        var sum = 0L

        for (i in input.indices step 4) {
            val equations =  getEquationMatrix(input.slice(i..i + 2))
            val modifiedPrizes = equations[2].map { it + 10000000000000 }
            val modifiedEquation = equations.toMutableList()
            modifiedEquation[2] = modifiedPrizes
            val (a, b) = solveEquation(modifiedEquation)
            sum += ((3L * abs(a)) + (1L * abs(b)))
        }

        return sum.toString()
    }

    fun getEquationMatrix(lines: List<String>): Matrix {
        return lines.fold(mutableListOf()) { acc, it ->
            acc.add(it.extractNumbers())
            acc
        }
    }

    fun solveEquation(mat: Matrix): Pair<Long, Long> {
        val determinant = mat.slice(0..1).getDeterminant()
        if (determinant == 0L) {
            return Pair(0, 0)
        } else {
            val extendedMatrix = listOf(
                mat.map { it[0] },
                mat.map { it[1] }
            )

            val a_matrix = extendedMatrix.map{it.filterIndexed{ index, _ -> index != 0 }.reversed()}
            val b_matrix = extendedMatrix.map{it.filterIndexed{ index, _ -> index != 1 }}

            val a_det = a_matrix.getDeterminant()
            val b_det = b_matrix.getDeterminant()

            if (!isIntegerDivision(a_det, determinant) || !isIntegerDivision(b_det, determinant)) {
                return Pair(0, 0)
            }
            val a = a_det / determinant
            val b = b_det / determinant

            return Pair(a, b)
        }
    }

    fun isIntegerDivision(numerator: Long, denominator: Long): Boolean {
        if (denominator == 0L) {
            throw IllegalArgumentException("Denominator cannot be zero")
        }
        return numerator % denominator == 0L
    }

    fun Matrix.getDeterminant(): Long {
        return (this[0][0] * this[1][1]) - (this[0][1] * this[1][0])
    }

    fun String.extractNumbers(): List<Long> {
        val numberPattern = "[0-9]+"
        val regex = Regex(numberPattern)
        return regex.findAll(this).map { it.value }.map { it.toLong() }.toList()
    }
}

fun main() {
    val path = "src/main/inputs/day13.in"
    val day = Day13(path)
    day.printSolution()
}
