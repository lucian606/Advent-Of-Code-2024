class Day24(filePath: String) : DaySolver(filePath) {

    override fun solvePartOne(input: List<String>): String {
        val gates = getGates(input)
        val wires = getWires(input)
        val binary = getGatesOutput(gates, wires.toMutableMap())

        return binary.toLong(2).toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val gates = getGates(input)

        val swappedBits = findSwappedWires(gates)
        return swappedBits.sorted().joinToString(separator = ",")
    }

    fun getWires(input: List<String>): Map<String, Int> {
        val wires = mutableMapOf<String, Int>()

        for (line in input) {
            if (line.isEmpty()) {
                break
            }
            val (wire, value) = line.split(": ")
            wires[wire] = value.toInt()
        }

        return wires
    }

    fun getGates(input: List<String>): List<List<String>> {
        val gates = mutableListOf<List<String>>()

        for (line in input) {
            if (line.isEmpty() || !line.contains("->")) {
                continue
            }
            val output = line.split(" -> ")[1]
            val gate = line.split(" ").dropLast(2)

            gates.add(gate + output)
        }

        return gates
    }

    fun getGateOuput(gate: List<String>, wires: MutableMap<String, Int>): Boolean {
        val a = gate[0]
        val b = gate[2]
        val output = gate[3]
        if (a !in wires || b !in wires) {
            return false
        }
        when (gate[1]) {
            "AND" -> wires[output] = wires[a]!! and wires[b]!!
            "OR" -> wires[output] = wires[a]!! or wires[b]!!
            "XOR" -> wires[output] = wires[a]!! xor wires[b]!!
        }
        return true
    }

    fun getGatesOutput(gates: List<List<String>>, wires: MutableMap<String, Int>): String {
        var wereAllGatesSolved = false

        while (!wereAllGatesSolved) {

            wereAllGatesSolved = true

            for (gate in gates) {
                val status = getGateOuput(gate, wires)
                if (!status) {
                    wereAllGatesSolved = false
                }
            }
        }

        val zValues = wires.keys.filter { it[0] == 'z' }.sorted()
        var binary = ""
        for (key in zValues) {
            binary += wires[key]!!
        }

        return binary.reversed()
    }

    fun findSwappedWires(gates: List<List<String>>): List<String> {
        val z = gates.filter { it[3][0] == 'z' }.map { it[3] }.sorted()
        val x = z.map { it.replace('z', 'x') }
        val y = z.map { it.replace('z', 'y') }

        val swappedBits = mutableListOf<String>()
        val outputOf = getGatesInputMapping(gates).toMutableMap()
        val inputOf = getGatesOutputMapping(gates)
        var carry = outputOf[listOf("x00", "AND", "y00")]!!

        // Here is the following formula
        // An = xi XOR yi
        // Bn = xi AND yi
        // Zn = An XOR Carry
        // Carry = Bn OR (An AND Carry)
        // A value is defined as correct if it doesn't contain any z value
        //
        // Here is what we know at each step
        // 1. The Carry value, which was previously computed, is always correct
        // 2. A and B are always computed correctly (but they might be swapped with each other)
        // 3. If we know A and Carry, we can compute Zn. So if we find an output for A XOR Carry, we can compute Zn
        //    If we can't find the output, we swap A and B
        // 4. If we could find the output for A XOR Carry, we can check Zn. If it doensn't contain any z value, we swap.
        // 6. If B/C/New Carry contain a z value, we swap them with Zn
        // 7. After making the swaps, we compute then new carry to be used in the next step
        // Note: This algorithm assumes that the first number is computted correctly
        // Note: This algorithm assumes that the last number is computed correctly
        // So we can start from the second number and go until the second last number

        for (i in 1 until z.size - 1) {
            var A = ""
            if (listOf(x[i], "XOR", y[i]) in outputOf) {
                A = outputOf[listOf(x[i], "XOR", y[i])]!!
            } else {
                A = outputOf[listOf(y[i], "XOR", x[i])]!!
            }

            var B = ""
            if (listOf(x[i], "AND", y[i]) in outputOf) {
                B = outputOf[listOf(x[i], "AND", y[i])]!!
            } else {
                B = outputOf[listOf(y[i], "AND", x[i])]!!
            }

            var Zn = ""
            if (listOf(A, "XOR", carry) in outputOf) {
                Zn = outputOf[listOf(A, "XOR", carry)]!!
            } else if (listOf(carry, "XOR", A) in outputOf) {
                Zn = outputOf[listOf(carry, "XOR", A)]!!
            } else {
                val aux = A
                A = B
                B = aux
                Zn = when (outputOf[listOf(A, "XOR", carry)]) {
                    null -> outputOf[listOf(carry, "XOR", A)]!!
                    else -> outputOf[listOf(A, "XOR", carry)]!!
                }
                swappedBits.add(A)
                swappedBits.add(B)
            }

            if (!Zn.contains("z")) {
                swappedBits.add(Zn)
                swappedBits.add(z[i])
                outputOf[inputOf[z[i]]!!] = Zn
                outputOf[inputOf[Zn]!!] = z[i]
            }

            if (B.contains("z")) {
                B = Zn
                Zn = z[i]
            }

            var C = ""
            if (listOf(A, "AND", carry) in outputOf) {
                C = outputOf[listOf(A, "AND", carry)]!!
            } else {
                C = outputOf[listOf(carry, "AND", A)]!!
            }

            if (listOf(B, "OR", C) in outputOf) {
                carry = outputOf[listOf(B, "OR", C)]!!
            } else {
                carry = outputOf[listOf(C, "OR", B)]!!
            }

        }

        return swappedBits
    }

    fun getGatesOutputMapping(gates: List<List<String>>): Map<String, List<String>> {
        val mapping = mutableMapOf<String, List<String>>()

        for (gate in gates) {
            val output = gate[3]
            val input = gate.dropLast(1)
            mapping[output] = input
        }

        return mapping
    }

    fun getGatesInputMapping(gates: List<List<String>>): Map<List<String>, String> {
        val mapping = mutableMapOf<List<String>, String>()

        for (gate in gates) {
            val output = gate[3]
            val input = gate.dropLast(1)
            mapping[input] = output
        }

        return mapping
    }

}

fun main() {
    val path = "src/main/inputs/day24.in"
    val day = Day24(path)
    day.printSolution()
}