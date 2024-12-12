import java.util.LinkedList

typealias Region = Set<Cell>
typealias Fence = Pair<Cell, Int>

class Day12(filePath: String) : DaySolver(filePath) {

    val directions = listOf(
        Cell(-1, 0),
        Cell(0, 1),
        Cell(1, 0),
        Cell(0, -1)
    )

    val NORTH = 0
    val EAST = 1
    val SOUTH = 2
    val WEST = 3


    override fun solvePartOne(input: List<String>): String {
        val regions = getRegions(input)
        return regions.fold(0) { acc, it ->
            acc + it.getPerimeter() * it.getArea()
        }.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val regions = getRegions(input)
        return regions.fold(0) { acc, it ->
            acc + it.getCorners() * it.getArea()
        }.toString()
    }

    fun getRegion(input: List<String>, start: Cell): Region {
        val region = mutableSetOf<Cell>()
        val queue = LinkedList<Cell>()
        val regionType = input[start.first][start.second]
        queue.add(start)
        while (queue.isNotEmpty()) {
            val cell = queue.pop()
            if (cell in region) {
                continue
            }
            region.add(cell)
            for (direction in directions) {
                val neighbour = cell.add(direction)
                if (
                    !neighbour.isOutOfBounds(input.size, input[0].length) &&
                    input[neighbour.first][neighbour.second] == regionType
                ) {
                    queue.add(neighbour)
                }
            }
        }
        return region
    }

    fun getRegions(input: List<String>): Set<Region> {
        val regions = mutableSetOf<Region>()
        val visited = mutableSetOf<Cell>()
        for (row in input.indices) {
            for (col in input[0].indices) {
                if (Cell(row, col) in visited) {
                    continue
                }
                val region = getRegion(input, Cell(row, col))
                regions.add(region)
                for (cell in region) {
                    visited.add(cell)
                }
            }
        }

        return regions
    }

    fun Cell.isOutOfBounds(maxRow: Int, maxCol: Int): Boolean {
        return this.first < 0 || this.first >= maxRow || this.second < 0 || this.second >= maxCol
    }

    fun Cell.add(other: Cell): Cell {
        return Cell(this.first + other.first, this.second + other.second)
    }

    fun Region.getArea(): Int {
        return this.size
    }

    fun Region.getPerimeter(): Int {
        var perimeter = 0

        for (cell in this) {
            for (direction in directions) {
                val neighbour = cell.add(direction)
                if (neighbour !in this) {
                    perimeter++
                }
            }
        }

        return perimeter
    }

    fun Region.getFences(): Set<Fence> {
        val fences = mutableSetOf<Fence>()

        for (cell in this) {
            for (i in directions.indices) {
                val neighbour = cell.add(directions[i])
                if (neighbour !in this) {
                    val fence = Fence(cell, i)
                    fences.add(fence)
                }
            }
        }

        return fences
    }

    fun Region.getCorners(): Int {
        val fences = this.getFences()
        val stack = LinkedList<Fence>()
        val visited = mutableSetOf<Fence>()
        var corners = 0

        while (visited.size != fences.size) {
            val remainingFences = fences.minus(visited)
            val startFence = remainingFences.first()
            stack.add(startFence)

            val isFenceVisitable = { f: Fence -> f in fences && (f !in visited || (f == startFence)) }

            while (stack.isNotEmpty()) {
                val fence = stack.removeFirst()
                if (fence in visited) {
                    continue
                }
                visited.add(fence)
                val cell = fence.first
                val fenceDirection = fence.second
                when (fenceDirection) {
                    NORTH -> {
                        // check if we can move right
                        val right = cell.add(directions[EAST])
                        var nextFence = Fence(right, fenceDirection)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            continue
                        }

                        // check if we can move right-down
                        nextFence = Fence(cell, EAST)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            corners++
                            continue
                        }

                        // check if we can move right-up
                        val rightUp = cell.add(Cell(-1, 1))
                        nextFence = Fence(rightUp, WEST)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            corners++
                            continue
                        }
                    }

                    SOUTH -> {
                        // check if we can move left
                        val left = cell.add(directions[WEST])
                        var nextFence = Fence(left, fenceDirection)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            continue
                        }

                        // check if we can move left-down
                        val leftDown = cell.add(Cell(1, -1))
                        nextFence = Fence(leftDown, EAST)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            corners++
                            continue
                        }

                        // check if we can move left-up
                        nextFence = Fence(cell, WEST)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            corners++
                            continue
                        }
                    }

                    EAST -> {
                        // check if we can move down
                        val down = cell.add(directions[SOUTH])
                        var nextFence = Fence(down, fenceDirection)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            continue
                        }

                        // check if we can move down-right
                        val downRight = cell.add(Cell(1, 1))
                        nextFence = Fence(downRight, NORTH)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            corners++
                            continue
                        }

                        // check if we can move down-left
                        nextFence = Fence(cell, SOUTH)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            corners++
                            continue
                        }
                    }

                    WEST -> {
                        // check if we can move up
                        val up = cell.add(directions[NORTH])
                        var nextFence = Fence(up, fenceDirection)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            continue
                        }

                        // check if we can move up-right
                        nextFence = Fence(cell, NORTH)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            corners++
                            continue
                        }

                        // check if we can move up-left
                        val upLeft = cell.add(Cell(-1, -1))
                        nextFence = Fence(upLeft, SOUTH)
                        if (isFenceVisitable(nextFence)) {
                            stack.addFirst(nextFence)
                            corners++
                            continue
                        }
                    }
                }

            }
        }

        return corners
    }

}

fun main() {
    val path = "src/main/inputs/day12.in"
    val day = Day12(path)
    day.printSolution()
}
