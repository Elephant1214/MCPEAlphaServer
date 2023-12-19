package me.elephant1214.mcpeas.utils

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketAddress

fun handleSend(socket: DatagramSocket, socketAddress: SocketAddress, data: ByteArray) {
    socket.send(DatagramPacket(data, data.size, socketAddress))
}