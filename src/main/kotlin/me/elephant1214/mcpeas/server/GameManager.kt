package me.elephant1214.mcpeas.server

import me.elephant1214.mcpeas.world.World
import me.elephant1214.mcpeas.world.block.registerBlocks
import me.elephant1214.mcpeas.world.entity.PlayerEntity
import me.elephant1214.mcpeas.world.entity.registerEntityTypes
import me.elephant1214.mcpeas.world.generation.FlatGenerator
import kotlin.properties.Delegates.notNull

object GameManager {
    val players = ArrayList<PlayerEntity>()
    var mainWorld: World by notNull()
        private set

    internal fun init() {
        registerBlocks()
        registerEntityTypes()

        mainWorld = World(name = "Test", generator = FlatGenerator())
        println("World ${mainWorld.getName()} loaded")
    }
}
