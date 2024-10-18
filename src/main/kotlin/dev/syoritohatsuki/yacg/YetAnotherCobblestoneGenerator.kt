package dev.syoritohatsuki.yacg

import com.mojang.logging.LogUtils
import dev.syoritohatsuki.yacg.config.GeneratorsManager
import dev.syoritohatsuki.yacg.registry.BlocksEntityRegistry
import dev.syoritohatsuki.yacg.registry.BlocksRegistry
import dev.syoritohatsuki.yacg.registry.ItemGroupsRegistry
import dev.syoritohatsuki.yacg.registry.ItemsRegistry
import net.fabricmc.api.ModInitializer
import org.slf4j.Logger

object YetAnotherCobblestoneGenerator : ModInitializer {

    const val MOD_ID = "yacg"
    val logger: Logger = LogUtils.getLogger()

    override fun onInitialize() {
        GeneratorsManager

        BlocksRegistry
        BlocksEntityRegistry
        ItemsRegistry
        ItemGroupsRegistry
    }
}