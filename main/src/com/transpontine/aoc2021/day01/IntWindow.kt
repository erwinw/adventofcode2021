package com.transpontine.aoc2021.day01

/** A circular buffer really */
class IntWindow(private val capacity: Int) {
    private val items = MutableList<Int?>(capacity) { null }
    private var nextIndex = 0

    fun add(item: Int) {
        items[nextIndex] = item
        nextIndex = (nextIndex + 1 ) % capacity
    }

    val sum get() = items[nextIndex]?.let { items.reduce { acc, curr -> acc!! + curr!! }}
}