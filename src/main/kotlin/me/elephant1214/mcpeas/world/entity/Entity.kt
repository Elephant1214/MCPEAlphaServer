package me.elephant1214.mcpeas.world.entity

import me.elephant1214.mcpeas.utils.Location
import me.elephant1214.mcpeas.world.WorldProvider
import kotlin.properties.Delegates.notNull

open class Entity(val entityType: EntityType) {
    var entityID by notNull<Int>()
    var world by notNull<WorldProvider>()
    var location by notNull<Location>()

    fun spawn(entityID: Int, world: WorldProvider, location: Location) {

    }
}