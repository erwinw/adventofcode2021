package com.transpontine.aoc2021.day17

import java.lang.Integer.MIN_VALUE
import java.lang.Integer.max

var abortCount = 0

private fun generateTrajectory(velocity: Velocity): Sequence<Point> = sequence {
    var step = 0
    val currentPosition = Point(0,0)
    val vel = velocity.copy()
    while (true) {
        step += 1
        currentPosition.add(vel)

        yield(currentPosition)

        vel.step()
    }
}

private fun calculateScore(velocity: Velocity, target: Ranges): Int? {
    val seq = generateTrajectory(velocity)
    val targetMinY = target.y.minOf { it }
    val targetMaxX = target.x.maxOf { it }
    var maxY = 0
    seq.forEachIndexed { index, point ->
        if (index > 999) {
            println("ABORT")
            abortCount += 1
            return null
        }
        maxY = max(maxY, point.y)
        // log("> $index, $point => $maxY")
        if (point in target) {
            println("HIT $velocity: $maxY")
            return maxY
        }
        if (point.y < targetMinY || point.x > targetMaxX) {
            return null
        }
    }
    return null
}


private fun process(fileName: String) {
    println("Processing $fileName")
    val target = readInput(fileName)
    val maxInitialVelocity = target.x.minOf { it }
    var winnerVelocity = Velocity(-1, -1)
    var winnerScore = MIN_VALUE
    for (y in 0 until maxInitialVelocity) {
        for (x in 0 until maxInitialVelocity) {
            val velocity = Velocity(x, y)
            val score = calculateScore(velocity, target)
            if (score != null && score > winnerScore) {
                println("New winner: $score -> $winnerScore; $velocity")
                winnerScore = score
                winnerVelocity = velocity
            }
        }
    }
    println("Abort count: $abortCount")
    println("New winner: $winnerScore; $winnerVelocity")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
