import java.lang.Math.abs

typealias PathList = Pair<Cell, List<Cell>>

data class Cell(val row: Int, val col: Int) {
    fun add(other: Cell): Cell {
        return Cell(row + other.row, col + other.col)
    }
    fun getDistance(other: Cell): Int {
        return abs(row - other.row) + abs(col - other.col)
    }

    fun isOutOfBounds(maxRow: Int, maxCol: Int): Boolean {
        return row < 0 || row >= maxRow || col < 0 || col >= maxCol
    }

    fun getDistanceAsCell(other: Cell): Cell {
        return Cell(other.row - row, other.col - col)
    }
}

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
