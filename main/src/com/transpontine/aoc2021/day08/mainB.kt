package com.transpontine.aoc2021.day08

import java.io.File

private val segmentCountToDigit = mapOf(
    2 to listOf(1),
    4 to listOf(4),
    3 to listOf(7),
    7 to listOf(8),

    6 to listOf(0, 6),
    5 to listOf(2, 3, 5, 9),
)

private val digitsToSignals = mapOf(
    0 to listOf('A', 'B','C', 'E', 'F'),
    1 to listOf('C', 'F'),
    2 to listOf('A', 'C', 'D', 'E', 'G'),
    3 to listOf('A', 'C', 'D', 'F', 'G'),
    4 to listOf('B', 'C', 'D', 'F'),
    5 to listOf('A', 'B', 'D', 'F', 'G'),
    6 to listOf('A', 'B', 'D', 'E', 'F', 'G'),
    7 to listOf('A', 'C', 'F'),
    8 to listOf('A', 'B', 'C', 'D', 'E', 'F', 'G'),
    9 to listOf('A', 'B', 'C', 'D', 'F', 'G'),
)

private fun decipherLine(signalPatterns: List<String>, digits: List<String>): Int {
    /*
    Need to figure out which lower-case letter maps to which uppercase letter.
     */
    // First see if we can uniquely identify any letters
    signalPatterns.forEachIndexed { idx, x ->
        println("$idx: $x")
    }
    // Each signal pattern matches, by length, 1 or more digits.
    // Each digit consists of 2 or more segments.
    signalPatterns.forEach { signal ->
        val signalSegments = signal.characters

        segmentCountToDigit[signal.length]?.forEach { candidateDigit ->
            val candidateSegments = digitsToSignals[candidateDigit] ?: emptyList()

            signalSegments.forEach { signalChar ->
                candidateSegments.forEach { candidateChar ->
                   println("$signalChar -> $candidateChar")
                }
            }
        }
    }
    TODO()
}

private val String.characters get() = map { it }

private fun process(fileName: String) {
    println("Processing $fileName")
    val sum = File(fileName)
        .readLines().sumOf { line ->
            val (signalPatterns, digits) = line.split(" | ")
                .map {
                    it.split(' ')
                }

            decipherLine(signalPatterns, digits)
        }
    println("Sum: $sum")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
