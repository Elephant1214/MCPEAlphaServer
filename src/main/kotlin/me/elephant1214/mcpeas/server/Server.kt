package me.elephant1214.mcpeas.server

import me.elephant1214.mcpeas.network.NetworkHandler
import kotlin.random.Random

class Server(val serverSettings: ServerSettings = ServerSettings()) {
    private var running = false

    fun start() {
        instance = this

        println("Starting network handler")
        networkHandler = NetworkHandler(serverSettings.port, serverSettings.playerLimit)
        println("Starting game handler")
        gameManager = GameManager()

        running = true
        serverLoop()
    }

    private fun serverLoop() {
        while (running && !shouldStop()) {
            networkHandler.handleIncoming()
        }
    }

    private fun shouldStop(): Boolean {
        return false // Always false for now
    }

    /**
     * The maximum MTU size is 1500, which should be fine on most connections.
     */
    data class ServerSettings(
        val mtuSize: Short = MAXIMUM_MTU_SIZE, val port: Int = 19132, val playerLimit: Int = 20
    )

    companion object {
        // Always a byte value of 0 (false)
        const val SERVER_SECURITY: Byte = 0
        const val MAXIMUM_MTU_SIZE: Short = 1500

        const val PROTOCOL_VERSION: Byte = 70
        const val GAME_VERSION = "0.14.3"

        val START_TIME = System.currentTimeMillis()
        private val random = Random.Default
        val GUID = random.nextLong()

        lateinit var instance: Server
            private set
        lateinit var networkHandler: NetworkHandler
            private set
        lateinit var gameManager: GameManager
            private set

        fun timeSinceStart(): Long = System.currentTimeMillis() - START_TIME
    }
}