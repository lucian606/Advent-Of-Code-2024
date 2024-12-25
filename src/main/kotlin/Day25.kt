class Day25(filePath: String) : DaySolver(filePath) {

    override fun solvePartOne(input: List<String>): String {
        val locks = getLocks(input)
        val keys = getKeys(input)
        var count = 0

        for (key in keys) {
            for (lock in locks) {
                val sum = key.zip(lock).map { it.first + it.second }
                if (sum.all { it <= 5 }) {
                    count++
                }
            }
        }

        return count.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        return "Not this time, merry christmas!"
    }

    fun getLocks(input: List<String>): List<List<Int>> {
        val locks = mutableListOf<List<Int>>()

        for (i in input.indices step 8) {
            if (input[i].all {it == '#'}) {
                val lockSchematic = input.slice(i.. i + 6)
                val lock = mutableListOf<Int>()
                for (col in 0 until lockSchematic[0].length) {
                    for (row in lockSchematic.size - 1 downTo 0) {
                        if (lockSchematic[row][col] == '#') {
                            lock.add(row)
                            break
                        }
                    }
                }
                locks.add(lock)
            }
        }

        return locks
    }

    fun getKeys(input: List<String>): List<List<Int>> {
        val keys = mutableListOf<List<Int>>()

        for (i in input.indices step 8) {
            if (input[i].all {it == '.'}) {
                val keySchematic = input.slice(i.. i + 6)
                val key = mutableListOf<Int>()
                for (col in 0 until keySchematic[0].length) {
                    for (row in 0 .. keySchematic.size - 1) {
                        if (keySchematic[row][col] == '#') {
                            key.add(6 - row)
                            break
                        }
                    }
                }
                keys.add(key)
            }
        }

        return keys
    }
}

fun main() {
    val path = "src/main/inputs/day25.in"
    val day = Day25(path)
    day.printSolution()
}