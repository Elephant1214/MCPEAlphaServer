package me.elephant1214.mcpeas.world

import me.elephant1214.mcpeas.utils.Location
import me.elephant1214.mcpeas.world.entity.Entity
import java.util.*

interface WorldProvider {
    fun tick()

    fun generateChunkColumn(chunkKey: Long)

    fun getSpawnPoint(): Location

    fun addEntity(entity: Entity)

    fun getEntityLocation(entity: Entity): Optional<Location>

    fun getTime(): Int

    fun getID(): UUID

    fun getName(): String
}
