package com.transpontine.aoc2021.day16

import java.io.File

private fun calculate(rawInput: Sequence<Bit>): Long {
    var input = rawInput
    var taken = 0

    fun take(n: Int) =
        input.take(n)
            .toInt()
            .also {
                taken += n
                input = input.drop(n)
            }

    var parsePackage: () -> Long = { 0 }

    fun parseLiteral(): Long {
        var literalChunk: Int
        var literalValue: Long = 0
        do {
            literalChunk = take(5)
            literalValue = (literalValue shl 4) + (literalChunk and 15)
        } while ((literalChunk and 16) != 0)
        println("Literal $literalValue\n")
        return literalValue
    }

    fun applyOperator(operator: PackageType, operands: List<Long>) =
        when (operator) {
            PackageType.SUM -> operands.sum()
            PackageType.PRODUCT -> operands.fold(1L) { acc, value -> acc * value }
            PackageType.MINIMUM -> operands.minOf { it }
            PackageType.MAXIMUM -> operands.maxOf { it }
            PackageType.LITERAL -> operands[0]
            PackageType.GREATER_THAN -> if (operands[0] > operands[1]) 1 else 0
            PackageType.LESS_THAN -> if (operands[0] < operands[1]) 1 else 0
            PackageType.EQUAL -> if (operands[0] == operands[1]) 1 else 0
        }.also {
            println("Operator ${operator.name} result: $it")
        }

    fun parseFixedLengthOperator(packageType: PackageType): Long {
        val length = take(15)

        println("Fixed length $length")
        // operand value(s); skip right past for now
        val lengthBefore = taken
        val operands = buildList {
            while (taken < lengthBefore + length) {
                add(parsePackage())
            }
        }

        return applyOperator(packageType, operands)
    }

    fun parseFixedCountOperator(packageType: PackageType): Long {
        val subPacketCount = take(11)

        println("Fixed count: $subPacketCount")
        val operands = buildList {
            repeat(subPacketCount) {
                add(parsePackage())
            }
        }

        return applyOperator(packageType, operands)
    }

    parsePackage = {
        // Version
        take(3)
        val typeId = take(3)
        println("TypeId $typeId")

        when (val packageType = PackageType.build(typeId)) {
            PackageType.LITERAL -> parseLiteral()
            null -> throw Exception("Unsupported package type")
            else -> {
                when (take(1)) {
                    0 -> parseFixedLengthOperator(packageType)
                    1 -> parseFixedCountOperator(packageType)
                    else -> throw Exception("Unexpected operator type")
                }
            }
        }
    }

    return parsePackage()
}

fun calc(input: String, expected: Long?) {
    println("Calculating $input")
    val result = calculate(generateBitSequence(input))
    val matches = expected?.let {it == result} ?: true
    println("Result: $result${
        expected?.let {
            if (matches) {
                " matches expected ✅"
            } else {
                " does not match expected $expected ❌"
            }
        } ?: ""
    }\n")
    if (!matches) {
        throw Exception("nope")
    }
}

private fun process(fileName: String) {
    println("Processing $fileName")

    calc("C200B40A82", 3)
    calc("04005AC33890", 54)
    calc("880086C3E88112", 7)
    calc("CE00C43D881120", 9)
    calc("D8005AC2A8F0", 1)
    calc("F600BC2D8F", 0)
    calc("9C005AC2F8F0", 0)
    calc("9C0141080250320F1802104A08", 1)
    println("----")
    calc(File(fileName).readLines().first(), null)
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
