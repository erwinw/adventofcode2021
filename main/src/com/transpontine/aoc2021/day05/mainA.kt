package com.transpontine.aoc2021.day05

import java.io.File
import java.lang.Integer.max

private fun process(fileName: String) {
    println("Processing $fileName")
    val lines = File(fileName).readLines()
        .map(Line::parse)

    val maxPoint = Point(
        x = lines.maxOf { max(it.start.x, it.finish.x) },
        y = lines.maxOf { max(it.start.y, it.finish.y) },
    )

    var unsafePointCount = 0
    for (y in 0..maxPoint.y) {
        for (x in 0..maxPoint.x) {
            val pointScore = lines.count { it.crosses(Point(x, y)) }
            if (pointScore > 1) {
                unsafePointCount += 1
            }
        }
    }

    println("Unsafe point count: $unsafePointCount")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
