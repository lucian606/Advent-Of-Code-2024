typealias WarehouseMap = MutableList<MutableList<Char>>

class Day15(filePath: String) : DaySolver(filePath) {

    override fun solvePartOne(input: List<String>): String {
        val warehouseMap = getWarehouseMap(input)
        val movementsList = getMovementsList(input)
        var robotPosition = getRobotPosition(warehouseMap)

        for (movement in movementsList) {
            when (movement) {
                '<' -> {
                    robotPosition = moveRobot(warehouseMap, WEST, robotPosition)
                }
                '>' -> {
                    robotPosition = moveRobot(warehouseMap, EAST, robotPosition)
                }
                '^' -> {
                    robotPosition = moveRobot(warehouseMap, NORTH, robotPosition)
                }
                'v' -> {
                    robotPosition = moveRobot(warehouseMap, SOUTH, robotPosition)
                }
            }
        }

        return getCoordsSum(warehouseMap).toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val warehouseMap = getExpandedWarehouseMap(getWarehouseMap(input))
        val movementsList = getMovementsList(input)
        var robotPosition = getRobotPosition(warehouseMap)

        for (movement in movementsList) {
            when (movement) {
                '<' -> {
                    robotPosition = moveRobotInExpandedWarehouse(warehouseMap, WEST, robotPosition)
                }
                '>' -> {
                    robotPosition = moveRobotInExpandedWarehouse(warehouseMap, EAST, robotPosition)
                }
                '^' -> {
                    robotPosition = moveRobotInExpandedWarehouse(warehouseMap, NORTH, robotPosition)
                }
                'v' -> {
                    robotPosition = moveRobotInExpandedWarehouse(warehouseMap, SOUTH, robotPosition)
                }
            }
        }

        return getCoordsSum(warehouseMap, '[').toString()
    }

    fun getWarehouseMap(input: List<String>): WarehouseMap {
        val warehouseMap: WarehouseMap = mutableListOf()
        for (line in input) {
            if (line.isEmpty()) {
                break
            }
            warehouseMap.add(line.toList().toMutableList())
        }

        return warehouseMap
    }

    fun getMovementsList(input: List<String>): String {
        val startIndex = input.indexOfFirst { it.isEmpty() } + 1
        val movementLines = input.slice(startIndex until input.size)
        return movementLines.fold("") { acc, str -> acc + str}
    }

    fun moveBox(warehouseMap: WarehouseMap, direction: Int, postion: Cell): Boolean {
        val (newRow, newCol) = postion.add(directions[direction])

        if (warehouseMap[newRow][newCol] == '.') {
            warehouseMap[newRow][newCol] = 'O'
            warehouseMap[postion.row][postion.col] = '.'
            return true
        } else if (warehouseMap[newRow][newCol] == '#') {
            return false
        } else if (warehouseMap[newRow][newCol] == 'O') {
            val wasNeighbourBoxMoved = moveBox(warehouseMap, direction, Cell(newRow, newCol))
            return if (wasNeighbourBoxMoved) {
                warehouseMap[newRow][newCol] = 'O'
                warehouseMap[postion.row][postion.col] = '.'
                true
            } else {
                false
            }
        }

        return false
    }

    fun moveRobot(warehouseMap: WarehouseMap, direction: Int, position: Cell): Cell {
        val (newRow, newCol) = position.add(directions[direction])

        if (warehouseMap[newRow][newCol] == '.') {
            warehouseMap[newRow][newCol] = '@'
            warehouseMap[position.row][position.col] = '.'
            return Cell(newRow, newCol)
        } else if (warehouseMap[newRow][newCol] == '#') {
            return position
        } else if (warehouseMap[newRow][newCol] == 'O') {
            val wasNeighbourBoxMoved = moveBox(warehouseMap, direction, Cell(newRow, newCol))
            return if (wasNeighbourBoxMoved) {
                warehouseMap[newRow][newCol] = '@'
                warehouseMap[position.row][position.col] = '.'
                Cell(newRow, newCol)
            } else {
                position
            }
        }

        return position
    }

    fun getRobotPosition(warehouseMap: WarehouseMap): Cell {
        val row = warehouseMap.indexOfFirst { it.contains('@') }
        val col = warehouseMap[row].indexOf('@')

        return Cell(row, col)
    }

    fun getCoordsSum(warehouseMap: WarehouseMap, boxChar: Char = 'O'): Long {
        var sum = 0L

        for (row in warehouseMap.indices) {
            for (col in warehouseMap[row].indices) {
                if (warehouseMap[row][col] == boxChar) {
                    sum += (row * 100) + col
                }
            }
        }

        return sum
    }

    fun getExpandedWarehouseMap(warehouseMap: WarehouseMap): WarehouseMap {
        val expandedWarehouseMap: WarehouseMap = mutableListOf()

        for (line in warehouseMap) {
            val expandedLine = line.map {
                when(it) {
                    '#' -> "##"
                    'O' -> "[]"
                    '@' -> "@."
                    else -> ".."
                }
            }

            expandedWarehouseMap.add(expandedLine.joinToString("").toMutableList())
        }

        return expandedWarehouseMap
    }

