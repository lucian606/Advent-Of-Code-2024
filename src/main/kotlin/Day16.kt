import java.util.*

private data class CellPath(val cell: Cell, val direction: Int, val cost: Int, val path: MutableSet<Cell> = mutableSetOf())

class Day16(filePath: String) : DaySolver(filePath) {

    override fun solvePartOne(input: List<String>): String {
        val start = getTileLocation(input, 'S')
        val end = getTileLocation(input, 'E')
        return getShortestPath(input, start, end).toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val start = getTileLocation(input, 'S')
        val end = getTileLocation(input, 'E')
        val pathTiles = getTilesOnShortestPaths(input, start, end)

        return pathTiles.size.toString()
    }

    fun getTileLocation(input: List<String>, tile: Char): Cell {
        val row = input.indexOfFirst { it.contains(tile) }
        val col = input[row].indexOf(tile)

        return Cell(row, col)
    }

    fun getShortestPath(input: List<String>, start: Cell, end: Cell): Int {
        val cellCosts = mutableMapOf<Cell, Int>()
        val queue: Queue<CellPath> = LinkedList()
        queue.add(CellPath(start, EAST, 0))

        while (queue.isNotEmpty()) {
            val (cell, currentDirection, cost) = queue.poll()
            val currentCost = cellCosts.getOrDefault(cell, Int.MAX_VALUE)
            if (cost < currentCost) {
                cellCosts[cell] = cost
            }

            if (cell == end) {
                continue
            }

            for (direction in directions.indices) {
                val neighbour = cell.add(directions[direction])
                var visitingCost = cost + 1
                if (direction != currentDirection) {
                    visitingCost += 1000
                }
                if (input[neighbour.row][neighbour.col] != '#') {
                    val neighbourCost = cellCosts.getOrDefault(Cell(neighbour.row, neighbour.col), Int.MAX_VALUE)
                    if (visitingCost <= neighbourCost) {
                        queue.add(CellPath(Cell(neighbour.row, neighbour.col), direction, visitingCost))
                    }
                }
            }
        }

        return cellCosts.getOrDefault(end, Int.MAX_VALUE)
    }

    fun getTilesOnShortestPaths(input: List<String>, start: Cell, end: Cell): Set<Cell> {
        val cellCosts = mutableMapOf<Cell, Int>()
        val queue: Queue<CellPath> = LinkedList()
        val finishedPath = mutableMapOf<Int, MutableSet<Cell>>()
        val startPath = mutableSetOf<Cell>()
        startPath.add(start)
        queue.add(CellPath(start, EAST, 0, startPath))

        while (queue.isNotEmpty()) {
            val (cell, currentDirection, cost, path) = queue.poll()
            val currentCost = cellCosts.getOrDefault(cell, Int.MAX_VALUE)
            if (cost < currentCost) {
                cellCosts[cell] = cost
            }

            if (cell == end) {
                val previousPath = finishedPath.getOrDefault(cost, emptySet())
                path.addAll(previousPath)
                finishedPath[cost] = path
                continue
            }

            for (direction in directions.indices) {
                val (neighRow, neighCol) = cell.add(directions[direction])
                var visitingCost = cost + 1
                if (direction != currentDirection) {
                    visitingCost += 1000
                }
                if (input[neighRow][neighCol] != '#') {
                    val neighbourCost = cellCosts.getOrDefault(Cell(neighRow, neighCol), Int.MAX_VALUE)
                    if (visitingCost <= neighbourCost) {
                        val newPath = path.toMutableSet()
                        newPath.add(Cell(neighRow, neighCol))
                        queue.add(CellPath(Cell(neighRow, neighCol), direction, visitingCost, newPath))
                    }
                }
            }
        }

        return finishedPath.getOrDefault(cellCosts[end]!!, emptySet())
    }
}

fun main() {
    val path = "src/main/inputs/day16.in"
    val day = Day16(path)
    day.printSolution()
}
