package me.elephant1214.mcpeas.world.entity

import me.elephant1214.mcpeas.registry.ENTITY_TYPE_REGISTRY
import me.elephant1214.mcpeas.world.GameElementInfo

fun registerEntityTypes() {
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(0, "player"), EntityType())

    ENTITY_TYPE_REGISTRY.register(GameElementInfo(10, "chicken"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(11, "cow"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(12, "pig"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(13, "sheep"), EntityType())

    ENTITY_TYPE_REGISTRY.register(GameElementInfo(32, "zombie"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(33, "creeper"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(34, "skeleton"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(35, "spider"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(36, "pig_zombie"), EntityType())

    ENTITY_TYPE_REGISTRY.register(GameElementInfo(64, "item"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(65, "primed_tnt"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(66, "falling_block"), EntityType())

    ENTITY_TYPE_REGISTRY.register(GameElementInfo(80, "arrow"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(81, "snowball"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(82, "egg"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(83, "painting"), EntityType())
    ENTITY_TYPE_REGISTRY.register(GameElementInfo(84, "minecart"), EntityType())
}