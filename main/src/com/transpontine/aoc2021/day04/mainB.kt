package com.transpontine.aoc2021.day04

import java.io.File

private const val ROWS = 5

private val Number.byte get() = this.toByte()

private fun process(fileName: String) {
    println("Processing $fileName")
    val lines = File(fileName).readLines().toMutableList()
    val guesses = lines[0].split(',').map { it.toByte() }
    val boards = lines
        .drop(2)
        .chunked(ROWS + 1) {
            Board(it.dropLast(1))
        }

    guesses.forEachIndexed { guessIdx, chip ->
        println("Guess $guessIdx: $chip")

        boards.forEach { it.pickChip(chip) }
        val numBingo = boards.count { it.hasBingo}
        val numNewBingo = boards.count { it.justGotBingo}
        println("Has bingo? $numBingo New bingo? $numNewBingo")

        if (boards.all { it.hasBingo} ) {
            val lastBoard = boards.find { it.justGotBingo }
            println("BOARD LAST:\n$lastBoard\n")

            val boardSumLeft = lastBoard!!.sumLeft()
            println("boardSumLeft $boardSumLeft x $chip = ${boardSumLeft * chip.toInt()}")
            return
        }
    }
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
