package com.transpontine.aoc2021.day07

import java.io.File
import kotlin.math.abs

private fun process(fileName: String) {
    println("Processing $fileName")
    val crabPositions = File(fileName)
        .readLines()
        .first()
        .split(',')
        .map { it.toShort() }
    println("Crab positions: $crabPositions")
    val crabSubs = crabPositions
        .groupBy { it }
        .mapValues { it.value.size }
        .toList()
    println("Crab subs: $crabSubs")

    val min = crabSubs.minOf { it.first }
    val max = crabSubs.maxOf { it.first }
    var optimumGoal: Short = (-1).toShort()
    var optimumFuel: Int? = null
    for(goal in min..max) {
        val fuel = crabSubs.sumOf { (position, count) -> abs(position - goal) * count }
        if (optimumFuel == null || fuel < optimumFuel) {
            optimumGoal = goal.toShort()
            optimumFuel = fuel
        }
        println("Goal/fuel [$goal, $fuel], [$optimumGoal, $optimumFuel]\n")
    }

    println("Optimum goal $optimumGoal, fuel $optimumFuel")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
