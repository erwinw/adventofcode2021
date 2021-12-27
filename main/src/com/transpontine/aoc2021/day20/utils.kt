package com.transpontine.aoc2021.day20

import java.io.File

enum class Bit(val char: Char, val bitChar: Char) {
    ZERO('.', '0'),
    ONE('#', '1'),
    ;

    override fun toString() = char.toString()
}

fun String.toBits() = map { Bit.values().first { bit -> bit.char == it } }

class Image
private constructor(
    private val bits: MutableList<MutableList<Bit>>,
    @Suppress("UNUSED_PARAMETER") _hackToGetDifferentSignature: Boolean,
) {
    val width = bits.first().size
    val height = bits.size

    constructor(width: Int, height: Int) : this(
        MutableList(height) { MutableList(width) { Bit.ZERO } },
        true,
    )

    constructor(input: List<String>) : this(
        input
            .map { it.toBits().toMutableList() }
            .toMutableList(),
        true
    )

    val lit get() = bits.sumOf { row -> row.count { it == Bit.ONE } }

    internal operator fun set(x: Int, y: Int, bit: Bit) {
        bits[y][x] = bit
    }

    private fun readBit(x: Int, y: Int, defaultValue: Bit): Bit =
        bits.getOrNull(y)?.getOrNull(x) ?: defaultValue

    fun readPatch(x: Int, y: Int, defaultValue: Bit): Int =
        (y - 1..y + 1).flatMap { iy: Int ->
            (x - 1..x + 1).map { ix: Int ->
                readBit(ix, iy, defaultValue).bitChar
            }
        }.joinToString(separator = "")
            .toInt(2)

    override fun toString() =
        bits.joinToString("\n") {
            it.joinToString("")
                .chunked(5).joinToString(" ")
        }
}

class Algorithm(input: String) {
    private val bits = input.toBits()
    fun first() = bits.first()
    fun last() = bits.last()

    fun apply(input: Image, defaultBit: Bit): Image =
        Image(width = input.width + 2, height = input.height + 2)
            .also { output ->
                for (iy in -1..input.height) {
                    for (ix in -1..input.width) {
                        output[ix + 1, iy + 1] = bits[input.readPatch(x = ix, y = iy, defaultBit)]
                    }
                }
            }

    override fun toString() = bits.joinToString("")
        .chunked(80)
        .joinToString("\n") {
            it.chunked(5).joinToString(" ")
        }
}

fun readInput(fileName: String): Pair<Algorithm, Image> =
    File(fileName).readLines().let { lines ->
        Pair(
            Algorithm(lines.first()),
            Image(lines.drop(2)),
        )
    }
