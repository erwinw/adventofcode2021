package com.transpontine.aoc2021.day01

import java.io.File

fun process(fileName: String) {
    println("Processing $fileName")
    var previousLine: Int? = null
    var increases = 0
    File(fileName).forEachLine { line ->
        val currentLine = line.toInt()
        if (previousLine?.let { it < currentLine } == true) {
            increases += 1
        }

        previousLine = currentLine
    }

    println("Increases: $increases")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
