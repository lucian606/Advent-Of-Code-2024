class Day3(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        val operations = extractMulOperations(input)
        operations.forEach{extractNumbersFromOperation(it)}
        return operations.fold(0L) { acc, op ->
            acc + extractNumbersFromOperation(op).reduce { a, l -> a * l  }
        }.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val operations = extractAllOperations(input)
        var mulEnabled = true
        var result = 0L
        for (operation in operations) {
            when (operation) {
                "do()" -> mulEnabled = true
                "don't()" -> mulEnabled = false
                else -> {
                    if (!mulEnabled)
                        continue
                    result += extractNumbersFromOperation(operation).reduce { acc, num -> acc * num}
                }
            }
        }
        return result.toString()
    }

    fun extractMulOperations(input: List<String>): List<String> {
        val mulOperationPattern = "mul\\([0-9]{1,3},[0-9]{1,3}\\)"
        return extractOperations(input, mulOperationPattern)
    }

    fun extractAllOperations(input: List<String>): List<String> {
        val operationPattern = "mul\\([0-9]{1,3},[0-9]{1,3}\\)|do\\(\\)|don't\\(\\)"
        return extractOperations(input, operationPattern)
    }

    fun extractOperations(input: List<String>, operationPattern: String): List<String> {
        val regex = Regex(operationPattern)
        val operations = mutableListOf<String>()
        for (line in input) {
            val matches = regex.findAll(line)
            matches.forEach { operations.add(it.value) }
        }
        return operations
    }

    fun extractNumbersFromOperation(operation: String): List<Long> {
        val numberPattern = "[0-9]{1,3}"
        val regex = Regex(numberPattern)
        val matches = regex.findAll(operation)
        val numbers = mutableListOf<Long>()
        matches.forEach { numbers.add(it.value.toLong()) }
        return numbers
    }
}

fun main() {
    val path = "src/main/inputs/day3.in"
    val day = Day3(path)
    day.printSolution()
}