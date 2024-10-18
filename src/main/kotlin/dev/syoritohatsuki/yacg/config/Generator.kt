package dev.syoritohatsuki.yacg.config

import kotlinx.serialization.Serializable

typealias ItemId = String

@Serializable
data class Generator(
    val energyUsage: Int,
    val items: Map<ItemId, ItemSettings> = emptyMap()
) {
    @Serializable
    data class ItemSettings(
        var weight: Int,
        val count: Int
    )
}