class Day11(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        return splitStones(convertStringToNumberList(input[0]), 25).toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        return splitStones(convertStringToNumberList(input[0]), 75).toString()
    }

    fun splitStones(stones: List<Long>, iterations: Int): Long {
        var frequencies = stones.getFrequencies()

        for (step in 1..iterations) {
            val currentFrequencies = mutableMapOf<Long, Long>()
            for ((stone, count) in frequencies) {
                if (stone == 0L) {
                    if (1 in currentFrequencies) {
                        currentFrequencies[1] = currentFrequencies[1]!! + count
                    } else {
                        currentFrequencies[1] = count
                    }
                } else if (stone.getNumberOfDigits() % 2 == 0) {
                    val newStones = stone.splitNumber().toList()
                    for (newStone in newStones) {
                        if (newStone in currentFrequencies) {
                            currentFrequencies[newStone] = currentFrequencies[newStone]!! + count
                        } else {
                            currentFrequencies[newStone] = count
                        }
                    }
                } else {
                    val newStone = stone * 2024
                    if (newStone in currentFrequencies) {
                        currentFrequencies[newStone] = currentFrequencies[1]!! + count
                    } else {
                        currentFrequencies[newStone] = count
                    }
                }
            }

            frequencies = currentFrequencies
        }

        return frequencies.values.sum()
    }


    fun Long.getNumberOfDigits(): Int {
        return this.toString().length
    }

    fun Long.splitNumber(): Pair<Long, Long> {
        val numStr =this.toString()
        val middle = numStr.length / 2
        return Pair(
            numStr.slice(0 until middle).toLong(),
            numStr.slice(middle until numStr.length).toLong()
        )
    }

    fun List<Long>.getFrequencies(): Map<Long, Long> {
        val frequenciesMap = mutableMapOf<Long, Long>()

        for (num in this) {
            if (num in frequenciesMap) {
                frequenciesMap[num] = frequenciesMap[num]!! + 1
            } else {
                frequenciesMap[num] = 1
            }
        }

        return frequenciesMap
    }
}

fun main() {
    val path = "src/main/inputs/day11.in"
    val day = Day11(path)
    day.printSolution()
}
