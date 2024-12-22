import java.util.*
import kotlin.math.max

class Day22(filePath: String) : DaySolver(filePath) {


    override fun solvePartOne(input: List<String>): String {
        var sum = 0L
        for (line in input) {
            val secret = line.toLong()
            sum += getNumberAfterIterations(secret, 2000)
        }
        return sum.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val fluctuationsList = mutableListOf<Map<List<Long>, Long>>()
        var sequences = mutableListOf<List<Long>>()
        for (line in input) {
            val secret = line.toLong()
            val fluctuations =getPriceFluctuations(secret, 2000)
            fluctuationsList.add(fluctuations)
            sequences.addAll(fluctuations.keys)
        }
        var maximum = 0L
        sequences = sequences.distinct().toMutableList()
        for (sequence in sequences) {
            var sum = 0L
            for (fluctuations in fluctuationsList) {
                sum = sum + fluctuations.getOrDefault(sequence, 0)
            }
            if (sum > maximum) {
                maximum = sum
            }
        }
        return maximum.toString()
    }

    fun mixNumbers(a: Long, b: Long): Long {
        return a xor b
    }

    fun pruneNumber(number: Long): Long {
        return number % 16777216
    }

    fun getNumberAfterIterations(secret: Long, iterations: Int): Long {
        var result = secret
        for (i in 0 until iterations) {
                var factor = result * 64
                result = mixNumbers(result, factor)
                result = pruneNumber(result)

                 factor = result / 32
                result = mixNumbers(result, factor)
                result = pruneNumber(result)
                factor = result * 2048
                result = mixNumbers(result, factor)
                result = pruneNumber(result)
        }

        return result
    }

    fun getPriceFluctuations(secret: Long, iterations: Int): Map<List<Long>, Long> {
        var fluctuationSequence = LinkedList<Long>()
        val fluctuations = mutableMapOf<List<Long>, Long>()
        var result = secret
        for (i in 0 until iterations) {
            val initialPrice = result % 10
            var factor = result * 64
            result = mixNumbers(result, factor)
            result = pruneNumber(result)

            factor = result / 32
            result = mixNumbers(result, factor)
            result = pruneNumber(result)
            factor = result * 2048
            result = mixNumbers(result, factor)
            result = pruneNumber(result)

            val priceFluctuation = (result % 10) - initialPrice
            if (i < 4) {
                fluctuationSequence.add(priceFluctuation)
            } else {
                fluctuationSequence.removeFirst()
                fluctuationSequence.add(priceFluctuation)
            }

            if (i >= 3) {
                val fluctuationSequenceCopy = fluctuationSequence.toMutableList()
                if (fluctuationSequenceCopy !in fluctuations) {
                    fluctuations[fluctuationSequenceCopy] = result % 10
                }
            }

        }

        return fluctuations
    }
}

fun main() {
    val path = "src/main/inputs/day22.in"
    val day = Day22(path)
    day.printSolution()
}
