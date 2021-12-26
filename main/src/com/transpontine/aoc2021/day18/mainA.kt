package com.transpontine.aoc2021.day18

private fun List<SnailFishNumber>.sum() =
    reduce(SnailFishNumber::plus)

private fun test() {
    println("Tests")
    val testExplodeCases = listOf(
        "[[[[[9,8],1],2],3],4]" to "[[[[0,9],2],3],4]",
        "[7,[6,[5,[4,[3,2]]]]]" to "[7,[6,[5,[7,0]]]]",
        "[[6,[5,[4,[3,2]]]],1]" to "[[6,[5,[7,0]]],3]",
        "[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]" to "[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]",
        "[[[[0,7],4],[[7,8],[0,[6,7]]]],[1,1]]" to "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
    )
    testExplodeCases.forEach { (input, expected) ->
        val actual = parseSnailFishNumber(input).first.reduce(true).toPlainString()
        assert(actual == expected) { "Exploded single failed" }
    }

    val testSumCases = listOf(
        listOf("[[[[4,3],4],4],[7,[[8,4],9]]]", "[1,1]") to "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]",
        listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]") to "[[[[1,1],[2,2]],[3,3]],[4,4]]",
        listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]") to "[[[[3,0],[5,3]],[4,4]],[5,5]]",
        listOf("[1,1]", "[2,2]", "[3,3]", "[4,4]", "[5,5]", "[6,6]") to "[[[[5,0],[7,4]],[5,5]],[6,6]]",

        listOf(
            "[[[0,[4,5]],[0,0]],[[[4,5],[2,6]],[9,5]]]",
            "[7,[[[3,7],[4,3]],[[6,3],[8,8]]]]",
            "[[2,[[0,8],[3,4]]],[[[6,7],1],[7,[1,6]]]]",
            "[[[[2,4],7],[6,[0,5]]],[[[6,8],[2,8]],[[2,1],[4,5]]]]",
            "[7,[5,[[3,8],[1,4]]]]",
            "[[2,[2,2]],[8,[8,1]]]",
            "[2,9]",
            "[1,[[[9,3],9],[[9,0],[0,7]]]]",
            "[[[5,[7,4]],7],1]",
            "[[[[4,2],2],6],[8,7]]",
        ) to "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]",
    )
    testSumCases.forEach { (input, expected) ->
        val actual = input.map { parseSnailFishNumber(it).first }.sum().toPlainString()
        assert(actual == expected) { "Test sum failed; vvv expected:\n$expected\n$actual\n^^^ actual\n" }
    }

    val testMagnitudeCases = listOf(
        listOf("[9,1]") to 29,
        listOf("[[1,2],[[3,4],5]]") to 143,
        listOf("[[[[0,7],4],[[7,8],[6,0]]],[8,1]]") to 1384,
        listOf("[[[[1,1],[2,2]],[3,3]],[4,4]]") to 445,
        listOf("[[[[3,0],[5,3]],[4,4]],[5,5]]") to 791,
        listOf("[[[[5,0],[7,4]],[5,5]],[6,6]]") to 1137,
        listOf("[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]") to 3488,
    )
    testMagnitudeCases.forEach { (input, expected) ->
        val actual = input.map { parseSnailFishNumber(it).first }.sum().magnitude()
        assert(actual == expected) { "Test magnitude failed; vvv expected:\n$expected\n$actual\n^^^ actual\n" }
    }
}

private fun process(fileName: String) {
    test()

    println("Processing $fileName")
    val numbers = readInput(fileName)
    println("INPUT: ${numbers.first().toPlainString()}")
    numbers.forEachIndexed { index, snailFishNumber -> println("> $index: $snailFishNumber") }

    val result = numbers.sum()
    println("Got a result:\n$result")
    println("Magnitude:\n${result.magnitude()}")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
