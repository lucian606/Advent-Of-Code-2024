import java.lang.Math.abs

typealias Cell = Pair<Int, Int>
typealias PathList = Pair<Cell, List<Cell>>

fun Cell.add(other: Cell): Cell {
    return Cell(this.first + other.first, this.second + other.second)
}
fun Cell.getDistance(other: Cell): Int {
    return abs(this.first - other.first) + abs(this.second - other.second)
}

fun Cell.isOutOfBounds(maxRow: Int, maxCol: Int): Boolean {
    return this.first < 0 || this.first >= maxRow || this.second < 0 || this.second >= maxCol
}

fun getOpositeDirection(direction: Int): Int {
    return (direction + 2) % 4
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
