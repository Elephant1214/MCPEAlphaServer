package me.elephant1214.mcpeas.registry

import me.elephant1214.mcpeas.world.GameElementInfo
import java.util.*

class SimpleRegistry<T> {
    private val registryMap = HashMap<GameElementInfo, T>()

    fun register(element: GameElementInfo, entry: T) {
        registryMap.forEach { (elem, _) ->
            if (element.isSimilar(elem)) {
                println("Can't register $element as it conflicts with $elem")
                return
            }
        }
        registryMap[element] = entry
    }

    fun get(id: GameElementInfo): Optional<T> {
        val entry = registryMap[id]
        return if (entry != null) {
            Optional.of<T>(entry) as Optional<T>
        } else {
            Optional.empty<T>() as Optional<T>
        }
    }

    fun get(identifier: Any): Optional<T> {
        val entryInformation = when (identifier) {
            is String -> registryMap.keys.find { it.name == identifier }
            is Int -> registryMap.keys.find { it.id == identifier }
            else -> null
        }

        if (entryInformation != null) {
            val entry = registryMap[entryInformation]
            if (entry != null) {
                return Optional.of<T>(entry) as Optional<T>
            }
        }
        return Optional.empty<T>() as Optional<T>
    }

    fun getInfo(entry: T): Optional<GameElementInfo> {
        val key = registryMap.getKey(entry)
        return if (key != null) {
            Optional.of(key)
        } else {
            Optional.empty()
        }
    }
}

private fun <K, V> HashMap<K, V>.getKey(entry: V): K? {
    forEach { (key, mapVal) ->
        if (mapVal == entry) {
            return key
        }
    }
    return null
}
