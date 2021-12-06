package com.transpontine.aoc2021.day06

import java.io.File
import java.lang.Integer.max

private const val INITIAL_START = 8.toByte()
private const val NORMAL_START = 6.toByte()

private fun day(fish: List<Byte>, day: Int): List<Byte> {
    val result: List<Byte> = fish.flatMap { f ->
        if (f==0.toByte()) {
            listOf(NORMAL_START, INITIAL_START)
        } else {
            listOf((f-1).toByte())
        }
    }
    println("Day $day, fish: ${result.size}")
    return result
}

private fun process(fileName: String) {
    println("Processing $fileName")
    val initial = File(fileName).readLines().first().split(',').map { it.toByte() }

    (1 .. 80).fold(initial, ::day)

    println("Initial: $initial")

}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
