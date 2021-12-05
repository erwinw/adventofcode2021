package com.transpontine.aoc2021.day01

import java.io.File
import kotlin.system.measureNanoTime

private fun processA(fileName: String) {
    println("Processing $fileName")
    val window = IntWindow(3)
    var previousSum: Int? = null
    var increases = 0
    File(fileName).forEachLine { line ->
        val currentLine = line.toInt()

        window.add(currentLine)
        val currentSum = window.sum

        if (currentSum != null && previousSum?.let { it < currentSum } == true) {
            increases += 1
        }

        previousSum = currentSum
    }

    println("Increases: $increases")
}

/** another attempt, just to play with `windowed` */
private fun processB(fileName: String) {
    println("Processing $fileName")
    val increases = File(fileName).readLines()
        .asSequence()
        .map { it.toInt() }
        // convert to a list of running sums of 3 items
        .windowed(3)
        .map { it.sum() }
        // window again, now to compare 2 items
        .windowed(2)
        .count { (prev, curr) -> curr > prev }

    println("Increases: $increases")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    measureNanoTime {
        processA(args[0])
    }.also {
        println("A duration: $it")
    }
    measureNanoTime {
        processB(args[0])
    }.also {
        println("B duration: $it")
    }
}
