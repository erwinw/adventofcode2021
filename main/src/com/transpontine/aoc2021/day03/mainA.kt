package com.transpontine.aoc2021.day03

import java.io.File

private fun process(fileName: String, bits: Int) {
    println("Processing $fileName")

    val bitCounts = List(bits) { MutableList(2) { 0 } }

    File(fileName).forEachLine { line ->
        val lineBits = line.toCharArray().toList()

        lineBits.forEachIndexed { idx, character ->
            when (character) {
                '0' -> bitCounts[idx][0] += 1
                '1' -> bitCounts[idx][1] += 1
            }
        }
    }

    val gamma = bitCounts
        .map { (countZero, countOne) ->
            '0'.takeIf { countZero > countOne } ?: '1'
        }
        .joinToString(separator = "").toInt(2)
    val epsilon = bitCounts
        .map { (countZero, countOne) ->
            '1'.takeIf { countZero > countOne } ?: '0'
        }
        .joinToString(separator = "").toInt(2)

    println("Gamma $gamma x epsilon $epsilon = ${gamma * epsilon}")
}

fun main(args: Array<String>) {
    if (args.size != 2) {
        throw Error("Do not forget to pass in an input filename and bit count!")
    }
    process(args[0], args[1].toInt())
}
