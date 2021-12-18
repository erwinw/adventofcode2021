package com.transpontine.aoc2021.day13

private fun process(fileName: String) {
    println("Processing $fileName")
    val (sheet, folds) = readInput(fileName)
    println("Sheet:\n${sheet}")
    println("Folds:\n${folds.joinToString("\n")}")
    val foldedSheet = sheet.performFold(folds.first())
    println("Folded sheet:\n$foldedSheet")
    println("Folded dot count:\n${foldedSheet.dotCount}")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
