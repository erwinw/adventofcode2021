package com.transpontine.aoc2021.day14

private fun MutableMap<String, Long>.plus(pre: Char, post: Char, amount: Long) {
    val key = pre.toString() + post
    this[key] = this.getOrDefault(key, 0L) + amount
}

private fun applyRules(polymer: PolymerB, rules: RulesB): PolymerB = buildMap {
    polymer.forEach { (pair, count) ->
        val inserted = rules.getValue(pair)
        plus(pair.first(), inserted, count)
        plus(inserted, pair.last(), count)
    }
}

private fun process(fileName: String) {
    println("Processing $fileName")
    val (template, rules, lastChar) = readInputB(fileName)

    val output =
        (0 until 40)
            .fold(template) { acc, _ ->
                applyRules(acc, rules).also {
                    println("> ${it.size}")
                }
            }

    println("Output: $output")

    val charCounts = output
        .map { it.key.first() to it.value }
        .groupBy({ it.first }, { it.second })
        .mapValues { it.value.sum() + if (it.key == lastChar) 1 else 0 }
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
