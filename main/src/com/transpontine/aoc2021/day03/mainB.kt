package com.transpontine.aoc2021.day03

import java.io.File

private val Number.byte get() = this.toByte()

private fun selectRepresentative(
    bitsPerLine: Byte,
    inputCandidates: List<List<Byte>>,
    mostCommon: Boolean,
): List<Byte> {
    var candidates = inputCandidates.toList()

    val (ifOne, ifZero) = if (mostCommon) {
        Pair(1.byte, 0.byte)
    } else {
        Pair(0.byte, 1.byte)
    }

    for (idx in 0 until bitsPerLine) {
        val bitCounts = candidates
            .fold(MutableList(2) { 0 }) { acc, item ->
                acc[item[idx].toInt()] += 1
                acc
            }

        val picked = ifOne.takeIf { bitCounts[1] >= bitCounts[0] } ?: ifZero

        candidates = candidates.filter { it[idx] == picked }
        if (candidates.size == 1) {
            return candidates[0]
        }
    }

    throw Error("No candidate found")
}

private fun process(fileName: String, bitsPerLine: Byte) {
    println("Processing $fileName")

    val lines = mutableListOf<List<Byte>>()

    File(fileName).forEachLine { line ->
        val lineBits = line.toCharArray().toList().map { it.toString().toByte() }
        lines.add(lineBits)
    }

    val oxygen = selectRepresentative(bitsPerLine, lines, mostCommon = true).joinToString("").toInt(2)
    val co2 = selectRepresentative(bitsPerLine, lines, mostCommon = false).joinToString("").toInt(2)
    println("Oxygen $oxygen x CO2 $co2 = ${oxygen * co2}")
}

fun main(args: Array<String>) {
    if (args.size != 2) {
        throw Error("Do not forget to pass in an input filename and bit count!")
    }
    process(args[0], args[1].toByte())
}
