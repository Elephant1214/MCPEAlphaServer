package me.elephant1214.mcpeas.world.entity

import me.elephant1214.mcpeas.utils.Location
import me.elephant1214.mcpeas.world.IWorldProvider
import me.elephant1214.mcpeas.world.entity.type.EntityType
import kotlin.properties.Delegates.notNull

open class Entity(val entityType: EntityType) {
    var entityID by notNull<Int>()
    var world by notNull<IWorldProvider>()
    var location by notNull<Location>()

    fun spawn(entityID: Int, world: IWorldProvider, location: Location) {

    }
}