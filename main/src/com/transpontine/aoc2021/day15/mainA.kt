package com.transpontine.aoc2021.day15

import java.util.PriorityQueue

private fun dijkstraTraversal(cave: Cave): Int {
    val target = Point(x = cave.maxX, y = cave.maxY)
    val todo = PriorityQueue<Traversal>()
    val visited = mutableSetOf<Point>()

    todo.add(Traversal(Point(0, 0), 0))
    while (todo.isNotEmpty()) {
        val candidate = todo.poll()
        if (candidate.point == target) {
            return candidate.totalRisk
        }
        visited.add(candidate.point)

        val neighbours = candidate.neighbours(cave)
            .filterNot { it.point in visited || it in todo}
        todo.addAll(neighbours)
    }

    throw Exception("No way out of cave found")
}

private fun process(fileName: String) {
    println("Processing $fileName")
    val cave: Cave = readInput(fileName)

    val solution = dijkstraTraversal(cave)
    println("Solution: $solution")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
