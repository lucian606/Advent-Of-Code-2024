typealias Cell = Pair<Int, Int>

fun Cell.add(other: Cell): Cell {
    return Cell(this.first + other.first, this.second + other.second)
}

fun Cell.isOutOfBounds(maxRow: Int, maxCol: Int): Boolean {
    return this.first < 0 || this.first >= maxRow || this.second < 0 || this.second >= maxCol
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
