@file:Suppress("MagicNumber")

package me.elephant1214.mcpeas.network.protocol

import me.elephant1214.mcpeas.network.NetworkHandler.PROTOCOL_VERSION
import me.elephant1214.mcpeas.network.NetworkHandler.RAKNET_OFFLINE_MESSAGE_DATA
import me.elephant1214.mcpeas.network.Packet
import me.elephant1214.mcpeas.server.Server
import me.elephant1214.mcpeas.server.Server.GUID
import me.elephant1214.mcpeas.server.Server.SERVER_SECURITY
import me.elephant1214.mcpeas.utils.ByteBufferUtil
import java.net.InetSocketAddress

/**
 * Uses the first byte of an incoming unconnected packet to figure out what type
 * of packet it is.
 * Commented out cases shouldn't be possible to receive and if
 * they are received, then something really weird is happening with the client.
 */
fun unconnectedPacketOf(id: Byte): Packet {
    return when (id) {
        0x01.toByte() -> C2SUnconnectedPingPacket()
        // 0x1C.toByte() -> ClientboundUnconnectedPongPacket()
        0x05.toByte() -> C2SOpenConnectionRequest1Packet()
        // 0x06.toByte() -> ClientboundOpenConnectionReply1Packet()
        0x07.toByte() -> C2SOpenConnectionRequest2Packet()
        // 0x08.toByte() -> ClientBoundOpenConnectionReply2Packet()
        0x1C.toByte(), 0x06.toByte(), 0x08.toByte() -> {
            throw IllegalArgumentException("A client sent a clientbound packet???")
        }

        else -> throw IllegalArgumentException("$id is not an unconnected packet ID")
    }
}

class C2SUnconnectedPingPacket(
    var timeSinceStart: Long = 0L
) : Packet(0x01) {
    override fun read(bufUtil: ByteBufferUtil) {
        super.read(bufUtil)
        timeSinceStart = bufUtil.readLong()
        bufUtil.skipMagic()
    }

    override fun toString(): String {
        return "C2SUnconnectedPingPacket(" +
                "${Server.timeSinceStart()}, ${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}" +
                ")"
    }
}

class S2CUnconnectedPongPacket(
    var data: String = ""
) : Packet(0x1C, 35 + data.toByteArray().size) {
    override fun write(bufUtil: ByteBufferUtil) {
        super.write(bufUtil)
        bufUtil.writeLong(Server.timeSinceStart())
        bufUtil.writeLong(GUID)
        bufUtil.writeMagic()
        bufUtil.writeString(data)
    }

    override fun toString(): String {
        return "S2CUnconnectedPongPacket(" +
                "${Server.timeSinceStart()}, $GUID, ${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}, $data" +
                ")"
    }
}

class C2SOpenConnectionRequest1Packet(
    var protocolVersion: Byte = 0
) : Packet(0x05) {
    override fun read(bufUtil: ByteBufferUtil) {
        super.read(bufUtil)
        bufUtil.skipMagic()
        protocolVersion = bufUtil.readByte()
    }

    override fun toString(): String {
        return "C2SOpenConnectionRequest1Packet(" +
                "${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}, $protocolVersion" +
                ")"
    }
}

class S2COpenConnectionReply1Packet : Packet(0x06, 28) {
    override fun write(bufUtil: ByteBufferUtil) {
        super.write(bufUtil)
        bufUtil.writeMagic()
        bufUtil.writeLong(GUID)
        bufUtil.writeByte(SERVER_SECURITY)
        bufUtil.writeShort(Server.settings.mtuSize)
    }

    override fun toString(): String {
        return "S2COpenConnectionReply1Packet(" +
                "${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}, $GUID, $SERVER_SECURITY, ${Server.settings.mtuSize}" +
                ")"
    }
}

class C2SOpenConnectionRequest2Packet(
    var clientSocket: InetSocketAddress? = null, var mtuSize: Short = 0, var clientID: Long = 0L
) : Packet(0x07) {
    override fun read(bufUtil: ByteBufferUtil) {
        super.read(bufUtil)
        bufUtil.skipMagic()
        clientSocket = bufUtil.readSocketAddress()
        mtuSize = bufUtil.readShort()
        clientID = bufUtil.readLong()
    }

    override fun toString(): String {
        return "C2SOpenConnectionRequest2Packet(" +
                "${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}, $clientSocket, $mtuSize, $clientID" +
                ")"
    }
}

class S2COpenConnectionReply2Packet(
    var clientPort: Short
) : Packet(0x08, 30) {
    override fun write(bufUtil: ByteBufferUtil) {
        super.write(bufUtil)
        bufUtil.writeMagic()
        bufUtil.writeLong(GUID)
        bufUtil.writeShort(clientPort)
        bufUtil.writeShort(Server.settings.mtuSize)
        bufUtil.writeByte(SERVER_SECURITY)
    }

    override fun toString(): String {
        return "S2COpenConnectionReply2Packet(" +
                "${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}, $GUID, $clientPort, ${Server.settings.mtuSize}, " +
                "$SERVER_SECURITY" +
                ")"
    }
}

class C2SClientConnectPacket(
    var session: Long = 0L
) : Packet(0x09, 18) {
    override fun read(bufUtil: ByteBufferUtil) {
        super.read(bufUtil)
        bufUtil.readLong()
        session = bufUtil.readLong()
        bufUtil.readByte()
    }
}

/**
 * Unknown use, eight byte constant
 */
val UNKNOWN_8_BYTES = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x04, 0x44, 0x0B.toByte(), 0xA9.toByte())

class S2CServerHandshakePacket(
    val session: Long
) : Packet(0x10, 96) {
    override fun write(bufUtil: ByteBufferUtil) {
        super.write(bufUtil)
        bufUtil.writeInt(0x043F57FE) // Cookie
        bufUtil.writeByte(0xCD.toByte()) // Security Flags
        bufUtil.writeShort(Server.settings.port.toShort()) // Server Port
        writeDataArray(bufUtil)
        bufUtil.writeByte(0x00); bufUtil.writeByte(0x00) // Some 0 bytes
        bufUtil.writeLong(session) // Session ID
        bufUtil.write(UNKNOWN_8_BYTES)
    }

    /**
     * Unknown use, 70 byte constant
     */
    private fun writeDataArray(bufUtil: ByteBufferUtil) {
        val unknown1 = byteArrayOf(0xF5.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xF5.toByte())
        val unknown2 = byteArrayOf(0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte(), 0xFF.toByte())
        bufUtil.writeInt24(unknown1.size)
        bufUtil.write(unknown1)
        for (i in 0 until 9) {
            bufUtil.writeInt24(unknown2.size)
            bufUtil.write(unknown2)
        }
    }
}

class S2CIncompatibleProtocolPacket : Packet(0x1A, 26) {
    override fun write(bufUtil: ByteBufferUtil) {
        super.write(bufUtil)
        bufUtil.writeByte(PROTOCOL_VERSION)
        bufUtil.writeMagic()
        bufUtil.writeLong(GUID)
    }
}
