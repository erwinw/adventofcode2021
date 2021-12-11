package com.transpontine.aoc2021.day10

import java.io.File
import java.math.BigInteger

private val braces = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

private val illegalCharacters = setOf(')', ']', '}', '>')

private val charScore = mapOf(
    ')' to 1.toBigInteger(),
    ']' to 2.toBigInteger(),
    '}' to 3.toBigInteger(),
    '>' to 4.toBigInteger(),
)

private fun scoreLine(line: String): BigInteger {
    val stack = mutableListOf<Char>()
    println("Line $line ?")
    line.forEach { char ->
        when {
            char == stack.firstOrNull() -> stack.removeAt(0)
            char in braces -> stack.add(0, braces[char]!!)
            char in illegalCharacters -> return BigInteger.ZERO
            else -> throw Exception("Unexpected character: '$char'")
        }
    }
    println("Line $line remainder: ${stack.joinToString("")}")

    return stack.fold(BigInteger.ZERO) { total, char ->
        total * 5.toBigInteger() + charScore[char]!!
    }.also { println("Line score: $it")}
}

private fun process(fileName: String) {
    println("Processing $fileName")
    val lineScores = File(fileName)
        .readLines()
        .map(::scoreLine)
        .filterNot { it == BigInteger.ZERO }
        .sorted()

    val middle = lineScores.size / 2
    println("Lines: ${lineScores.size} => $middle")
    val score = lineScores[middle]

    println("Score: $score")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
