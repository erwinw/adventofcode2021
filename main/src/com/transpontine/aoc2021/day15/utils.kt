package com.transpontine.aoc2021.day15

import java.io.File

typealias Cave = Array<IntArray>

data class Point(val x: Int, val y: Int)

val Cave.maxX get() = first().size - 1
val Cave.maxY get() = size - 1

fun Cave.expand(times: Int): Cave {
    val oldMaxX = maxX
    val oldMaxY = maxY
    val oldDimX = oldMaxX + 1
    val oldDimY = oldMaxY + 1
    val newDimX = oldDimX * times
    val newDimY = oldDimY * times

    val newCave = Array(newDimY) { IntArray(newDimX) }

    for (y in 0..oldMaxY) {
        for (x in 0..oldMaxX) {
            val value = this[y][x]
            repeat(times) { repY ->
                val newY = repY * oldDimY + y
                repeat(times) { repX ->
                    val newX = repX * oldDimX + x

                    // We cannot use modulo; after 9 comes 1, not 0
                    // I'm sure I can come up with a neat way of doing this using a modulo 8 - but this works too.
                    var rawNewValue = value + repY + repX
                    while (rawNewValue > 9) {
                        rawNewValue -= 9
                    }
                    newCave[newY][newX] = rawNewValue
                }
            }
        }
    }

    return newCave
}

class Traversal(
    val point: Point,
    val totalRisk: Int,
) : Comparable<Traversal> {
    constructor(x: Int, y: Int, totalRisk: Int) : this(Point(x, y), totalRisk)

    fun neighbours(cave: Cave): List<Traversal> = buildList {
        fun add(x: Int, y: Int) {
            add(Traversal(x, y, totalRisk + cave[y][x]))
        }

        if (point.x > 0) add(point.x - 1, point.y)
        if (point.y > 0) add(point.x, point.y - 1)
        if (point.x < cave.maxX) add(point.x + 1, point.y)
        if (point.y < cave.maxY) add(point.x, point.y + 1)
    }

    override fun compareTo(other: Traversal): Int =
        this.totalRisk - other.totalRisk

    override fun toString() = "(${point.x}, ${point.y}) @ $totalRisk"

    // To be able to find Traversals in the to do
    override fun equals(other: Any?): Boolean {
        return other != null && point == (other as? Traversal)?.point
    }

    override fun hashCode() = point.hashCode()
}

fun readInput(fileName: String): Cave =
    File(fileName)
        .readLines()
        .map { line ->
            line
                .map { it - '0' }
                .toIntArray()
        }.toTypedArray()
