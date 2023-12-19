package me.elephant1214.mcpeas.network.packet

import me.elephant1214.mcpeas.server.ConnectedClient
import me.elephant1214.mcpeas.utils.ByteBufferUtil
import me.elephant1214.mcpeas.utils.Location

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
