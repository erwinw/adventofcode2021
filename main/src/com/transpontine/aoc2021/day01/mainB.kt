package com.transpontine.aoc2021.day01

import java.io.File

private fun process(fileName: String) {
    println("Processing $fileName")
    val window = IntWindow(3)
    var previousSum: Int? = null
    var increases = 0
    File(fileName).forEachLine { line ->
        val currentLine = line.toInt()

        window.add(currentLine)
        val currentSum = window.sum

        if (currentSum != null && previousSum?.let { it < currentSum } == true ) {
            increases += 1
        }

        previousSum = currentSum
    }

    println("Increases: $increases")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
