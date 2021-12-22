package com.transpontine.aoc2021.day16


enum class PackageType(val typeId: Int) {
    SUM(0),
    PRODUCT(1),
    MINIMUM(2),
    MAXIMUM(3),
    LITERAL(4),
    GREATER_THAN(5),
    LESS_THAN(6),
    EQUAL(7),
    ;

    companion object {
        fun build(value: Int) =
            values().firstOrNull { it.typeId == value }
    }
}


enum class Bit(val char: Char, val int: Int) {
    ZERO('0', 0),
    ONE('1', 1);

    companion object {
        fun build(value: Int) = ZERO.takeIf { value == 0 } ?: ONE
    }
}

fun Sequence<Bit>.toInt() =
    fold(0) { acc, bit ->
        (acc shl 1) + bit.int
    }


fun generateBitSequence(hexChars: String): Sequence<Bit> = sequence {
    hexChars.forEach { hexChar ->
        val hex = hexChar.toString().toInt(16)
        yield(Bit.build(hex and 8))
        yield(Bit.build(hex and 4))
        yield(Bit.build(hex and 2))
        yield(Bit.build(hex and 1))
    }
}
