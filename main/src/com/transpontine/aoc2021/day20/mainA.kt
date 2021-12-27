package com.transpontine.aoc2021.day20

private fun process(fileName: String) {
    val (algorithm, inputImage) = readInput(fileName)
    println("Algorithm:\n$algorithm")
    println("Image:\n$inputImage")

    var result = inputImage
    var defaultBit = Bit.ZERO
    repeat(2) {
        result = algorithm.apply(result, defaultBit)

        println("Result[$it]:\n$result\n---\n${result.lit}\n${result.width} x ${result.height}\n")

        defaultBit =
            if (defaultBit == Bit.ZERO) algorithm.first() else algorithm.last()
    }
    println("Result final:\n${result.lit}")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
