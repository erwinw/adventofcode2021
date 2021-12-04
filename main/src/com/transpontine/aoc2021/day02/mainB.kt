package com.transpontine.aoc2021.day02

import java.io.File

private fun process(fileName: String) {
    println("Processing $fileName")
    var horizontal = 0
    var depth = 0
    var aim = 0

    File(fileName).forEachLine { line ->
        val (command, argStr) = line.split(" ", limit = 2)
        val arg = argStr.toInt()

        when (command) {
            Command.DOWN.value -> aim += arg
            Command.UP.value -> aim -= arg
            Command.FORWARD.value -> {
                horizontal += arg
                depth += aim * arg
            }
        }
    }

    println("Result: $horizontal x $depth = ${horizontal * depth}")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
