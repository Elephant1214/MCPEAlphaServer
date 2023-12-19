package me.elephant1214.mcpeas.server

import me.elephant1214.mcpeas.network.packet.Packet
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.util.*

class ConnectedClient(val clientSocket: InetSocketAddress, val gameManager: GameManager, val clientID: Long) {
    private val networkHandler = gameManager.networkHandler
    private var status: ClientStatus = ClientStatus.CONNECTING
    private val outgoing = ArrayList<Packet>()
    private val needsAck = TreeSet<Int>()
    private var lastPing = Calendar.getInstance().time

    @OptIn(ExperimentalStdlibApi::class)
    fun handlePacket(data: ByteBuffer) {
        println(data[0].toUByte().toInt())

        //data.position(0)
        // when (data[0]) {
        //     0x84.toByte() ->  C2SReadyPacket().handle(this)
        //     // 0xC0.toByte() -> sendPlayPacket()
        // }

    }

    fun sendPlayPacket(packet: Packet) {
        networkHandler.sendPacket(clientSocket, packet, packet.size)
    }

    enum class ClientStatus {
        CONNECTING,
        CONNECTED,
        DISCONNECTING,
        DISCONNECTED
    }
}