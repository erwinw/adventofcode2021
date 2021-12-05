package com.transpontine.aoc2021.day05

import java.lang.Integer.max
import java.lang.Integer.min
import kotlin.math.abs

class Line(val start: Point, val finish: Point) {
    private val horizontal = start.x == finish.x
    private val vertical = start.y == finish.y

    private val minX = min(start.x, finish.x)
    private val maxX = max(start.x, finish.x)
    private val minY = min(start.y, finish.y)
    private val maxY = max(start.y, finish.y)

    private val points: Set<Point> =
        if (horizontal || vertical) {
            emptySet()
        } else {
            val dx = if (start.x < finish.x) 1 else -1
            val dy = if (start.y < finish.y) 1 else -1
            val len = abs(maxY - minY)

            (0..len)
                .map {
                    Point(start.x + it * dx, start.y + it * dy)
                }
                .toSet()
        }

    fun crosses(point: Point) =
        when {
            horizontal ->
                start.x == point.x && minY <= point.y && maxY >= point.y
            vertical ->
                start.y == point.y && minX <= point.x && maxX >= point.x
            else -> false
        }

    fun crossesDiagonal(point: Point) =
        when {
            horizontal ->
                start.x == point.x && minY <= point.y && maxY >= point.y
            vertical ->
                start.y == point.y && minX <= point.x && maxX >= point.x
            else -> {
                point in points
            }
        }

    override fun toString() = "$start -> $finish"

    companion object {
        fun parse(input: String) =
            input.split(" -> ")
                .map { Point.parse(it) }
                .let { (start, finish) ->
                    Line(start, finish)
                }
    }
}