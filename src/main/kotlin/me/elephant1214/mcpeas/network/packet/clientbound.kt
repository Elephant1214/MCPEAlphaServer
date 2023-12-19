package me.elephant1214.mcpeas.network.packet

import me.elephant1214.mcpeas.network.packet.S2CLoginStatusPacket.LoginStatus.*
import me.elephant1214.mcpeas.utils.ByteBufferUtil

class S2CLoginStatusPacket(
    var loginStatus: LoginStatus = VERSIONS_MATCH
) : Packet(0x83.toByte()) {
    override fun write(bufUtil: ByteBufferUtil) {
        super.write(bufUtil)
        bufUtil.writeInt(VERSIONS_MATCH.ordinal)
    }

    enum class LoginStatus {
        VERSIONS_MATCH,
        OUTDATED_SERVER,
        OUTDATED_CLIENT
    }
}