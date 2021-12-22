package com.transpontine.aoc2021.day17

import java.lang.Integer.max

private var abortCount = 0

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
    val maxInitialVelocity = target.x.maxOf { it }
    var hitCount = 0
    for (y in -2 * maxInitialVelocity until maxInitialVelocity * 2) {
        for (x in 0 until maxInitialVelocity*2) {
            val velocity = Velocity(x, y)
            val score = calculateScore(velocity, target)
            if (score != null) {
                hitCount +=1
            }
        }
    }
    println("Abort count: $abortCount")
    println("Hit count: $hitCount")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
