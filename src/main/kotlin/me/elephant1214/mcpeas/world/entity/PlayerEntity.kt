package me.elephant1214.mcpeas.world.entity

import me.elephant1214.mcpeas.registry.ENTITY_TYPE_REGISTRY
import me.elephant1214.mcpeas.server.ConnectedClient

class PlayerEntity(val client: ConnectedClient) : Entity(ENTITY_TYPE_REGISTRY.get("player").get()) {
    fun isClientOf(other: ConnectedClient): Boolean = client.clientID == other.clientID
}