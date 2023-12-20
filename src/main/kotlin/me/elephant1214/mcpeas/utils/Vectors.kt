package me.elephant1214.mcpeas.utils

class Vector3i(private var x: Int, private var y: Int, private var z: Int) {
    fun add(x: Int, y: Int, z: Int): Vector3i {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun add(value: Vector3i): Vector3i {
        this.x += value.x
        this.y += value.y
        this.z += value.z
        return this
    }

    fun sub(x: Int, y: Int, z: Int): Vector3i {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun sub(value: Vector3i): Vector3i {
        this.x -= value.x
        this.y -= value.y
        this.z -= value.z
        return this
    }
}

class Vector3f(var x: Float, var y: Float, var z: Float) {
    fun add(x: Float, y: Float, z: Float): Vector3f {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun add(value: Vector3f): Vector3f {
        this.x += value.x
        this.y += value.y
        this.z += value.z
        return this
    }

    fun sub(x: Float, y: Float, z: Float): Vector3f {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun sub(value: Vector3f): Vector3f {
        this.x -= value.x
        this.y -= value.y
        this.z -= value.z
        return this
    }
}

class Vector3d(private var x: Double, private var y: Double, private var z: Double) {
    fun add(x: Int, y: Int, z: Int): Vector3d {
        this.x += x
        this.y += y
        this.z += z
        return this
    }

    fun add(value: Vector3d): Vector3d {
        this.x += value.x
        this.y += value.y
        this.z += value.z
        return this
    }

    fun sub(x: Double, y: Double, z: Double): Vector3d {
        this.x -= x
        this.y -= y
        this.z -= z
        return this
    }

    fun sub(value: Vector3d): Vector3d {
        this.x -= value.x
        this.y -= value.y
        this.z -= value.z
        return this
    }
}
