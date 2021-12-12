package com.transpontine.aoc2021.day12

import java.io.File

private data class BNode(val name: String) {
    val isSmall = name.first().isLowerCase()

    val connections = mutableSetOf<BNode>()

    fun connect(neighbour: BNode) {
        connections.add(neighbour)
        neighbour.connections.add(this)
    }

    companion object {
        fun build(name: String): BNode {
            nodesByName[name]?.let { return it }

            return BNode(name).also {
                nodesByName[name] = it
            }
        }
    }
}

private val nodesByName: MutableMap<String, BNode> = mutableMapOf()

private lateinit var startNode: BNode
private lateinit var endNode: BNode

private class BPath private constructor() {
    constructor(firstNode: BNode) : this() {
        nodes.add(firstNode)
    }

    val nodes = mutableListOf<BNode>()

    val lastNode get() = nodes.last()
    val isFinished get() = lastNode == endNode

    fun visitedSmallTwice() =
        nodes.filter { it.isSmall }
            .groupBy { it }
            .values
            .any { it.size > 1 }

    fun clone() =
        BPath().also {
            it.nodes.addAll(nodes)
        }

    override fun toString() = nodes.joinToString(separator = " -> ") { it.name }
}

private fun buildPaths(startNode: BNode, endNode: BNode): List<BPath> {
    var paths: List<BPath> = listOf(BPath(startNode))

    do {
        val sizeBefore = paths.size
        paths = paths.flatMap { path ->
            if (path.isFinished) {
                return@flatMap listOf(path)
            }

            val visitedSmallTwice = path.visitedSmallTwice()

            path.lastNode
                .connections
                .filterNot { nextNode ->
                    nextNode == startNode ||
                        (visitedSmallTwice && nextNode.isSmall && nextNode in path.nodes)
                }
                .map { nextNode ->
                    path.clone().also {
                        it.nodes.add(nextNode)
                    }
                }
        }
    } while (paths.size != sizeBefore)
    println("\nPATHS:")
    paths.forEachIndexed { index, path -> println("> $index: $path") }
    println("\n")

    return paths
}

private fun process(fileName: String) {
    println("Processing $fileName")
    File(fileName)
        .readLines()
        .map { line ->
            val (startNode, endNode) = line.split('-').map(BNode::build)
            startNode.connect(endNode)
        }

    startNode = nodesByName["start"]!!
    endNode = nodesByName["end"]!!
    println("Have ${nodesByName.size} nodes")

    val paths = buildPaths(startNode, endNode)
    println("Have ${paths.size} paths")
}

fun main(args: Array<String>) {
    if (args.size != 1) {
        throw Error("Do not forget to pass in an input filename!")
    }
    process(args[0])
}
