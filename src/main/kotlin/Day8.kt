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

    fun getAntennas(input: List<String>): Map<Char, List<Cell>> {
        val antennas = mutableMapOf<Char, MutableList<Cell>>()

        for (row in input.indices) {
            for (col in input[row].indices) {
                if (input[row][col] != '.') {
                    val antenna = input[row][col]
                    if (antenna in antennas) {
                        antennas[antenna]?.add(Cell(row, col))
                    } else {
                        antennas[antenna] = mutableListOf(Cell(row, col))
                    }
                }
            }
        }

        return antennas
    }

    fun getAntinodes(input: List<String>, antennas: Map<Char, List<Cell>>, harmonicSignal: Boolean = false): Set<Cell> {
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

    fun Cell.getDistance(other: Cell): Cell {
        return Cell(other.first - this.first, other.second - this.second)
    }
}

fun main() {
    val path = "src/main/inputs/day8.in"
    val day = Day8(path)
    day.printSolution()
}
