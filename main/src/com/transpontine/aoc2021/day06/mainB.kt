package com.transpontine.aoc2021.day06

import java.io.File
import java.lang.Integer.max

private const val INITIAL_START = 8.toByte()
private const val NORMAL_START = 6.toByte()

typealias Fish = List<Pair<Byte, Long>>

private fun Fish.count() = sumOf { it.second }

private fun day(fish: Fish, day: Int): Fish {
    val result = fish.flatMap { (toGo, count) ->
        if (toGo == 0.toByte()) {
            listOf(
                Pair(INITIAL_START, count),
                Pair(NORMAL_START, count),
            )
        } else {
            listOf(
                Pair((toGo - 1).toByte(), count),
            )
        }
    }.groupBy { it.first }
        .toList()
        .map { (fish, days) -> Pair(fish, days.sumOf { it.second })}

    println("Day $day, fish: ${result.count()}")
    return result
}

private fun process(fileName: String) {
    println("Processing $fileName")
    val initial = File(fileName)
        .readLines()
        .first()
        .split(',')
        .map { it.toByte() }
        .groupBy { it }
        .mapValues { it.value.size.toLong() }
        .toList()

    println("Initial: $initial")

    (1 .. 256).fold(initial, ::day)
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
