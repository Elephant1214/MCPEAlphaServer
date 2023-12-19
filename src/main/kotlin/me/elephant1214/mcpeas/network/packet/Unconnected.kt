package me.elephant1214.mcpeas.network.packet

import me.elephant1214.mcpeas.network.NetworkHandler.RAKNET_OFFLINE_MESSAGE_DATA
import me.elephant1214.mcpeas.server.Server
import me.elephant1214.mcpeas.server.Server.GUID
import me.elephant1214.mcpeas.server.Server.PROTOCOL_VERSION
import me.elephant1214.mcpeas.server.Server.SERVER_SECURITY
import me.elephant1214.mcpeas.utils.ByteBufferUtil
import java.net.InetSocketAddress

class C2SUnconnectedPingPacket(
    var timeSinceStart: Long = 0L
) : Packet(0x01) {
    override fun read(bufUtil: ByteBufferUtil) {
        super.read(bufUtil)
        timeSinceStart = bufUtil.readLong()
        bufUtil.skipMagic()
    }

    override fun toString(): String {
        return "C2SUnconnectedPingPacket(${Server.timeSinceStart()}, ${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()})"
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
        return "S2CUnconnectedPongPacket(${Server.timeSinceStart()}, $GUID, ${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}, $data)"
    }
}

class C2SOpenConnectionRequest1Packet(
    var protocolVersion: Byte = 0
) : Packet(0x05) {
    override fun read(bufUtil: ByteBufferUtil) {
        super.read(bufUtil)
        bufUtil.skipMagic()
        val tens = bufUtil.readByte().toInt() and 0xFF
        val ones = bufUtil.readByte().toInt() and 0xFF
        protocolVersion = ((tens * 10) + ones).toByte()
    }

    override fun toString(): String {
        return "C2SOpenConnectionRequest1Packet(${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}, $protocolVersion)"
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
        return "S2COpenConnectionReply1Packet(${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}, $GUID, $SERVER_SECURITY, ${Server.settings.mtuSize})"
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
        return "C2SOpenConnectionRequest2Packet(${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}, $clientSocket, $mtuSize, $clientID)"
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
        return "S2COpenConnectionReply2Packet(${RAKNET_OFFLINE_MESSAGE_DATA.toHexString()}, $GUID, $clientPort, ${Server.settings.mtuSize}, $SERVER_SECURITY)"
    }
}

class S2CIncompatibleProtocolVersion : Packet(0x1A) {
    override fun write(bufUtil: ByteBufferUtil) {
        super.write(bufUtil)
        bufUtil.writeByte(PROTOCOL_VERSION)
        bufUtil.writeMagic()
        bufUtil.writeLong(GUID)
    }
}