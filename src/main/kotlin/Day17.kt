import java.lang.Exception

class Day17(filePath: String) : DaySolver(filePath) {

    val A = 0
    val B = 1
    val C = 2

    override fun solvePartOne(input: List<String>): String {
        val registries = getRegisters(input)
        val instructions = getInstructions(input)

        return computeProgram(registries, instructions)
    }

    override fun solvePartTwo(input: List<String>): String {
        val instructions = getInstructions(input)
        val output = instructions.joinToString( ",")
        
        return getInputFromOutput(instructions, output).toString()
    }

    fun getRegisters(input: List<String>): MutableList<Long> {
        val registries = mutableListOf<Long>()

        for (line in input) {
            if (line.isEmpty()) {
                break
            }

            val numberStr = line.split(": ")[1]
            registries.add(numberStr.toLong())
        }

        return registries
    }

    fun getInstructions(input: List<String>): List<Int> {
        for (line in input) {
            if (line.contains("Program")) {
                val operationsStr = line.split(": ")[1]
                return convertStringToNumberList(operationsStr, ",").map { it.toInt() }
            }
        }

        return emptyList()
    }

    fun computeProgram(registries: MutableList<Long>, instructions: List<Int>): String {
        var instructionPointer = 0
        var output = ""

        while (instructionPointer < instructions.size) {
            val instruction = instructions[instructionPointer]
            when (instruction) {
                0 -> {
                    instructionPointer++
                    val operand = getComboOperand(instructions[instructionPointer], registries).toInt()
                    registries[A] = registries[A] / (2.shl(operand - 1)).coerceAtLeast(1)
                    instructionPointer++
                }
                1 -> {
                    instructionPointer++
                    registries[B] = registries[B] xor instructions[instructionPointer].toLong()
                    instructionPointer++
                }
                2 -> {
                    instructionPointer++
                    val operand = getComboOperand(instructions[instructionPointer], registries)
                    registries[B] = operand % 8
                    instructionPointer++
                }
                3 -> {
                    if (registries[A] == 0L) {
                        instructionPointer += 2
                    } else {
                        instructionPointer++
                        instructionPointer = instructions[instructionPointer]
                    }
                }
                4 -> {
                    registries[B] = registries[B] xor registries[C]
                    instructionPointer += 2
                }
                5 -> {
                    instructionPointer++
                    val operand = getComboOperand(instructions[instructionPointer], registries)
                    output = output + (operand % 8).toString() + " "
                    instructionPointer++
                }
                6 -> {
                    instructionPointer++
                    val operand = getComboOperand(instructions[instructionPointer], registries).toInt()
                    registries[B] = registries[A] / (2.shl(operand - 1)).coerceAtLeast(1)
                    instructionPointer++
                }
                7 -> {
                    instructionPointer++
                    val operand = getComboOperand(instructions[instructionPointer], registries).toInt()
                    registries[C] = registries[A] / (2.shl(operand - 1)).coerceAtLeast(1)
                    instructionPointer++
                }
            }
        }

        return output.trim().replace(' ', ',')
    }

    fun getComboOperand(opCode: Int, registries: MutableList<Long>): Long {
        return when (opCode) {
            in 0..3 -> {
                opCode.toLong()
            }
            4 -> {
                registries[A]
            }
            5 -> {
                registries[B]
            }
            6 -> {
                registries[C]
            }
            else -> -1
        }
    }

    /***
     * Searches for inputs that match the output
     * Let's say my output is 2,4,1,1,7,5,1,5,4,3,0,3,5,5,3,0
     * For example, I start to look for inputs that have the output 0
     * I found out that 4 returns 0
     * After that I multiply each found input by 8, because f(x) = f(x * 8 + 0..7).removeFirstChar()
     * Basically if I am looking for a suffix, and I found it at an index x, I will also find inputs which have that
     * suffix at indexes 8*x, 8*x + 1, ... 8 * x + 7
     * So after finding any x for which its output has the searched suffix, I will extend the suffix by one character
     * and look at each X which match the new suffix
     * In this case I will look in [32, 39] for the inputs which have 3,0 as their output
     * Ths process is repeated until the suffix is equal to the output, at this point I will have all x which have that output
     * The reason this search is efficient is because instead of bruteforcing we try to filter our search space by suffix matches
     */
    fun getInputFromOutput(instructions: List<Int>, output: String): Long {
        val reversedOuput = output.filter { it != ',' }.reversed()
        var searchIndexes = mutableSetOf(0L)
        val registries = mutableListOf(0L, 0L, 0L)
        
        
        for (i in 1 .. reversedOuput.length) {
            val suffix = reversedOuput.slice(0 until i).reversed()
            val newSearchIndexes = mutableSetOf<Long>()

            for (searchIndex in searchIndexes) {
                val start = searchIndex * 8

                for (k in 0..7) {
                    val testIndex = start + k
                    registries[A] = testIndex
                    val testOutput = computeProgram(registries, instructions).filter { it != ',' }

                    if (testOutput == suffix) {
                        newSearchIndexes.add(testIndex)
                    }
                }
            }

            searchIndexes = newSearchIndexes
        }

        if (searchIndexes.isEmpty()) {
            throw Exception("Invalid program, no input that matches the ouput was found")
        }

        return searchIndexes.min()
    }
}

fun main() {
    val path = "src/main/inputs/day17.in"
    val day = Day17(path)
    day.printSolution()
}
