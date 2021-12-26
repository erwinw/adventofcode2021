package com.transpontine.aoc2021.day18

import java.io.File

fun Any?.hash(): String = Integer.toHexString(this.hashCode())

interface SnailFishNumber {
    var parent: SnailFishComplexNumber?

    operator fun plus(nextNumber: SnailFishNumber) =
        SnailFishComplexNumber(parent, this, nextNumber)
            .also {
                it.left.parent = it
                it.right.parent = it
            }
            .reduce()

    fun reduce(once: Boolean = false): SnailFishNumber
    fun toComplexString(): String
    fun toPlainString(): String
    fun magnitude(): Int
}

class SnailFishDigitNumber(
    override var parent: SnailFishComplexNumber?,
    var digit: Int,
) : SnailFishNumber {
    override fun toString() = toPlainString()
    override fun toComplexString() = """<${hash()} ${digit}>"""
    override fun toPlainString() = digit.toString()

    override fun reduce(once: Boolean) = this

    fun split() = SnailFishComplexNumber(
        parent,
        left = SnailFishDigitNumber(parent, digit / 2),
        right = SnailFishDigitNumber(parent, digit - digit / 2),
    ).also {
        it.left.parent = it
        it.right.parent = it
    }

    override fun magnitude() = digit
}

class SnailFishComplexNumber(
    override var parent: SnailFishComplexNumber?,
    var left: SnailFishNumber,
    var right: SnailFishNumber,
) : SnailFishNumber {
    override fun toString() = toPlainString()
    override fun toComplexString() =
        "${hash()} (↑ ${parent.hash()}) [${left.toComplexString()},${right.toComplexString()}]"

    override fun toPlainString() = "[${left.toPlainString()},${right.toPlainString()}]"

    override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()

    override fun reduce(once: Boolean): SnailFishNumber {
        var reduced: Boolean
        var before = this.toPlainString()
        var i = 0
        do {
            // println("Reducing [$i]...\n$this")
            reduced = reduceExplode() || reduceSplit()
            if (reduced) {
                i += 1
                this.toPlainString().let {
                    if (it == before) {
                        throw Exception("LOOP!")
                    }
                    before = it
                }
            }
        } while (!once && reduced)

        return this
    }

    private fun explodeLeftDigit() {
        // until we find a `left`, go up the tree
        var leftParent: SnailFishNumber? = parent?.left ?: return
        var prevParent: SnailFishNumber? = this
        while (leftParent == prevParent) {
            prevParent = prevParent?.parent
            leftParent = prevParent?.parent?.left
        }
        // until we find a digit, go down the tree towards the right
        if (leftParent != null) {
            var rightDigit = leftParent
            while (rightDigit != null && rightDigit is SnailFishComplexNumber) {
                rightDigit = rightDigit.right
            }
            if (rightDigit is SnailFishDigitNumber) {
                val sourceDigit = rightDigit.digit
                rightDigit.digit += (left as SnailFishDigitNumber).digit
                // println("Explode left, adding $sourceDigit + ${left.toPlainString()} ==> ${rightDigit.digit}")
            }
        }
    }

    private fun explodeRightDigit() {
        // until we find a `right`, go up the tree
        var rightParent: SnailFishNumber? = parent?.right ?: return
        var prevParent: SnailFishNumber? = this
        while (rightParent == prevParent) {
            prevParent = prevParent?.parent
            rightParent = prevParent?.parent?.right
        }
        // println("Finding right or parent: $this -> $rightParent")
        // until we find a digit, go down the tree towards the left
        if (rightParent != null) {
            var leftDigit = rightParent
            while (leftDigit != null && leftDigit is SnailFishComplexNumber) {
                leftDigit = leftDigit.left
            }
            if (leftDigit is SnailFishDigitNumber) {
                val sourceDigit = leftDigit.digit
                leftDigit.digit += (right as SnailFishDigitNumber).digit
                // println("Explode right, adding $sourceDigit + ${right.toPlainString()} ==> ${leftDigit.digit}")
            }
        }
    }

    private fun reduceExplode(): Boolean {
        fun SnailFishComplexNumber.recurseFindTarget(levelRemaining: Int): SnailFishComplexNumber? {
            // println("recurseFindTarget $levelRemaining $this")
            if (levelRemaining == 0) {
                // println("Ding-dong")
                return this
            }

            return (left as? SnailFishComplexNumber)?.recurseFindTarget(levelRemaining - 1)
                ?: (right as? SnailFishComplexNumber)?.recurseFindTarget(levelRemaining - 1)
        }

        val target = recurseFindTarget(4) ?: return false
        // println("Exploding $target")

        // left
        target.explodeLeftDigit()
        target.explodeRightDigit()

        // number
        val targetParent = target.parent
        when {
            targetParent == null -> {
                println("NO PARENT?")
            }
            targetParent.left == target ->
                targetParent.left = SnailFishDigitNumber(targetParent, 0)
            targetParent.right == target ->
                targetParent.right = SnailFishDigitNumber(targetParent, 0)
        }

        return true
    }

    private fun reduceSplit(): Boolean {
        var localPart = left
        when {
            localPart is SnailFishDigitNumber ->
                if (localPart.digit > 9) {
                    left = localPart.split()
                    return true
                }

            localPart is SnailFishComplexNumber && localPart.reduceSplit() ->
                return true
        }

        localPart = right
        when {
            localPart is SnailFishDigitNumber ->
                if (localPart.digit > 9) {
                    right = localPart.split()
                    return true
                }

            localPart is SnailFishComplexNumber && localPart.reduceSplit() ->
                return true
        }

        return false
    }
}

fun parseComplexNumber(
    numberString: String,
): Pair<SnailFishComplexNumber, String> {
    var left: SnailFishNumber
    var right: SnailFishNumber
    var remainder = numberString
    parseSnailFishNumber(remainder).let {
        left = it.first
        remainder = it.second
    }
    if (remainder.first() != ',') {
        throw Exception("Unexpected input; expected ','. Remainder: '$remainder'")
    }
    remainder = remainder.drop(1)
    parseSnailFishNumber(remainder).let {
        right = it.first
        remainder = it.second
    }
    val result = SnailFishComplexNumber(null, left, right)
    result.left.parent = result
    result.right.parent = result
    return result to remainder
}

fun parseSnailFishNumber(numberString: String): Pair<SnailFishNumber, String> {
    // either it's a digit, or a complex number
    val lookAhead = numberString.first()
    var remainder = numberString.drop(1)
    when {
        lookAhead == '[' -> {
            var result: SnailFishComplexNumber
            parseComplexNumber(remainder).let {
                result = it.first
                if (it.second.firstOrNull() != ']') {
                    throw Exception("Unexpected input; expected ']'. Remainder: '$remainder'")
                }
                remainder = it.second.drop(1)
            }
            return result to remainder
        }

        lookAhead.isDigit() -> {
            val digit = SnailFishDigitNumber(null, lookAhead.digitToInt())
            return digit to remainder
        }

        else -> throw Exception("Unexpected input; expected '[' or digit, got '$lookAhead'")
    }
}

fun readInput(fileName: String): List<SnailFishNumber> =
    File(fileName).readLines().map { parseSnailFishNumber(it) }
        .map { (number, _) -> number }
