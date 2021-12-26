package com.transpontine.aoc2021.day18

private fun List<SnailFishNumber>.sum() =
    reduce(SnailFishNumber::plus)

private fun process(fileName: String) {
    println("Processing $fileName")
    val numbers = readInput(fileName)

    println("INPUT: ${numbers.first().toPlainString()}")
    numbers.forEachIndexed { index, snailFishNumber -> println("> $index: $snailFishNumber") }

    var biggestMagnitude = -1
    var perms = 0
    numbers.forEach { itA ->
        (numbers - itA).forEach { itB ->
            println(" A: $itA  +B: $itB")
            (itA + itB).let { sum ->
                perms += 1
                val mag = sum.magnitude()
                println(" A: $itA  +B: $itB  = : $sum\n=> $mag")
                if (mag > biggestMagnitude) {
                    println("New candidate! $mag")
                    biggestMagnitude = mag
                }
            }

            println(" B: $itB  +A: $itA")
            (itB + itA).let { sum ->
                perms += 1
                val mag = sum.magnitude()
                println(" B: $itB  +A: $itA  = : $sum\n=> $mag")
                if (mag > biggestMagnitude) {
                    println("New candidate! $mag")
                    biggestMagnitude = mag
                }
            }
        }
    }
    println("Tried perms: $perms")

    println("Biggest Magnitude:\n$biggestMagnitude")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
