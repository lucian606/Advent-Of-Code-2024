fun Cell.add(other: Cell): Cell {
    return Cell(this.first + other.first, this.second + other.second)
}

class Robot(var position: Cell,
            val velocity: Cell,
            val gridWidth: Int,
            val gridHeight: Int) {
    fun move() {
        var (newX, newY) = position.add(velocity)
        if (newX < 0) {
            newX = gridWidth + newX
        } else if (newX >= gridWidth) {
            newX = newX - gridWidth
        }

        if (newY < 0) {
            newY = gridHeight + newY
        } else if (newY >= gridHeight) {
            newY = newY - gridHeight
        }

        position = Cell(newX, newY)
    }

    @Override
    override fun toString(): String {
        return "p=$position, v=$velocity"
    }
}


class Day14(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        val gridWidth = 101
        val gridHeight = 103
        val robots = getRobots(input, gridWidth, gridHeight)
        for (second in 1..100) {
            robots.forEach { it.move() }
        }

        return getSafetyFactors(robots, gridWidth, gridHeight).toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val gridWidth = 101
        val gridHeight = 103
        val robots = getRobots(input, gridWidth, gridHeight)
        var step = 1
        while (true) {
            robots.forEach { it.move() }
            if (areRobotsLinedUpForTree(robots, gridWidth, gridHeight)) {
                println(step)
                printGrid(robots, gridWidth, gridHeight)
                break
            }
            step++
        }

        return step.toString()
    }

    fun getRobots(input: List<String>, gridWidth: Int, gridHeight: Int): List<Robot> {
        val robots = mutableListOf<Robot>()

        for (line in input) {
            val robotDetails = line.split(" ")
            val positionStr = robotDetails[0].split("=")[1]
            val velocityStr = robotDetails[1].split("=")[1]
            val position = convertStringToNumberList(positionStr,",").map{ it.toInt() }.zipWithNext()[0]
            val velocity = convertStringToNumberList(velocityStr,",").map{ it.toInt() }.zipWithNext()[0]

            robots.add(Robot(position, velocity, gridWidth, gridHeight))
        }
        return robots
    }

    fun getSafetyFactors(robots: List<Robot>, gridWidth: Int, gridHeight: Int): Int {
        val midX = gridWidth / 2
        val midY = gridHeight / 2

        val robotsInQuadrants = robots.filterNot { it.position.first == midX || it.position.second == midY }
        val quadrants = mutableListOf(0, 0, 0, 0)

        for (robot in robotsInQuadrants) {
            val position = robot.position
            if (position.first < midX && position.second < midY) {
                quadrants[0]++
            } else if(position.first > midX && position.second < midY) {
                quadrants[1]++
            } else if(position.first < midX && position.second > midY) {
                quadrants[2]++
            } else if(position.first > midX && position.second > midY) {
                quadrants[3]++
            }
        }

        return quadrants.fold(1) { acc, count -> acc * count}
    }

    fun areRobotsLinedUpForTree(robots: List<Robot>, gridWidth: Int, gridHeight: Int): Boolean {
        var rowsLinedUp = 0

        for (row in 0 until gridHeight) {
            var rowString = ""
            val linedUp = "########"
            for (col in 0 until gridWidth) {
                if (robots.any { it.position.first == col && it.position.second == row }) {
                    rowString += "#"
                } else {
                    rowString += "."
                }
            }
            if (rowString.contains(linedUp)) {
                rowsLinedUp++
            }
        }

        if (rowsLinedUp >= 2) {
            return true
        }

        return false
    }

    fun printGrid(robots: List<Robot>, gridWidth: Int, gridHeight: Int) {
        for (row in 0 until gridHeight) {
            for (col in 0 until gridWidth) {
                if (robots.any { it.position.first == col && it.position.second == row }) {
                    print("#")
                } else {
                    print(".")
                }
            }
            println()
        }
    }
}

fun main() {
    val path = "src/main/inputs/day14.in"
    val day = Day14(path)
    day.printSolution()
}
