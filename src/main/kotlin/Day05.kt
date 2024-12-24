private typealias Rule = Pair<Long, Long>
private typealias Update = List<Long>

class Day5(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        val updates = getUpdates(input)
        val orderingRules = getOrderingMap(input)

        val correctUpdates = updates.filter { isUpdateCorrect(it, orderingRules) }

        return correctUpdates.fold(0L) { acc, update ->
            acc + update.middle()
        }.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val updates = getUpdates(input)
        val orderingRules = getOrderingMap(input)

        val incorrectUpdates = updates.filterNot { isUpdateCorrect(it, orderingRules) }
        val fixedUpdates = incorrectUpdates.map { fixUpdate(it, orderingRules) }

        return fixedUpdates.fold(0L) { acc, update ->
            acc + update.middle()
        }.toString()
    }

    fun getRules(input: List<String>): List<Rule> {
        val rules = mutableListOf<Rule>()

        for (line in input) {
            if (line.isEmpty()) {
                break
            }
            val ruleNumbers = line.split('|').filter { it.isNotEmpty() }.map { it.toLong() }
            rules.add(Rule(ruleNumbers[0], ruleNumbers[1]))
        }

        return rules
    }

    fun getUpdates(input: List<String>): List<Update> {
        val updates = mutableListOf<Update>()
        val start = input.indexOfFirst { it.isEmpty() } + 1

        for (i in start until input.size) {
            val update = convertStringToNumberList(input[i], ",")
            updates.add(update)
        }

        return updates
    }

    fun getOrderingMap(input: List<String>): Map<Long, Set<Long>> {
        val rules = getRules(input)
        val orderingMap: MutableMap<Long, MutableSet<Long>> = mutableMapOf()

        for ((a, b) in rules) {
            val ordering = orderingMap.getOrDefault(a, mutableSetOf())
            ordering.add(b)
            orderingMap[a] = ordering
        }

        return orderingMap
    }

    fun isUpdateCorrect(update: Update, orderingRules: Map<Long, Set<Long>>): Boolean {
        for (i in update.indices) {
            val ordering = orderingRules.getOrDefault(update[i], emptySet())
            for (j in 0 until i) {
                if (ordering.contains(update[j])) {
                    return false
                }
            }
        }

        return true
    }

    fun fixUpdate(update: Update, orderingRules: Map<Long, Set<Long>>): Update {
        val newUpdate = update.toMutableList()
        var finished = false

        while(!finished) {
            finished = true

            for (i in newUpdate.indices) {
                val ordering = orderingRules.getOrDefault(newUpdate[i], emptySet())
                for (j in 0 until i) {
                    if (ordering.contains(newUpdate[j])) {
                        newUpdate.swap(i, j)
                        finished = false
                    }
                }
            }
        }

        return newUpdate
    }

    fun List<Long>.middle(): Long {
        return this[size / 2]
    }

    fun MutableList<Long>.swap(a: Int, b: Int) {
        val aux = this[a]
        this[a] = this[b]
        this[b] = aux
    }
}

fun main() {
    val path = "src/main/inputs/day5.in"
    val day = Day5(path)
    day.printSolution()
}
