import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day12Test {
    @Test
    fun `Test get corners of a simple square`() {
        val path = "src/test/inputs/day12_square.in"
        val day = Day12(path)
        val input = day.readInput()
        val regions = day.getRegions(input)

        assertEquals(1, regions.size)
        assertEquals(4, with(day) { regions.first().getCorners() })
    }

    @Test
    fun `Test get corners of a square`() {
        val path = "src/test/inputs/day12_square.in"
        val day = Day12(path)
        val input = day.readInput()
        val regions = day.getRegions(input)

        assertEquals(1, regions.size)
        assertEquals(4, with(day) { regions.first().getCorners() })
    }

    @Test
    fun `Test get corners of a hourglass`() {
        val path = "src/test/inputs/day12_hourglass.in"
        val day = Day12(path)
        val input = day.readInput()
        val regions = day.getRegions(input)

        assertEquals(3, regions.size)
        val hourglass = regions.first { with(day) { it.getArea() > 1} }

        assertEquals(12, with(day) { hourglass.getCorners() })
    }

    @Test
    fun `Test get corners of a punctured shape`() {
        val path = "src/test/inputs/day12_punctured_shape.in"
        val day = Day12(path)
        val input = day.readInput()
        val regions = day.getRegions(input)

        assertEquals(5, regions.size)
        val hourglass = regions.first { with(day) { it.getArea() > 1} }

        assertEquals(20, with(day) { hourglass.getCorners() })
    }

    @Test
    fun `Test get corners of an L shape`() {
        val path = "src/test/inputs/day12_L_shape.in"
        val day = Day12(path)
        val input = day.readInput()
        val regions = day.getRegions(input)

        assertEquals(2, regions.size)
        val L = regions.first { with(day) { it.getArea() > 1} }

        assertEquals(6, with(day) { L.getCorners() })
    }


    @Test
    fun `Test first example`() {
        val path = "src/test/inputs/day12_example1.in"
        val day = Day12(path)
        val input = day.readInput()

        assertEquals(1206, day.solvePartTwo(input).toInt())
    }
}