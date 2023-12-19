package me.elephant1214.mcpeas.utils

import me.elephant1214.mcpeas.network.NetworkHandler.Companion.RAKNET_OFFLINE_MESSAGE_DATA
import java.io.EOFException
import java.lang.IllegalStateException
import java.net.InetAddress
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.charset.StandardCharsets

class ByteBufferUtil(private val stream: ByteBuffer) {

    constructor(bufferSize: Int) : this(ByteBuffer.allocate(bufferSize))

    fun <T : Any> get(clazzIn: T): ByteArray {
        val size: Int = when (clazzIn) {
            Short::class.java -> 2
            Int::class.java -> 4
            Float::class.java -> 4
            Long::class.java -> 8
            Double::class.java -> 8
            else -> throw IllegalArgumentException("${clazzIn.javaClass.name} is not supported")
        }
        val span = ByteArray(size)
        if (stream.remaining() < size) throw EOFException()
        stream.get(span)
        return span
    }

    fun read(length: Int): ByteArray {
        val memory = ByteArray(length)
        if (stream.remaining() < length) throw EOFException()
        stream.get(memory)
        return memory
    }

    fun readAll(): ByteArray = stream.array()

    fun getStream(): ByteBuffer = stream

    fun write(data: ByteArray) {
        stream.put(data)
    }

    fun readByte(): Byte = stream.get()

    fun writeByte(value: Byte) {
        stream.put(value)
    }

    fun readShort(): Short = ByteBuffer.wrap(get(Short::class.java)).order(ByteOrder.BIG_ENDIAN).getShort()

    fun writeShort(value: Short) {
        stream.putShort(value)
        // stream.put(ByteBuffer.allocate(Short.SIZE_BYTES).putShort(value).order(ByteOrder.BIG_ENDIAN))
    }

    fun readInt(): Int = ByteBuffer.wrap(get(Int::class.java)).order(ByteOrder.BIG_ENDIAN).getInt()

    fun writeInt(value: Int) {
        stream.putInt(value)
        // stream.put(ByteBuffer.allocate(Int.SIZE_BYTES).putInt(value).order(ByteOrder.BIG_ENDIAN))
    }

    fun readLong(): Long = ByteBuffer.wrap(get(Long::class.java)).order(ByteOrder.BIG_ENDIAN).getLong()

    fun writeLong(value: Long) {
        stream.putLong(value)
        // stream.put(ByteBuffer.allocate(Long.SIZE_BYTES).putLong(value).order(ByteOrder.BIG_ENDIAN))
    }

    fun readFloat(): Float = ByteBuffer.wrap(get(Float::class.java)).order(ByteOrder.BIG_ENDIAN).getFloat()

    fun writeFloat(value: Float) {
        stream.putFloat(value)
        // stream.put(ByteBuffer.allocate(Float.SIZE_BYTES).putFloat(value).order(ByteOrder.BIG_ENDIAN))
    }

    fun readDouble(): Double = ByteBuffer.wrap(get(Double::class.java)).order(ByteOrder.BIG_ENDIAN).getDouble()

    fun writeDouble(value: Double) {
        stream.putDouble(value)
        // stream.put(ByteBuffer.allocate(Double.SIZE_BYTES).putDouble(value).order(ByteOrder.BIG_ENDIAN))
    }

    fun readInt24(): Int {
        val byte1 = readByte().toInt() and 0xFF
        val byte2 = readByte().toInt() and 0xFF
        val byte3 = readByte().toInt() and 0xFF
        return (byte1 shl 16) or (byte2 shl 8) or byte3
    }

    fun readRemaining(): ByteArray = read(stream.remaining())

    fun readString(): String {
        val length = readShort()
        val bytes = ByteArray(length.toInt())
        if (stream.remaining() < length) throw EOFException()
        stream.get(bytes)
        return String(bytes, StandardCharsets.UTF_8)
    }

    fun writeString(value: String) {
        val bytes = value.toByteArray()
        writeShort(bytes.size.toShort())
        write(bytes)
    }

    fun readTriad(): Int {
        val byte1 = stream.get().toUByte().toInt()
        val byte2 = stream.get().toUByte().toInt() shl 8
        val byte3 = stream.get().toUByte().toInt() shl 16
        return byte1 or byte2 or byte3
    }

    fun readSocketAddress(): InetSocketAddress {
        val ipVersion = readByte().toInt();
        val port: Int
        val socket: InetSocketAddress

        when (ipVersion) {
            4 -> {
                val address = "${readByte().toUByte().toInt()}.${readByte().toUByte().toInt()}.${readByte().toUByte().toInt()}.${readByte().toUByte().toInt()}"
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
                throw IllegalStateException("Got invalid IP version, IPv$ipVersion")
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
        stream.position(stream.position() + amount)
    }

    fun skipMagic() {
        stream.position(stream.position() + 16)
    }

    fun writeMagic() {
        stream.put(RAKNET_OFFLINE_MESSAGE_DATA)
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