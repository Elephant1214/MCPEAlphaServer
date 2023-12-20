package me.elephant1214.mcpeas.server

import me.elephant1214.mcpeas.network.NetworkHandler
import kotlin.random.Random

object Server {
    const val SERVER_SECURITY: Byte = 0 // Always a byte value of 0 (false)
    const val MAXIMUM_MTU_SIZE: Short = 1500
    const val GAME_VERSION = "0.14.3"

    val settings = ServerSettings()
    private var hasCrashed = false
    val START_TIME = System.currentTimeMillis()
    private val random = Random.Default
    val GUID = random.nextLong()

    fun start() {
        println("Starting network handler")
        NetworkHandler.init()
        println("Starting game handler")
        GameManager.init()

        serverLoop()
    }

    private fun serverLoop() {
        while (!shouldStop()) {
            NetworkHandler.handleIncoming()
        }
    }

    private fun shouldStop(): Boolean {
        return hasCrashed // Always false for now, will actually do something eventually
    }

    fun timeSinceStart(): Long = System.currentTimeMillis() - START_TIME

    /**
     * The maximum MTU size is 1500, which should be fine on most connections.
     */
    data class ServerSettings(
        val mtuSize: Short = MAXIMUM_MTU_SIZE, val port: Int = 19132, val playerLimit: Int = 20
    )
}
