package com.transpontine.aoc2021.day09

import java.io.File

private lateinit var heightMap: List<List<Byte>>
private var rowCount: Int = -1
private var columnCount: Int = -1

private data class Coordinate(val row: Int, val column: Int) {
    val hasAbove get() = row > 0
    val hasToLeft get() = column > 0
    val hasBelow get() = row < rowCount - 1
    val hasToRight get() = column < columnCount - 1
    val neighbours
        get() = listOfNotNull(
            aboveOrNull(),
            toLeftOrNull(),
            belowOrNull(),
            toRightOrNull(),
        )

    fun above() = decrementRow()
    fun below() = incrementRow()
    fun toLeft() = decrementColumn()
    fun toRight() = incrementColumn()
    fun aboveOrNull() = if (hasAbove) decrementRow() else null
    fun belowOrNull() = if (hasBelow) incrementRow() else null
    fun toLeftOrNull() = if (hasToLeft) decrementColumn() else null
    fun toRightOrNull() = if (hasToRight) incrementColumn() else null

    fun incrementRow(amount: Int = 1) = copy(row = row + amount)
    fun decrementRow(amount: Int = 1) = copy(row = row - amount)
    fun incrementColumn(amount: Int = 1) = copy(column = column + amount)
    fun decrementColumn(amount: Int = 1) = copy(column = column - amount)

    override fun toString() = "[$row,$column]"
}

private operator fun List<List<Byte>>.get(coordinate: Coordinate) =
    this[coordinate.row][coordinate.column]

private val Coordinate.isLocalLow: Boolean
    get() {
        val localValue = heightMap[this]
        // above
        if (hasAbove && heightMap[above()] <= localValue) {
            return false
        }

        // left
        if (hasToLeft && heightMap[toLeft()] <= localValue) {
            return false
        }
        // below
        if (hasBelow && heightMap[below()] <= localValue) {
            return false
        }

        // right
        if (hasToRight && heightMap[toRight()] <= localValue) {
            return false
        }

        return true
    }

private fun process(fileName: String) {
    println("Processing $fileName")
    heightMap = File(fileName)
        .readLines()
        .map { line ->
            line.map {
                it.toString().toByte()
            }
        }
    rowCount = heightMap.size
    columnCount = heightMap.first().size

    var localLowsCount = 0
    var localLowsScore = 0

    val localLows: MutableList<Coordinate> = mutableListOf()

    for (row in 0 until rowCount) {
        for (column in 0 until columnCount) {
            val coordinate = Coordinate(row, column)
            if (coordinate.isLocalLow) {
                localLows.add(coordinate)
                val localValue = heightMap[row][column] + 1
                localLowsCount += 1
                localLowsScore += localValue
            }
        }
    }
    println("Count: $localLowsCount, score: $localLowsScore")

    // Now determine the basins of each local low
    // Do this iteratively - while the basin keep growing,

    // List of pairs of coordinate to basin size
    val basins: List<Pair<Coordinate, Int>> = localLows.map { localLow ->
        val basinMembers: MutableSet<Coordinate> = mutableSetOf(localLow)
        val unexpandedMembers: MutableSet<Coordinate> = mutableSetOf(localLow)

        println("Basin $localLow (value ${heightMap[localLow]})")

        do {
            val subject = unexpandedMembers.first()
            val subjectValue = heightMap[subject]
            println("ITER: $subject (value $subjectValue)")
            val basinSizeBefore = basinMembers.size
            val unexpandedMembersBefore = unexpandedMembers.size

            // determine new members and add to basin
            val newMembers = subject.neighbours.filterNot {
                val candidateValue = heightMap[it]
                val inBasin = it in basinMembers
                val isNine = candidateValue >= 9
                val isLower = candidateValue < subjectValue
                // println("Candidate: $it (value $candidateValue): in?: $inBasin, nine?: $isNine, lower?: $isLower")
                inBasin || isNine || isLower
            }
            basinMembers += newMembers
            unexpandedMembers += newMembers

            // println("Expanded $subject; basin: $basinSizeBefore -> ${basinMembers.size}, unexpanded $unexpandedMembersBefore -> ${unexpandedMembers.size} new: ${newMembers.size}")

            unexpandedMembers.remove(subject)
        } while (unexpandedMembers.isNotEmpty())

        println("Result basin $localLow: ${basinMembers.size} = $basinMembers\n")

        localLow to basinMembers.size
    }

    val top3 = basins.sortedByDescending { it.second }.take(3)
    top3.forEachIndexed { index, (coordinate, size) -> println("Basin #${index + 1}: $coordinate = $size") }

    val score = top3.map { it.second }.reduce { product, it -> product * it }
    println("Score: $score")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
