import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14Test {

    @Test
    fun `test robot movement`() {
        val startPosition = Cell(0, 0)
        val velocity = Cell(3, 2)
        val robot = Robot(startPosition, velocity, 10, 10)
        robot.move()
        val expectedPos = Cell(3, 2)
        assertEquals(expectedPos, robot.position)
    }

    @Test
    fun `test robot negative movement`() {
        val startPosition = Cell(0, 0)
        val velocity = Cell(-2, -3)
        val robot = Robot(startPosition, velocity, 10, 10)
        robot.move()
        val expectedPos = Cell(8, 7)
        assertEquals(expectedPos, robot.position)
    }

    @Test
    fun `test robot over bounds movement`() {
        val startPosition = Cell(9, 8)
        val velocity = Cell(2, 3)
        val robot = Robot(startPosition, velocity, 10, 10)
        robot.move()
        val expectedPos = Cell(1, 1)
        assertEquals(expectedPos, robot.position)
    }
}