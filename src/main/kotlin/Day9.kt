import java.util.LinkedList

class Day9(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        val disk = input[0]
        val expandedDisk = getExpandedDiskMap(disk)
        val optimisedDisk = optimiseDiskSpace(expandedDisk)

        return getCheckSum(optimisedDisk).toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val disk = input[0]
        val expandedDisk = getExpandedDiskMap(disk)
        val optimisedDisk = moveWholeFiles(expandedDisk)

        return getCheckSum(optimisedDisk).toString()
    }

    fun getExpandedDiskMap(disk: String): List<Int> {
        val result = mutableListOf<Int>()
        var id = 0
        for (i in disk.indices step 2) {
            val blocks = disk[i].digitToInt()
            val freeSpace = if (i + 1 < disk.length) disk[i + 1].digitToInt() else 0
            for (j in 1..blocks) {
                result.add(id)
            }
            for (j in 1..freeSpace) {
                result.add(-1)
            }
            id++
        }
        return result
    }

    fun optimiseDiskSpace(expandedDiskMap: List<Int>): List<Int> {
        val queue = LinkedList<Int>()
        for (i in expandedDiskMap.indices.reversed()) {
            if (expandedDiskMap[i] != -1) {
                queue.add(expandedDiskMap[i])
            }
        }

        val totalBlocks = queue.size
        val result = mutableListOf<Int>()
        for (i in expandedDiskMap.indices) {
            if (i >= totalBlocks) {
                break
            } else {
                if (expandedDiskMap[i] != -1) {
                    result += expandedDiskMap[i]
                } else {
                    result += queue.pop()
                }
            }
        }

        return result
    }

    fun moveWholeFiles(expandedDiskMap: List<Int>): List<Int> {
        val frequencies = expandedDiskMap.getFrequencies()
        val optimisedList = expandedDiskMap.toMutableList()
        val maxId = expandedDiskMap.max()

        for (id in maxId downTo 0) {
            val maxIndex = expandedDiskMap.indexOf(id)
            var findSpace = true
            var i = 0
            while (findSpace) {
                if (i >= maxIndex) {
                    break
                }
                val num = optimisedList[i]
                if (num == -1) {
                    var freeSpace = 0

                    for (j in i..expandedDiskMap.size) {
                        if (optimisedList[j] == -1) {
                            freeSpace++
                        } else {
                            break
                        }
                    }

                    if (freeSpace >= frequencies[id]!!) {
                        for (j in i until i + frequencies[id]!!) {
                            optimisedList[j] = id
                        }
                        for (j in maxIndex until maxIndex + frequencies[id]!!) {
                            optimisedList[j] = -1
                        }
                        findSpace = false
                    } else {
                        i++
                    }
                } else {
                    i++
                }
            }
        }

        return optimisedList
    }

    fun getCheckSum(optimisedDisk: List<Int>): Long {
        var checkSum = 0L
        for (i in optimisedDisk.indices) {
            if (optimisedDisk[i] != -1) {
                checkSum += optimisedDisk[i] * i
            }
        }

        return checkSum
    }

    fun List<Int>.getFrequencies(): Map<Int, Int> {
        val frequenciesMap = mutableMapOf<Int, Int>()

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
    val path = "src/main/inputs/day9.in"
    val day = Day9(path)
    day.printSolution()
}
