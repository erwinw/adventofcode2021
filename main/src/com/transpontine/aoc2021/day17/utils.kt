package com.transpontine.aoc2021.day17

import java.io.File

data class Ranges(val x: IntRange, val y: IntRange) {
    operator fun contains(point: Point): Boolean {
        return point.x in x && point.y in y
    }
}

data class Point(var x: Int, var y: Int) {
    fun add(velocity: Velocity) {
        x += velocity.x
        y += velocity.y
    }
}

data class Velocity(var x: Int, var y: Int) {
    fun step() {
        when {
            x < 0 -> x += 1
            x > 0 -> x -= 1
        }
        y -= 1
    }
}

fun log(s: String) {
    println(s)
}

fun readInput(fileName: String): Ranges {
    val (_, xRange, _, yRange) = File(fileName).readLines().first().split(Regex("[,=]"))
    val (xMin, xMax) = xRange.split("..").map(String::toInt)
    val (yMin, yMax) = yRange.split("..").map(String::toInt)

    return Ranges(
        xMin..xMax,
        yMin..yMax,
    )
}
