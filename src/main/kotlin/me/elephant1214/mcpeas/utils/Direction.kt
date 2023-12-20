package me.elephant1214.mcpeas.utils

enum class Direction {
    NORTH, EAST, SOUTH, WEST;

    fun getOpposite(): Direction = when (this) {
        NORTH -> SOUTH
        EAST -> WEST
        SOUTH -> NORTH
        WEST -> EAST
    }
}
