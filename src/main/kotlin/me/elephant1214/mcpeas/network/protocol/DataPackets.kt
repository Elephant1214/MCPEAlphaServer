@file:Suppress("MagicNumber")

package me.elephant1214.mcpeas.network.protocol

import me.elephant1214.mcpeas.network.Packet
import me.elephant1214.mcpeas.network.protocol.S2CLoginStatusPacket.LoginStatus.VERSIONS_MATCH
import me.elephant1214.mcpeas.server.ConnectedClient
import me.elephant1214.mcpeas.utils.ByteBufferUtil
import me.elephant1214.mcpeas.utils.Location

fun dataPacketOf(id: Byte): Packet? {
    return when (id) {
        0x84.toByte() -> C2SReadyPacket()
        else -> {
            println("Unhandled ${id.toInt()}")
            return null
        }
    }
}

class S2CLoginStatusPacket(
    var loginStatus: LoginStatus = VERSIONS_MATCH
) : Packet(0x83.toByte()) {
    override fun write(bufUtil: ByteBufferUtil) {
        super.write(bufUtil)
        bufUtil.writeInt(VERSIONS_MATCH.ordinal)
    }

    enum class LoginStatus {
        VERSIONS_MATCH,
        OUTDATED_SERVER,
        OUTDATED_CLIENT
    }
}

class C2SReadyPacket : ConnectedPacket(0x85.toByte()) {
    override fun handle(client: ConnectedClient) {
        for (player in client.gameManager.players) {
            if (!player.isClientOf(client)) {
                // client.sendPlayPacket()
            }
        }
    }
}

class S2CAddPlayerPacket(
    var playerID: Long, var username: String, var entityID: Int, var location: Location, var itemID: Short,
) : ConnectedPacket(0x89.toByte()) {
    override fun write(bufUtil: ByteBufferUtil) {
        super.write(bufUtil)
    }
}
