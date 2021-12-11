package com.transpontine.aoc2021.day09

import java.io.File

typealias HeightMap = List<List<Byte>>

private lateinit var heightMap: HeightMap
private var rowCount: Int = -1
private var columnCount: Int = -1

private fun isLocalLow(row: Int, column: Int): Boolean {
    val localValue = heightMap[row][column]
    // above
    if (row > 0 && heightMap[row - 1][column] <= localValue) {
        return false
    }

    // left
    if (column > 0 && heightMap[row][column - 1] <= localValue) {
        return false
    }
    // below
    if (row < rowCount - 1 && heightMap[row + 1][column] <= localValue) {
        return false
    }

    // right
    if (column < columnCount - 1 && heightMap[row][column + 1] <= localValue) {
        return false
    }

    return true
}

private fun process(fileName: String) {
    println("Processing $fileName")
    heightMap = File(fileName)
        .readLines()
        .map { line ->
            line.map {
                it.toString().toByte()
            }
        }
    rowCount = heightMap.size
    columnCount = heightMap.first().size

    var localLowsCount = 0
    var localLowsScore = 0

    for (row in 0 until rowCount) {
        for (column in 0 until columnCount) {
            if (isLocalLow(row = row, column = column)) {
                val localValue = heightMap[row][column] + 1
                println("Local low at row $row, column $column: $localValue")
                localLowsCount += 1
                localLowsScore += localValue
            }
        }
    }
    println("Count: $localLowsCount, score: $localLowsScore")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
