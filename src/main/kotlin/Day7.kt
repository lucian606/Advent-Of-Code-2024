class Day7(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        val opMap = mutableMapOf<Int, List<String>>()

        return input.filter { isEquationSolveable(getTestValue(it), getNumbers(it), opMap, "+*") }.fold(0L) {
            acc, line -> acc + getTestValue(line)
        }.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val opMap = mutableMapOf<Int, List<String>>()

        return input.filter { isEquationSolveable(getTestValue(it), getNumbers(it), opMap, "+*|") }.fold(0L) {
                acc, line -> acc + getTestValue(line)
        }.toString()
    }

    fun getTestValue(line: String): Long {
        return line.split(':')[0].toLong()
    }

    fun getNumbers(line:String): List<Long> {
        return convertStringToNumberList(line.split(": ")[1])
    }

    fun generateAllOperations(operationsCount: Int, possibleOperations: String): List<String> {
        val generatedOperations = mutableListOf<String>()
        generateOperations(operationsCount, generatedOperations, "", possibleOperations)
        return generatedOperations
    }

    fun generateOperations(operationsCount: Int, generatedOperations: MutableList<String>, currentOp: String, possibleOperations: String) {
        if (operationsCount == 0) {
            generatedOperations.add(currentOp)
        } else {
            for (op in possibleOperations) {
                generateOperations(operationsCount - 1,generatedOperations, currentOp + op, possibleOperations)
            }
        }
    }

    fun isEquationSolveable(value: Long, numbers: List<Long>, operationMap: MutableMap<Int, List<String>>, possibleOperations: String): Boolean {
        val operationsCount = numbers.size - 1
        val operationCombinations: List<String>

        if (operationMap.contains(operationsCount)) {
            operationCombinations = operationMap[operationsCount]!!
        } else {
            operationCombinations = generateAllOperations(operationsCount, possibleOperations)
            operationMap[operationsCount] = operationCombinations
        }

        for (operations in operationCombinations) {
            val solution = solveExpression(numbers, operations)
            if (solution == value) {
                return true
            }
        }

        return false
    }

    fun solveExpression(numbers: List<Long>, operations: String): Long {
        var result = numbers[0]

        for (i in operations.indices) {
            when (operations[i]) {
                '+' -> {
                    result += numbers[i + 1]
                }
                '*' -> {
                    result *= numbers[i + 1]
                }
                '|' -> {
                    result = result.concat(numbers[i + 1])
                }
            }
        }

        return result
    }

    fun Long.concat(other: Long): Long {
        return (this.toString() + other.toString()).toLong()
    }
}

fun main() {
    val path = "src/main/inputs/day7.in"
    val day = Day7(path)
    day.printSolution()
}
