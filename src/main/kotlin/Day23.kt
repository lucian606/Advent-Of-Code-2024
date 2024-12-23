import java.util.StringJoiner

class Day23(filePath: String) : DaySolver(filePath) {

    override fun solvePartOne(input: List<String>): String {
        val graph = getGraph(input)
        val cycles = getCliquesOfSizeThree(graph)
        return cycles.filter { it.any { s -> s[0] == 't' } }.size.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val graph = getGraph(input)
        val maximumClique = getCliquesOfMaximumSize(graph).first()
        return maximumClique.joinToString(separator = ",") { it }
    }

    fun getGraph(input: List<String>): Map<String, List<String>> {
        val graph = mutableMapOf<String, MutableList<String>>()

        for (line in input) {
            val (node, neighbour) = line.split("-")
            val nodeNeighbors = graph.getOrDefault(node, mutableListOf())
            nodeNeighbors.add(neighbour)
            graph[node] = nodeNeighbors
            val neighbourNeighbors = graph.getOrDefault(neighbour, mutableListOf())
            neighbourNeighbors.add(node)
            graph[neighbour] = neighbourNeighbors
        }
        return graph
    }

    fun getCliquesOfSizeThree(graph: Map<String, List<String>>): Set<List<String>> {
        val results = mutableSetOf<List<String>>()
        for (node in graph.keys) {
            val neighbours = graph[node]
            if (neighbours == null) {
                continue
            }
            for (i in neighbours.indices) {
                for (j in i + 1 until neighbours.size) {
                    if (graph[neighbours[j]]?.contains(neighbours[i]) == true) {
                        val cycle = listOf(
                            node,
                            neighbours[i],
                            neighbours[j]
                        ).sorted()

                        results.add(cycle)
                    }
                }
            }
        }

        return results
    }

    fun getCliquesOfMaximumSize(graph: Map<String, List<String>>): Set<List<String>> {
        val cliques = getCliquesOfSizeThree(graph).toMutableList()

        for (i in cliques.indices) {
            val clique = cliques[i].toMutableList()
            for (node in graph.keys) {
                if (graph[node].isNullOrEmpty()) {
                    continue
                } else {
                    if (node !in clique && graph[node]!!.containsAll(clique)) {
                        clique.add(node)
                    }
                }
            }
            cliques[i] = clique
        }

        val maximumSize = cliques.fold(0) { acc, it -> if (it.size > acc) it.size else acc }

        return cliques.filter { it.size == maximumSize }.map { it.sorted() }.toSet()
    }
}

fun main() {
    val path = "src/main/inputs/day23.in"
    val day = Day23(path)
    day.printSolution()
}