package me.elephant1214.mcpeas.network

import me.elephant1214.mcpeas.network.packet.*
import me.elephant1214.mcpeas.server.ConnectedClient
import me.elephant1214.mcpeas.server.GameManager
import me.elephant1214.mcpeas.server.Server
import me.elephant1214.mcpeas.utils.ByteBufferUtil
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import kotlin.properties.Delegates.notNull

object NetworkHandler {
    private var listenSocket: DatagramChannel by notNull()
    private val connectedClients = HashMap<InetSocketAddress, ConnectedClient>(Server.settings.playerLimit)

    internal fun init() {
        listenSocket = DatagramChannel.open().bind(InetSocketAddress(Server.settings.port))
        listenSocket.configureBlocking(false)
        println("Listening on ${listenSocket.localAddress}")
    }

    fun handleIncoming() {
        val buffer = ByteBuffer.allocate(Server.settings.mtuSize.toInt())
        val socketAddress: SocketAddress = listenSocket.receive(buffer) ?: return
        if (socketAddress !is InetSocketAddress) {
            println("Received an untrusted socket address with no port. Aborting handle because we don't know where to send response packets.")
            return
        }

        if (connectedClients.containsKey(socketAddress)) {
            val client = connectedClients[socketAddress]!!
            client.handlePacket(buffer)
        } else {
            handleUnconnected(socketAddress, buffer)
        }
    }

    private fun handleUnconnected(socketAddress: InetSocketAddress, buffer: ByteBuffer) {
        when (val unconnectedPacket = Packet.unconnectedPacketOf(buffer[0])) {
            is C2SUnconnectedPingPacket -> {
                val packet =
                    S2CUnconnectedPongPacket("MCPE;Test Server;${Server.PROTOCOL_VERSION};${Server.GAME_VERSION};${connectedClients.size};${Server.settings.playerLimit}")
                this.sendPacket(
                    socketAddress, packet, packet.size
                )
            }

            is C2SOpenConnectionRequest1Packet -> {
                unconnectedPacket.read(ByteBufferUtil(buffer))
                if (unconnectedPacket.protocolVersion != Server.PROTOCOL_VERSION) {
                    // handleIncorrectProtocolVersion(socketAddress) // Don't use for now
                }
                val packet = S2COpenConnectionReply1Packet()
                this.sendPacket(socketAddress, packet, packet.size)
            }

            is C2SOpenConnectionRequest2Packet -> {
                buffer.position(0) // Literally no clue why this is needed
                unconnectedPacket.read(ByteBufferUtil(buffer))
                val connectedClient = ConnectedClient(socketAddress, GameManager, unconnectedPacket.clientID)
                this.connectedClients[socketAddress] = connectedClient

                val packet = S2COpenConnectionReply2Packet(socketAddress.port.toShort())
                this.sendPacket(socketAddress, packet, packet.size)
            }
        }
    }

    fun sendPacket(socketAddress: InetSocketAddress, packet: Packet, size: Int) {
        val writer = ByteBufferUtil(size)
        packet.write(writer)
        val buffer = writer.readAll()
        val finalBuf = ByteBuffer.wrap(buffer)
        listenSocket.send(finalBuf, socketAddress)
    }

    // private fun handleIncorrectProtocolVersion(socketAddress: SocketAddress) {
    //     val packet = S2CIncompatibleProtocolVersion()
    //     val writer = ByteBufferUtil(26)
    //     packet.write(writer)
    //     val buffer = writer.readAll()
    //     handleSend(listenSocket, socketAddress, buffer)
    // }

    val RAKNET_OFFLINE_MESSAGE_DATA = byteArrayOf(
        0x00,
        0xff.toByte(),
        0xff.toByte(),
        0x00,
        0xfe.toByte(),
        0xfe.toByte(),
        0xfe.toByte(),
        0xfe.toByte(),
        0xfd.toByte(),
        0xfd.toByte(),
        0xfd.toByte(),
        0xfd.toByte(),
        0x12,
        0x34,
        0x56,
        0x78
    )
}