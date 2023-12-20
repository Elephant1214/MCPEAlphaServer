package me.elephant1214.mcpeas.network

import me.elephant1214.mcpeas.server.Server
import me.elephant1214.mcpeas.utils.ByteBufferUtil

abstract class Packet(
    val id: Byte,
    var size: Int = Server.settings.mtuSize.toInt()
) {
    open fun read(bufUtil: ByteBufferUtil) {
        bufUtil.readByte() // Reads the packet ID
    }

    open fun write(bufUtil: ByteBufferUtil) {
        bufUtil.writeByte(id) // Writes the packet ID
    }
}
