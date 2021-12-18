package com.transpontine.aoc2021.day13

import java.lang.Integer.max

private fun process(fileName: String) {
    println("Processing $fileName")
    val (sheet, folds) = readInput(fileName)
    // println("Sheet:\n${sheet}")
    println("Start sheet: ${sheet.dimX} x ${sheet.dimY}")
    println("Folds:\n${folds.joinToString("\n")}")
    val result = folds.fold(sheet) { workSheet, fold ->
        workSheet.performFold(fold).also {
            if (max(it.dimX, it.dimY) > 60) {
                println("FOLDED $fold -> ${it.dimX} x ${it.dimY}\n")
            } else {
                println("FOLDED $fold\n$it\n")
            }
        }
    }
    println("Folded sheet:\n$result")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
