package com.transpontine.aoc2021.day14

fun Polymer.applyRules(rules: Rules): Polymer =
    windowed(2, partialWindows = true) { window -> //(pre, post) ->
        if (window.size == 1) {
            return@windowed window
        }

        val (pre, post) = window

        rules[Pair(pre, post)]
            ?.let {
                listOf(pre, it)
            }
            ?: listOf(pre)
    }
        .flatten()

private fun process(fileName: String) {
    println("Processing $fileName")
    val (template, rules) = readInput(fileName)

    val output = List(10) { it }
        .fold(template) { acc, idx ->
            acc.applyRules(rules).also {
                println("$idx > ${it.size}")
            }
        }
    val charCounts = output.groupBy { it }
        .mapValues { (_, characters) -> characters.size }
        .toList()
        .sortedBy { (_, count) -> count }

    val first = charCounts.first()
    val last = charCounts.last()
    val score = last.second - first.second

    println("First: $first")
    println("Last : $last")
    println("CharCounts: $charCounts")
    println("Score: $score")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
