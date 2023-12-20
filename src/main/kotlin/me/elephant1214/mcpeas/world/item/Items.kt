package me.elephant1214.mcpeas.world.item

import me.elephant1214.mcpeas.registry.ITEM_REGISTRY
import me.elephant1214.mcpeas.world.GameElementInfo

/**
 * Don't actually know what these are yet
 */
fun registerItems() {
    ITEM_REGISTRY.register(GameElementInfo(0, ""), Item())
}
