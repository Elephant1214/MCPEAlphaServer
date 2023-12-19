package me.elephant1214.mcpeas.world

data class GameElementInfo(val id: Int, val name: String) {
    override fun equals(other: Any?): Boolean {
        return other is GameElementInfo && other.id == id && other.name == name
    }

    fun isSimilar(other: GameElementInfo): Boolean {
        return other.id == id || other.name == name
    }

    override fun toString(): String {
        return "GameElementInfo($id, $name)"
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + name.hashCode()
        return result
    }
}