    fun moveBigBox(warehouseMap: WarehouseMap, direction: Int, postion: Cell): Boolean {
        if (!isBigBoxMoveable(warehouseMap, direction, postion)) {
            return false
        }

        val (newRow, newCol) = postion.add(directions[direction])
        val (oldRow, oldCol) = postion

        if (direction % 2 == 0) {
            if (warehouseMap[newRow][newCol] == '.' && warehouseMap[newRow][newCol + 1] == '.') {
                warehouseMap[newRow][newCol] = '['
                warehouseMap[newRow][newCol + 1] = ']'
                warehouseMap[oldRow][oldCol] = '.'
                warehouseMap[oldRow][oldCol + 1] = '.'
                return true
            } else {
                if (warehouseMap[newRow][newCol] == '[') {
                    moveBigBox(warehouseMap, direction, Cell(newRow, newCol))
                } else if (warehouseMap[newRow][newCol] == ']') {
                    moveBigBox(warehouseMap, direction, Cell(newRow, newCol - 1))
                }

                if (warehouseMap[newRow][newCol + 1] == '[') {
                    moveBigBox(warehouseMap, direction, Cell(newRow, newCol + 1))
                }

                warehouseMap[newRow][newCol] = '['
                warehouseMap[newRow][newCol + 1] = ']'
                warehouseMap[oldRow][oldCol] = '.'
                warehouseMap[oldRow][oldCol + 1] = '.'

                return true
            }
        } else {
            if (direction == WEST) {
                if (warehouseMap[newRow][newCol] == ']') {
                    moveBigBox(warehouseMap, direction, Cell(newRow, newCol - 1))
                }

                warehouseMap[newRow][newCol] = '['
                warehouseMap[newRow][newCol + 1] = ']'
                warehouseMap[oldRow][oldCol + 1] = '.'
                return true
            } else {
                if (warehouseMap[newRow][newCol + 1] == '[') {
                    moveBigBox(warehouseMap, direction, Cell(newRow, newCol + 1))
                }

                warehouseMap[newRow][newCol] = '['
                warehouseMap[newRow][newCol + 1] = ']'
                warehouseMap[oldRow][oldCol] = '.'
                return true
            }
        }
    }

    fun isBigBoxMoveable(warehouseMap: WarehouseMap, direction: Int, postion: Cell): Boolean {
        val (newRow, newCol) = postion.add(directions[direction])

        if (direction % 2 == 0) {
            if (warehouseMap[newRow][newCol] == '.' && warehouseMap[newRow][newCol + 1] == '.') {
                return true
            } else if (warehouseMap[newRow][newCol] == '#' || warehouseMap[newRow][newCol + 1] == '#') {
                return false
            } else {
                if (warehouseMap[newRow][newCol] == '[') {
                   if(!isBigBoxMoveable(warehouseMap, direction, Cell(newRow, newCol))) {
                       return false
                   }
                } else if (warehouseMap[newRow][newCol] == ']') {
                    if(!isBigBoxMoveable(warehouseMap, direction, Cell(newRow, newCol - 1))) {
                        return false
                    }
                }

                if (warehouseMap[newRow][newCol + 1] == '[') {
                    if(!isBigBoxMoveable(warehouseMap, direction, Cell(newRow, newCol + 1))) {
                        return false
                    }
                }
                return true
            }
        } else {
            if (direction == WEST) {
                if (warehouseMap[newRow][newCol] == '.') {
                    return true
                } else if (warehouseMap[newRow][newCol] == '#') {
                    return false
                } else if (warehouseMap[newRow][newCol] == ']') {
                    if(!isBigBoxMoveable(warehouseMap, direction, Cell(newRow, newCol - 1))) {
                        return false
                    }
                }

                return true
            } else {
                if (warehouseMap[newRow][newCol + 1] == '.') {
                    return true
                } else if (warehouseMap[newRow][newCol + 1] == '#') {
                    return false
                } else if (warehouseMap[newRow][newCol + 1] == '[') {
                    if(!isBigBoxMoveable(warehouseMap, direction, Cell(newRow, newCol + 1))) {
                        return false
                    }
                }

                return true
            }
        }
    }

    fun moveRobotInExpandedWarehouse(warehouseMap: WarehouseMap, direction: Int, position: Cell): Cell {
        val (newRow, newCol) = position.add(directions[direction])

        if (warehouseMap[newRow][newCol] == '.') {
            warehouseMap[newRow][newCol] = '@'
            warehouseMap[position.row][position.col] = '.'
            return Cell(newRow, newCol)
        } else if (warehouseMap[newRow][newCol] == '#') {
            return position
        } else if (warehouseMap[newRow][newCol] == '[') {
            val wasNeighbourBoxMoved = moveBigBox(warehouseMap, direction, Cell(newRow, newCol))
            return if (wasNeighbourBoxMoved) {
                warehouseMap[newRow][newCol] = '@'
                warehouseMap[position.row][position.col] = '.'
                Cell(newRow, newCol)
            } else {
                position
            }
        } else if (warehouseMap[newRow][newCol] == ']') {
            val wasNeighbourBoxMoved = moveBigBox(warehouseMap, direction, Cell(newRow, newCol - 1))
            return if (wasNeighbourBoxMoved) {
                warehouseMap[newRow][newCol] = '@'
                warehouseMap[position.row][position.col] = '.'
                Cell(newRow, newCol)
            } else {
                position
            }
        }

        return position
    }
}

fun main() {
    val path = "src/main/inputs/day15.in"
    val day = Day15(path)
    day.printSolution()
}
