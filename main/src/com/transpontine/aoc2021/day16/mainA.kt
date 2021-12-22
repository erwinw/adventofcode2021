package com.transpontine.aoc2021.day16

import java.io.File

private fun sumVersionNumbers(rawInput: Sequence<Bit>): Int {
    var input = rawInput
    var totalVersion = 0
    var taken = 0

    fun take(n: Int): Int {
        taken += n
        val result = input.take(n).toInt()
        input = input.drop(n)
        return result
    }
    var parsePackage: () -> Unit = {}

    fun parseLiteral() {
        println("Literal")
        var literalChunk: Int
        do {
            literalChunk = take(5)
        } while ((literalChunk and 16) != 0)
    }

    fun parseFixedLengthOperator() {
        val length = take(15)
        println("Fixed length $length")
        // operand value(s); skip right past for now
        val lengthBefore = taken
        while (taken < lengthBefore + length) {
            parsePackage()
        }
        // take(length)
    }

    fun parseFixedCountOperator() {
        val subPacketCount = take(11)
        // operand value(s); skip right past for now
        println("Fixed count: $subPacketCount")
        repeat(subPacketCount) {
            parsePackage()
        }
    }

    parsePackage = {
        val version = take(3)
        totalVersion += version
        val typeId = take(3)
        println("Version $version, typeId $typeId")

        when (PackageType.build(typeId)) {
            PackageType.LITERAL -> parseLiteral()
            else -> {
                when (take(1)) {
                    0 -> parseFixedLengthOperator()
                    1 -> parseFixedCountOperator()
                }
            }
        }
    }

    while (!input.none()) {
        parsePackage()
    }

    return totalVersion
}

private fun process(fileName: String) {
    println("Processing $fileName")
    val input = File(fileName).readLines().first()

    println("Starting:")
    // Mine: 110100101111111000101000
    // His : 110100101111111000101000
    // caseA: D2FE28 -> 6
    // caseB: 8A004A801A8002F478 -> 16
    // caseC: 620080001611562C8802118E34 -> 12
    // caseD: C0015000016115A2E0802F182340 -> 23
    val score = sumVersionNumbers(generateBitSequence(input))
    println("Score: $score")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
