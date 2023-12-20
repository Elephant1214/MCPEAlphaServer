package me.elephant1214.mcpeas.utils

import me.elephant1214.mcpeas.network.NetworkHandler.RAKNET_OFFLINE_MESSAGE_DATA
import java.io.EOFException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

@Suppress("MagicNumber", "TooManyFunctions")
class ByteBufferUtil(private val buffer: ByteBuffer) {

    constructor(bufferSize: Int) : this(ByteBuffer.allocate(bufferSize))

    fun <T : Any> get(clazzIn: T): ByteArray {
        val size: Int = when (clazzIn) {
            Short::class.java -> 2
            Int::class.java -> 4
            Float::class.java -> 4
            Long::class.java -> 8
            Double::class.java -> 8
            else -> error("${clazzIn.javaClass.name} is not supported")
        }
        val span = ByteArray(size)
        if (buffer.remaining() < size) throw EOFException()
        buffer.get(span)
        return span
    }

    fun read(length: Int): ByteArray {
        val memory = ByteArray(length)
        if (buffer.remaining() < length) throw EOFException()
        buffer.get(memory)
        return memory
    }

    fun readAll(): ByteArray = buffer.array()

    fun getBuffer(): ByteBuffer = buffer

    fun write(data: ByteArray) {
        buffer.put(data)
    }

    fun readByte(): Byte = buffer.get()

    fun writeByte(value: Byte) {
        buffer.put(value)
    }

    fun readShort(): Short = ByteBuffer.wrap(get(Short::class.java)).order(ByteOrder.BIG_ENDIAN).getShort()

    fun writeShort(value: Short) {
        buffer.putShort(value)
    }

    fun readInt(): Int = ByteBuffer.wrap(get(Int::class.java)).order(ByteOrder.BIG_ENDIAN).getInt()

    fun writeInt(value: Int) {
        buffer.putInt(value)
    }

    fun readLong(): Long = ByteBuffer.wrap(get(Long::class.java)).order(ByteOrder.BIG_ENDIAN).getLong()

    fun writeLong(value: Long) {
        buffer.putLong(value)
    }

    fun readFloat(): Float = ByteBuffer.wrap(get(Float::class.java)).order(ByteOrder.BIG_ENDIAN).getFloat()

    fun writeFloat(value: Float) {
        buffer.putFloat(value)
    }

    fun readDouble(): Double = ByteBuffer.wrap(get(Double::class.java)).order(ByteOrder.BIG_ENDIAN).getDouble()

    fun writeDouble(value: Double) {
        buffer.putDouble(value)
    }

    /**
     * Triad, which is just a 24-bit (3 byte) integer
     */
    fun readInt24(): Int {
        var int24 = 0L
        for (i in 0 until 3) {
            int24 = (int24 shl 8) + (readByte().toInt() and 0xFF)
        }
        return int24.toInt()
    }

    /**
     * Triad, which is just a 24-bit (3 byte) integer
     */
    fun writeInt24(value: Int) {
        require(value > UINT_24_MIN || value < UINT_24_MAX) {
            "$value is not within the range of an unsigned 24-bit integer." +
                    "Must be between $UINT_24_MIN and $UINT_24_MAX"
        }
        val buffer = ByteArray(3)
        buffer[2] = ((value shr 16) and 0xFF).toByte()
        buffer[1] = ((value shr 8) and 0xFF).toByte()
        buffer[0] = (value and 0xFF).toByte()
        this.buffer.put(buffer)
    }

    fun readRemaining(): ByteArray = read(buffer.remaining())

    fun readString(): String {
        val length = readShort()
        val bytes = ByteArray(length.toInt())
        if (buffer.remaining() < length) throw EOFException()
        buffer.get(bytes)
        return String(bytes, StandardCharsets.UTF_8)
    }

    fun writeString(value: String) {
        val bytes = value.toByteArray()
        writeShort(bytes.size.toShort())
        write(bytes)
    }

    fun readSocketAddress(): InetSocketAddress {
        val ipVersion = readByte().toInt()
        val port: Int
        val socket: InetSocketAddress

        when (ipVersion) {
            4 -> {
                val address = "${readByte().toUByte().toInt()}.${readByte().toUByte().toInt()}." + "${
                    readByte().toUByte().toInt()
                }.${
                    readByte().toUByte().toInt()
                }"
                port = readShort().toInt()
                socket = InetSocketAddress(address, port)
            }

            6 -> {
                readShort()
                port = readShort().toInt()
                readLong()
                val addressBytes = read(16)
                socket = InetSocketAddress(InetAddress.getByAddress(addressBytes), port)
            }

            else -> {
                error("Got an Invalid IP version, IPv$ipVersion")
            }
        }
        return socket
    }

    fun writeSocketAddress(socketAddress: InetSocketAddress) {
        val addressBytes = socketAddress.address.address
        writeByte(if (addressBytes.size == 4) 0x04 else 0x06)
        val xorBytes = ByteArray(addressBytes.size)
        for (i in xorBytes.indices) {
            xorBytes[i] = (addressBytes[i].toInt() xor 225.toByte().toInt()).toByte()
        }
        write(xorBytes)
        writeShort(socketAddress.port.toShort())
    }

    fun skip(amount: Int) {
        buffer.position(buffer.position() + amount)
    }

    fun skipMagic() {
        buffer.position(buffer.position() + 16)
    }

    fun writeMagic() {
        buffer.put(RAKNET_OFFLINE_MESSAGE_DATA)
    }

    fun readVector(): Vector3f {
        val x = readFloat()
        val y = readFloat()
        val z = readFloat()
        return Vector3f(x, y, z)
    }

    fun writeVector(v: Vector3f) {
        writeFloat(v.x)
        writeFloat(v.y)
        writeFloat(v.z)
    }
}
