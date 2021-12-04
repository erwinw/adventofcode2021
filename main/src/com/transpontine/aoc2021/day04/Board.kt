package com.transpontine.aoc2021.day04

class Board(lines: List<String>) {
    private val cells = lines.also {
        println("LINES:")
        it.forEachIndexed { index, l -> println(">> [$index] $l") }
    }
        .map { line ->
            line
                .trim()
                .split(Regex("\\s+"))
                .map { num ->
                    Cell(num.toByte())
                }
        }

    fun pickChip(chip: Byte) {
        val previousBingo = hasBingo
        cells.forEach { row ->
            row.forEach { cell ->
                if (cell.number == chip) {
                    cell.taken = true
                }
            }
        }
        hasBingo = calculateHasBingo()
        justGotBingo = hasBingo && !previousBingo
    }

    var justGotBingo = false
        private set

    var hasBingo = false
        private set

    private fun calculateHasBingo(): Boolean {
        cells.forEach { row ->
            if (row.all { it.taken }) {
                return true
            }
        }
        for (colIdx in 0 until cells[0].size) {
            if (cells.all { it[colIdx].taken }) {
                return true
            }
        }

        return false
    }

    fun sumLeft() =
        cells.fold(0) { accRow, row ->
            accRow + row
                .filter { !it.taken }
                .fold(0) { accCell, cell -> accCell + cell.number.toInt() }
        }

    override fun toString(): String {
        return cells.joinToString("\n") { row ->
            row
                .joinToString(" ") { cell ->
                    if (cell.taken) {
                        "[%2s]".format(cell.number)
                    } else {
                        " %2s ".format(cell.number)
                    }
                }
        } + "\n"
    }
}