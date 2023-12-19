package me.elephant1214.mcpeas.network.packet

import me.elephant1214.mcpeas.network.NetworkHandler
import me.elephant1214.mcpeas.utils.ByteBufferUtil

abstract class Packet(
    val id: Byte,
    var size: Int = NetworkHandler.getMtuSize().toInt()
) {
    open fun read(bufUtil: ByteBufferUtil) {
        bufUtil.readByte() // Reads the packet ID
    }

    open fun write(bufUtil: ByteBufferUtil) {
        bufUtil.writeByte(id) // Writes the packet ID
    }

    companion object {
        /**
         * Uses the first byte of an incoming unconnected packet to figure out what type
         * of packet it is. Commented out cases shouldn't be possible to receive and if
         * they get received, then something really weird is happening with the client.
         */
        fun unconnectedPacketOf(id: Byte): Packet {
            return when (id) {
                0x01.toByte() -> C2SUnconnectedPingPacket()
                // 0x1C.toByte() -> ClientboundUnconnectedPongPacket()
                0x05.toByte() -> C2SOpenConnectionRequest1Packet()
                // 0x06.toByte() -> ClientboundOpenConnectionReply1Packet()
                0x07.toByte() -> C2SOpenConnectionRequest2Packet()
                // 0x08.toByte() -> ClientBoundOpenConnectionReply2Packet()
                0x1C.toByte(), 0x06.toByte(), 0x08.toByte() -> throw IllegalArgumentException("A client sent a clientbound packet???")
                else -> throw IllegalArgumentException("$id is not an unconnected packet ID")
            }
        }

        fun dataPacketOf(id: Byte): Packet? {
            return when (id) {
                0x84.toByte() -> C2SReadyPacket()
                else -> {
                    println("Unhandled ${id.toInt()}")
                    return null
                }
            }
        }
    }
}