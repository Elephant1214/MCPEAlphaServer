package me.elephant1214.mcpeas.world.block

import me.elephant1214.mcpeas.utils.Vector3i

open class Block(
    val hasCollision: Boolean = true,
    val blastResistance: Float = 0F,
    val hardness: Float = 0F, // Also destroy time
    val frictionCoefficient: Float = 0F,
    val transparent: Boolean = false,
    val flammable: Boolean = false,
    val flammability: Float = 0F,
    val isReplaceable: Boolean = false,
    val isLiquid: Boolean = false,
    val defaultLightOutput: Int = 0
) {
    val position: Vector3i? = null
}
