@file:Suppress("MagicNumber")

package me.elephant1214.mcpeas.world.block

import me.elephant1214.mcpeas.world.GameElementInfo
import me.elephant1214.mcpeas.registry.BLOCK_REGISTRY

fun registerBlocks() {
    BLOCK_REGISTRY.register(GameElementInfo(0, "air"), Block(hasCollision = false, isReplaceable = true))
    BLOCK_REGISTRY.register(GameElementInfo(1, "stone"), Block(blastResistance = 30F, hardness = 1.5F))
    BLOCK_REGISTRY.register(GameElementInfo(2, "grass"), Block(blastResistance = 3F, hardness = 0.6F))
    BLOCK_REGISTRY.register(GameElementInfo(3, "dirt"), Block(blastResistance = 2.5F, hardness = 0.5F))
    BLOCK_REGISTRY.register(GameElementInfo(4, "cobblestone"), Block(blastResistance = 6.0F, hardness = 2.0F))
    BLOCK_REGISTRY.register(GameElementInfo(5, "planks"), Block())
    BLOCK_REGISTRY.register(GameElementInfo(6, "sapling"), Block())
    BLOCK_REGISTRY.register(GameElementInfo(7, "bedrock"), Block(blastResistance = 18000000F, hardness = 60000F))
}
