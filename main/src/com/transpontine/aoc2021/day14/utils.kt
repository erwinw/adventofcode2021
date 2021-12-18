package com.transpontine.aoc2021.day14

import java.io.File

typealias PolymerA = List<String>
typealias PolymerB = Map<String, Long>
typealias RuleInputA = Pair<String, String>
typealias RulesA = Map<RuleInputA, String>
typealias RulesB = Map<String, Char>

fun readInputA(fileName: String): Pair<PolymerA, RulesA> {
    val lines = File(fileName).readLines()

    val template = lines.first().map { it.toString() }

    val rules = lines.drop(2)
        .associate { line ->
            val (pre, post) = line.take(2).map { it.toString() }
            Pair(Pair(pre, post), line.takeLast(1))
        }

    return Pair(template, rules)
}

fun readInputB(fileName: String): Triple<PolymerB, RulesB, Char> {
    val lines = File(fileName).readLines()

    val templateLine = lines.first()
    val template = templateLine
        .windowed(2)
        .groupingBy { it }
        .eachCount().mapValues { it.value.toLong() }

    val rules = lines.drop(2)
        .associate { line ->
            Pair(line.substring(0..1), line[6])
        }

    return Triple(template, rules, templateLine.last())
}

