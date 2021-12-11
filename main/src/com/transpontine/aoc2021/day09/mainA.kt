package com.transpontine.aoc2021.day08

import java.io.File
import kotlin.math.abs

private val segmentCountToDigit = mapOf(
    2 to 1,
    4 to 4,
    3 to 7,
    7 to 8,
)

private fun process(fileName: String) {
    println("Processing $fileName")
    val count = File(fileName)
        .readLines().sumOf { line ->
            val digits = line.split(" | ")
                .last()

            println("Line: $digits")

                digits
                .split(' ')
                .map { it.length }
                .count {
                    println("? $it -> ${segmentCountToDigit[it]}")
                    it in segmentCountToDigit
                }
        }
    println("Count: $count")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
