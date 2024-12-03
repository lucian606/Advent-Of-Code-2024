import kotlin.math.abs

class Day2(filePath: String) : DaySolver(filePath) {
    override fun solvePartOne(input: List<String>): String {
        val reports = input.map{ convertStringToNumberList(it) }
        return reports.count { isReportSafe(it) }.toString()
    }

    override fun solvePartTwo(input: List<String>): String {
        val reports = input.map{ convertStringToNumberList(it) }
        return reports.count { canReportBeSafe(it) }.toString()
    }

    fun isReportGradual(report: List<Long>): Boolean {
        for (i in 0 until report.size - 1) {
            if (abs(report[i] - report[i + 1]) !in 1..3) {
                return false
            }
        }
        return true
    }

    fun isReportSafe(report: List<Long>): Boolean {
        var sortedReport = report.sorted()
        if (report == sortedReport) {
            return isReportGradual(report)
        }
        sortedReport = report.sortedDescending()
        if (report == sortedReport) {
            return isReportGradual(report)
        }
        return false
    }

    fun canReportBeSafe(report: List<Long>): Boolean {
        for (i in report.indices) {
            val newReport = report.toMutableList()
            newReport.removeAt(i)
            if (isReportSafe(newReport)) {
                return true
            }
        }
        return false
    }
}

fun main() {
    val path = "src/main/inputs/day2.in"
    val day = Day2(path)
    day.printSolution()
}