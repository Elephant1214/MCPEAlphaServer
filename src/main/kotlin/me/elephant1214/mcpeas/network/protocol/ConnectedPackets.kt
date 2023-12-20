@file:Suppress("MagicNumber")

package me.elephant1214.mcpeas.network.protocol

import me.elephant1214.mcpeas.network.Packet
import me.elephant1214.mcpeas.server.ConnectedClient
import me.elephant1214.mcpeas.server.Server
import me.elephant1214.mcpeas.utils.ByteBufferUtil

open class ConnectedPacket(id: Byte, size: Int = Server.settings.mtuSize.toInt()) : Packet(id) {
    open fun handle(client: ConnectedClient) {
    }
}

// class

class TWAckNak : ConnectedPacket(0xC0.toByte(), 10) {
    override fun read(bufUtil: ByteBufferUtil) {
        super.read(bufUtil)
        val ranges = ArrayList<Pair<Int, Int>>()
        val count = bufUtil.readShort()
        for (i in 0 until count) {
            val oneSequence = bufUtil.readByte()
            if (oneSequence == i.toByte()) {
                val start = bufUtil.readInt24()
                var end = bufUtil.readInt24()
                if (end - start > 512) end = start + 512
                ranges.add(Pair(start, end))
            } else {
                val seqNum = bufUtil.readInt24()
                ranges.add(Pair(seqNum, seqNum))
            }
        }
    }

    override fun write(bufUtil: ByteBufferUtil) {
        super.write(bufUtil)
    }
}
