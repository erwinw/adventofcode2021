package com.transpontine.aoc2021.day12

import java.io.File

private data class ANode(val name: String) {
    val isSmall = name.first().isLowerCase()

    val connections = mutableSetOf<ANode>()

    fun connect(neighbour: ANode) {
        connections.add(neighbour)
        neighbour.connections.add(this)
    }

    companion object {
        fun build(name: String): ANode {
            nodesByName[name]?.let { return it }

            return ANode(name).also {
                nodesByName[name] = it
            }
        }
    }
}

private val nodesByName: MutableMap<String, ANode> = mutableMapOf()

private lateinit var startNode: ANode
private lateinit var endNode: ANode

private class APath private constructor() {
    constructor(firstNode: ANode) : this() {
        nodes.add(firstNode)
    }

    val nodes = mutableListOf<ANode>()

    val lastNode get() = nodes.last()
    val isFinished get() = lastNode == endNode

    fun clone() =
        APath().also {
            it.nodes.addAll(nodes)
        }

    override fun toString() = nodes.joinToString(separator = " -> ") { it.name }
}

private fun buildPaths(startNode: ANode, endNode: ANode): List<APath> {
    var paths: List<APath> = listOf(APath(startNode))

    do {
        val sizeBefore = paths.size
        paths = paths.flatMap { path ->
            if (path.isFinished) {
                return@flatMap listOf(path)
            }

            path.lastNode
                .connections
                .filterNot { nextNode ->
                    nextNode.isSmall && nextNode in path.nodes
                }
                .map { nextNode ->
                    path.clone().also {
                        it.nodes.add(nextNode)
                    }
                }
        }
        paths.forEachIndexed { index, path -> println("> $index: $path") }
    } while (paths.size != sizeBefore)

    return paths
}

private fun process(fileName: String) {
    println("Processing $fileName")
    File(fileName)
        .readLines()
        .map { line ->
            val (startNode, endNode) = line.split('-').map(ANode::build)
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
