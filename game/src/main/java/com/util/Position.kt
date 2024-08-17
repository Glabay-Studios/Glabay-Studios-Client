package com.util

data class Position(val x : Int,val y : Int, val z : Int = 0) {

    operator fun plus(other: Position): Position {
        return Position(x + other.x, y + other.y)
    }

    override fun toString(): String {
        return "Position(x=$x, y=$y, z=$z)"
    }

}