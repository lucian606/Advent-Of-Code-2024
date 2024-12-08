typealias Point = Pair<Int, Int>

class Day8(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        val antennas = getAntennas(input)
        val antinodes = getAntinodes(input, antennas)
        return antinodes.size.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val antennas = getAntennas(input)
        val antinodes = getAntinodes(input, antennas, true)
        return antinodes.size.toString()
    }

    fun getAntennas(input: List<String>): Map<Char, List<Point>> {
        val antennas = mutableMapOf<Char, MutableList<Point>>()

        for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] != '.') {
                    val antenna = input[row][col]
                    if (antenna in antennas) {
                        antennas[antenna]?.add(Point(row, col))
                    } else {
                        antennas[antenna] = mutableListOf(Point(row, col))
                    }
                }
            }
        }

        return antennas
    }

    fun getAntinodes(input: List<String>, antennas: Map<Char, List<Point>>, harmonicSignal: Boolean = false): Set<Cell> {
        val antinodes = mutableSetOf<Cell>()

        for (antennasList in antennas.values) {
            for (i in antennasList.indices) {
                for (j in antennasList.indices) {
                    if (i != j) {
                        val distance = antennasList[i].getDistance(antennasList[j])
                        var antinode = antennasList[i].add(distance)

                        if (harmonicSignal) {
                            while (!antinode.isOutOfBounds(input.size, input[0].length)) {
                                antinodes.add(antinode)
                                antinode = antinode.add(distance)
                            }
                        } else {
                            antinode = antinode.add(distance)
                            if (!antinode.isOutOfBounds(input.size, input[0].length)) {
                                antinodes.add(antinode)
                            }
                        }
                    }
                }
            }
        }

        return antinodes
    }

    fun Point.isOutOfBounds(maxRow: Int, maxCol: Int): Boolean {
        return this.first < 0 || this.first >= maxRow || this.second < 0 || this.second >= maxCol
    }

    fun Point.getDistance(other: Point): Point {
        return Point(other.first - this.first, other.second - this.second)
    }

    fun Point.add(other: Point): Point {
        return Point(this.first + other.first, this.second + other.second)
    }
}

fun main() {
    val path = "src/main/inputs/day8.in"
    val day = Day8(path)
    day.printSolution()
}
