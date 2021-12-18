package com.transpontine.aoc2021.day14

import java.io.File

typealias Polymer = List<String>
typealias RuleInput = Pair<String, String>
typealias Rules = Map<RuleInput, String>

fun readInput(fileName: String): Pair<Polymer, Rules> {
    val lines = File(fileName).readLines()

    val template = lines.first().map { it.toString() }

    val rules = lines.drop(2)
        .associate { line ->
            val (pre, post) = line.take(2).map { it.toString() }
            Pair(Pair(pre, post), line.takeLast(1))
        }

    return Pair(template, rules)
}
