import kotlin.math.abs

class Day1(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        val lists = getLists(input)
        lists.forEach { it.sort() }
        val mergedList = lists[0].zip(lists[1])
        return mergedList.fold(0) { totalDistance: Int, pair ->
            val distance = abs(pair.first - pair.second)
            totalDistance + distance
        }.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val lists = getLists(input)
        return lists[0].fold(0) { similarityScore: Int, number: Int ->
            similarityScore + number * lists[1].count { it == number }
        }.toString()
    }

    fun getLists(input: List<String>): List<MutableList<Int>> {
        val lists = mutableListOf<MutableList<Int>>(mutableListOf(), mutableListOf())
        input.map {
            val numbers = it.split(" ").filter { it.isNotEmpty() }
            for (i in numbers.indices) {
                lists[i].add(numbers[i].toInt())
            }
        }
        return lists
    }
}

fun main() {
    val path = "src/main/inputs/day1.in"
    val day1 = Day1(path)
    day1.printSolution()
}