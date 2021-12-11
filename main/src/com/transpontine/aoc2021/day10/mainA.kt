package com.transpontine.aoc2021.day10

import java.io.File

private val braces = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

private val illegalScore = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)

private fun scoreLine(line: String): Int {
    val stack = mutableListOf<Char>()
    line.forEach { char ->
        when {
            char == stack.firstOrNull() -> stack.removeAt(0)
            char in braces -> stack.add(0, braces[char]!!)
            char in illegalScore -> return illegalScore[char]!!
            else -> throw Exception("Unexpected character: '$char'")
        }
    }

    return 0
}

private fun process(fileName: String) {
    println("Processing $fileName")
    val score = File(fileName)
        .readLines()
        .sumOf(::scoreLine)

    println("Score: $score")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
