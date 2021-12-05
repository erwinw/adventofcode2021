package com.transpontine.aoc2021.day05

data class Point(val x: Int, val y: Int) {
    override fun toString() = "($x, $y)"

    companion object {
        fun parse(input: String) =
            input.split(',')
                .map { it.toInt() }
                .let { (x, y) -> Point(x, y)}
    }
}
