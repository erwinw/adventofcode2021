package com.transpontine.aoc2021.day11

import java.io.File

private var maxRow = 0
private var maxColumn = 0

private data class Coordinate(val row: Int, val column: Int) {
    val hasAbove get() = row > 0
    val hasBelow get() = row < maxRow
    val hasToLeft get() = column > 0
    val hasToRight get() = column < maxColumn

    fun neighbours() = listOfNotNull(
        Coordinate(row - 1, column - 1).takeIf { hasAbove && hasToLeft },
        Coordinate(row - 1, column).takeIf { hasAbove },
        Coordinate(row - 1, column + 1).takeIf { hasAbove && hasToRight },

        Coordinate(row, column - 1).takeIf { hasToLeft },
        Coordinate(row, column + 1).takeIf { hasToRight },

        Coordinate(row + 1, column - 1).takeIf { hasBelow && hasToLeft },
        Coordinate(row + 1, column).takeIf { hasBelow },
        Coordinate(row + 1, column + 1).takeIf { hasBelow && hasToRight },
    )
}

private data class Octopus(val coordinate: Coordinate, val value: Byte)

private typealias Octopuses = List<MutableList<Byte>>

private fun Octopuses.coordinateIterator() = OctopusesIterator(this)

private fun Octopuses.present() =
    joinToString("\n") { row ->
        row.joinToString(separator = ",", prefix = "[", postfix = "]") { it.toString() }
    }

private class OctopusesIterator(private val octopuses: Octopuses) : Iterator<Octopus> {
    private var row: Int = 0
    private var column: Int = 0

    override fun hasNext() = row != maxRow || column != maxColumn
    override fun next(): Octopus {
        val level = octopuses[row][column]

        column += 1
        if (column > maxColumn) {
            column = 0
            row += 1
        }

        return Octopus(Coordinate(row, column), level)
    }
}

private fun Octopuses.clone() = map { row -> row.map { it }.toMutableList() }
private fun Octopuses.incrementAll() =
    indices.forEach { row ->
        first().indices.forEach { column ->
            this[row][column] = (this[row][column] + 1).toByte()
        }
    }

private fun Octopuses.resetFlashed() =
    indices.forEach { row ->
        first().indices.forEach { column ->
            if (this[row][column] > 9) {
                this[row][column] = 0.toByte()
            }
        }
    }

private fun Octopuses.increment(coordinate: Coordinate) {
    val (row, column) = coordinate
    this[row][column] = (this[row][column] + 1).toByte()
}

var flashedCount = 0

private fun performStep(octopuses: Octopuses, step: Int): Octopuses {
    val inProgress = octopuses.clone()
    // step A: the energy level of each octopus increases by 1
    inProgress.incrementAll()
    println("Incremented:\n${inProgress.present()}\n\n")

    val flashed = mutableListOf<Coordinate>()
    var newFlash: Boolean
    do {
        val flashing = mutableListOf<Coordinate>()

        val flashingIterator = inProgress.coordinateIterator()
        while (flashingIterator.hasNext()) {
            val (coordinate, level) = flashingIterator.next()
            if (level > 9 && coordinate !in flashed) {
                println("Flashing: $coordinate $level")
                flashed += coordinate
                flashing += coordinate
            }
        }

        flashing.forEach { flashingOctopus ->
            flashingOctopus.neighbours().forEach { coordinate ->
                inProgress.increment(coordinate)
            }
        }

        newFlash = flashing.isNotEmpty()
    } while (newFlash)

    flashedCount += flashed.size

    inProgress.resetFlashed()

    println("Step: $step\nFlashed: $flashedCount\nOctopuses:\n${inProgress.present()}\n\n")

    return inProgress
}

private fun process(fileName: String) {
    println("Processing $fileName")
    val octopuses = File(fileName)
        .readLines()
        .map { line ->
            line.map { it.toString().toByte() }
                .toMutableList()
        }
    println("START:\n${octopuses.present()}\n\n")
    maxRow = octopuses.size - 1
    maxColumn = octopuses.first().size - 1

    (1..100).fold(octopuses, ::performStep)
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
