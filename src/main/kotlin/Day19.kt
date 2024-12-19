

class Day19(filePath: String) : DaySolver(filePath) {

    override fun solvePartOne(input: List<String>): String {
        val stripesList = getStripesList(input)
        val designs = getDesigns(input)

        return designs.filterIndexed { index, s ->
            isDesignPossible(stripesList, s)
        }.size.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val stripesList = getStripesList(input)
        val designs = getDesigns(input)

        return designs.fold(0L) { acc, design ->
            acc + countWaysToCreateDesign(stripesList, design)
        }.toString()
    }

    fun getStripesList(input: List<String>): List<String> {
        return input[0].split(", ")
    }

    fun getDesigns(input: List<String>): List<String> {
        val designs = mutableListOf<String>()

        for (i in 2 until input.size) {
            designs.add(input[i])
        }

        return designs
    }

    fun isDesignPossible(stripesList: List<String>, design: String): Boolean {
        val n = design.length
        val possible = mutableListOf<Boolean>()

        for (i in 0 until n) {
            possible.add(false)
        }

        for (i in n - 1 downTo 0) {
            val subDesign = design.slice(i until n)
            if (subDesign in stripesList) {
                possible[i] = true
            } else {
                for (stripesPattern in stripesList) {
                    val patternLength = stripesPattern.length
                    if (stripesPattern == subDesign.take(patternLength) && possible[i + patternLength]) {
                        possible[i] = true
                        break
                    }
                }
            }
        }

        return possible[0]
    }

    fun countWaysToCreateDesign(stripesList: List<String>, design: String): Long {
        val n = design.length
        val possible = mutableListOf<Long>()

        for (i in 0 .. n) {
            possible.add(0L)
        }

        for (i in n - 1 downTo 0) {
            val subDesign = design.slice(i until n)
            if (subDesign in stripesList) {
                possible[i]++
            }
            for (stripesPattern in stripesList) {
                val patternLength = stripesPattern.length
                if (stripesPattern == subDesign.take(patternLength) && possible[i + patternLength] > 0) {
                    possible[i] += possible[i + patternLength]
                }
            }
        }

        return possible[0]
    }
}

fun main() {
    val path = "src/main/inputs/day19.in"
    val day = Day19(path)
    day.printSolution()
}
