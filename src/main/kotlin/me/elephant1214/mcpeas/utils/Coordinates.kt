package me.elephant1214.mcpeas.utils

class Location(
    private var x: Double,
    private var y: Double,
    private var z: Double,
    private var pitch: Double = 0.0,
    private var yaw: Double = 0.0
) {
    fun getDirection() {}

    companion object {
        @JvmStatic
        fun zero(): Location = Location(0.0, 0.0, 0.0)
    }
}

class ChunkLocation(private var x: Int, private var z: Int) {

}