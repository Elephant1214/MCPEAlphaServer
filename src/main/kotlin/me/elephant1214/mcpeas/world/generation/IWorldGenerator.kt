package me.elephant1214.mcpeas.world.generation

interface IWorldGenerator {
    fun generateChunkColumn(chunkKey: Long)
}