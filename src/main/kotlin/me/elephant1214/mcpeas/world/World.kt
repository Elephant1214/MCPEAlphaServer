package me.elephant1214.mcpeas.world

import me.elephant1214.mcpeas.utils.Location
import me.elephant1214.mcpeas.world.entity.Entity
import me.elephant1214.mcpeas.world.generation.WorldGenerator
import java.util.*

class World(
    private val id: UUID = UUID.randomUUID(), private val name: String, val generator: WorldGenerator
) : WorldProvider {
    private val entities = ArrayList<Entity>()
    private var time = 0

    init {
        println("Initializing world $name")
    }

    override fun tick() {
        time++
    }

    override fun generateChunkColumn(chunkKey: Long) {
        generator.generateChunkColumn(chunkKey)
    }

    override fun getSpawnPoint(): Location = Location()

    override fun addEntity(entity: Entity) {
        this.entities.add(entity)
    }

    override fun getEntityLocation(entity: Entity): Optional<Location> {
        val found = this.entities.find { it == entity }
        return if (found == null) {
            Optional.empty()
        } else {
            Optional.of(entity.location)
        }
    }

    override fun getTime(): Int = time

    override fun getID(): UUID = id

    override fun getName(): String = name
}
