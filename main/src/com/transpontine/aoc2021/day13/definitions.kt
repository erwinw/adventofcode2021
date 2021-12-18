package com.transpontine.aoc2021.day13

import java.io.File

class Sheet(
    val dimX: Int,
    val dimY: Int,
) {
    private val cells = MutableList(dimY) {
        MutableList(dimX) { false }
    }

    operator fun get(x: Int, y: Int): Boolean = cells[y][x]

    operator fun set(x: Int, y: Int, hasDot: Boolean) {
        cells[y][x] = hasDot
    }

    val dotCount
        get() = cells.sumOf { row ->
            row.count { it }
        }

    fun performFold(fold: Fold): Sheet {
        // Note: Int division rounds towards 0.
        val (resultX, resultY) = when (fold.axis) {
            Axis.X -> fold.position to dimY
            Axis.Y -> dimX to fold.position
        }
        val result = Sheet(dimX = resultX, dimY = resultY)
        println("Result sheet will be $resultX x $resultY; fold: ${fold.axis}")

        // If we have a coordinate below that falls below/to the right of the line, we need to map that to the upper/left half.
        val foldCoordinates: ((x: Int, y: Int) -> Pair<Int, Int>) = when (fold.axis) {
            Axis.X -> { x, y ->
                Pair(fold.wrap(x), y)
            }
            Axis.Y -> { x, y ->
                Pair(x, fold.wrap(y))
            }
        }

        // iterate over all the coordinates in the result sheet, see if they are set in the upper/left half or other half
        for (x in 0 until resultX) {
            for (y in 0 until resultY) {
                if (this[x, y]) {
                    result[x, y] = true
                    continue
                }
                val (foldedX, foldedY) = foldCoordinates(x, y)
                if (foldedX < dimX && foldedY < dimY && this[foldedX, foldedY]) {
                    result[x, y] = true
                }
            }
        }

        return result
    }

    override fun toString() = cells.joinToString("\n") { row ->
        row.joinToString(" ") { hasDot ->
            "#".takeIf { hasDot } ?: "."
        }
    }
}

data class Coordinate(val x: Int, val y: Int)

enum class Axis(val value: String) {
    X("x"),
    Y("y"),
}

data class Fold(val axis: Axis, val position: Int) {
    override fun toString() = "Fold by $axis, position $position."

    fun wrap(dotPosition: Int) = 2 * position - dotPosition
}

fun readInput(fileName: String): Pair<Sheet, List<Fold>> {
    val lines = File(fileName).readLines()
    val emptyIndex = lines.indexOfFirst { it.isEmpty() }
    val sheetCoordinates = lines.take(emptyIndex)
        .map { line ->
            val (x, y) = line.split(",").map { it.toInt() }
            Coordinate(x = x, y = y)
        }
    val folds = lines.drop(emptyIndex + 1)
        .map { line ->
            // Drop `fold along ` -- hacky shortcut but it works!
            val (axisName, positionString) = line.drop(11).split('=')
            val axis = Axis.values().first { it.value == axisName }
            val position = positionString.toInt()
            Fold(axis, position)
        }

    val dimX = sheetCoordinates.maxOf { it.x } + 1
    val dimY = sheetCoordinates.maxOf { it.y } + 1
    val sheet = Sheet(dimX = dimX, dimY = dimY)
    sheetCoordinates.forEach { coordinate -> sheet[coordinate.x, coordinate.y] = true }

    return sheet to folds
}
