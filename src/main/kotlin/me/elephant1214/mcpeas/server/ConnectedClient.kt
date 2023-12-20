package me.elephant1214.mcpeas.server

import me.elephant1214.mcpeas.network.NetworkHandler
import me.elephant1214.mcpeas.network.Packet
import me.elephant1214.mcpeas.network.protocol.C2SClientConnectPacket
import me.elephant1214.mcpeas.network.protocol.S2CServerHandshakePacket
import me.elephant1214.mcpeas.utils.ByteBufferUtil
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.time.Instant
import java.util.*

@Suppress("MagicNumber")
class ConnectedClient(val clientSocket: InetSocketAddress, val gameManager: GameManager, val clientID: Long) {
    private var status: ClientStatus = ClientStatus.CONNECTING
    private val outgoing = ArrayList<Packet>()
    private val needsAck = TreeSet<Int>()
    private var lastPing = Instant.now()

    fun handlePacket(buffer: ByteBuffer) {
        // println(buffer.array().toHexString())
        when (buffer[0]) {
            // Doesn't work, but I think the encapsulation is just messed up,
            // and I'm not sending exactly what it wants back (yet)
            0x84.toByte() -> {
                if (buffer[10] == 0x09.toByte()) {
                    buffer.position(9) // Encapsulation, I guess?
                    val connectPacket = C2SClientConnectPacket()
                    connectPacket.read(ByteBufferUtil(buffer))
                    val handshake = S2CServerHandshakePacket(connectPacket.session)
                    val handshakeBuf = ByteBufferUtil(handshake.size)
                    handshake.write(handshakeBuf)
                    sendPlayPacket(handshake)
                    encapsulateAndSend(handshake, handshakeBuf.readAll())
                } else {
                    println("Real ready packet?")
                }
            }
        }

        //data.position(0)
        // when (data[0]) {
        //     0x84.toByte() ->  C2SReadyPacket().handle(this)
        //     // 0xC0.toByte() -> sendPlayPacket()
        // }

    }

    private fun encapsulateAndSend(packet: Packet, byteArr: ByteArray) {
        val encapsulationBuf = ByteBufferUtil(ByteBuffer.allocate(3 + packet.size))
        val lengthBits = (packet.size * 8).toShort()
        encapsulationBuf.writeByte(0x00)
        encapsulationBuf.writeShort(lengthBits)
        encapsulationBuf.write(byteArr)
        NetworkHandler.sendBuffer(clientSocket, encapsulationBuf.getBuffer())
    }

    fun sendPlayPacket(packet: Packet) {
        NetworkHandler.sendPacket(clientSocket, packet, packet.size)
    }

    enum class ClientStatus {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED
    }
}
