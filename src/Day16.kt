data class Point2D1(val x: Int, val y: Int) {
    fun neighbours(): List<Point2D1> {
        return listOf(
                Point2D1(x - 1, y),
                Point2D1(x + 1, y),
                Point2D1(x, y - 1),
                Point2D1(x, y + 1),
        )
    }

    operator fun plus(other: Point2D1): Point2D1 = Point2D1(x + other.x, y + other.y)

    companion object {
        val NORTH = Point2D1(0, -1)
        val EAST = Point2D1(1, 0)
        val SOUTH = Point2D1(0, 1)
        val WEST = Point2D1(-1, 0)
    }
}

fun main() {
    data class Beam(val point: Point2D1, val dir: Point2D1)

    fun score(grid: Array<CharArray>, startingBeam: Beam): Int {
        val seen = mutableSetOf<Beam>()
        val queue = ArrayDeque(listOf(startingBeam))

        while (queue.isNotEmpty()) {
            var current = queue.removeFirst()

            while (current !in seen) {
                seen.add(current)
                val nextPoint = current.point + current.dir
                var nextDir = current.dir
                if (nextPoint.y !in grid.indices || nextPoint.x !in grid.first().indices) {
                    break
                }
                when (grid[nextPoint.y][nextPoint.x]) {
                    '/'  -> nextDir = Point2D1(-nextDir.y, -nextDir.x)
                    '\\' -> nextDir = Point2D1(nextDir.y, nextDir.x)
                    '-'  -> if (nextDir.y != 0) {
                        nextDir = Point2D1.EAST
                        queue.add(Beam(nextPoint, Point2D1.WEST))
                    }
                    '|'  -> if (nextDir.x != 0) {
                        nextDir = Point2D1.NORTH
                        queue.add(Beam(nextPoint, Point2D1.SOUTH))
                    }
                }
                current = Beam(nextPoint, nextDir)
            }
        }

        return seen.map { it.point }.toSet().size - 1
    }



    fun part1(input: List<String>): Long {
        val grid = input.map { it.toCharArray() }.toTypedArray()
        val beam = Beam(Point2D1(-1, 0), Point2D1.EAST)
        return score(grid, beam).toLong()
    }
    fun part2(input: List<String>): Long {
        val grid = input.map { it.toCharArray() }.toTypedArray()

        val top = grid.first().indices.map { Beam(Point2D1(it, -1), Point2D1.SOUTH) }
        val bottom = grid.first().indices.map { Beam(Point2D1(it, grid.size), Point2D1.NORTH) }
        val left = grid.indices.map { Beam(Point2D1(-1, it), Point2D1.EAST) }
        val right = grid.indices.map { Beam(Point2D1(grid.first().size, it), Point2D1.WEST) }

        return (top + bottom + left + right).maxOf { score(grid, it) }.toLong()
    }
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day16_test")
    check(part1(testInput) == 46L)
    check(part2(testInput) == 51L)
    val input = readInput("Day16")
    println(part1(input))
    println(part2(input))
}
