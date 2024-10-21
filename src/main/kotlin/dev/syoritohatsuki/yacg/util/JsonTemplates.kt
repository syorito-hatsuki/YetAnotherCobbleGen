package dev.syoritohatsuki.yacg.util

import net.minecraft.util.Identifier
import org.intellij.lang.annotations.Language

object JsonTemplates {
    @Language("JSON")
    fun getBlockState(id: Identifier): String = """
        {
            "variants":{
                "facing=north,enabled=true": {
                    "model":"yacg:block/${id.namespace}_${id.path}"
                },
                "facing=north,enabled=false": {
                    "model":"yacg:block/${id.namespace}_${id.path}_off"
                },
                "facing=east,enabled=true":{
                    "model":"yacg:block/${id.namespace}_${id.path}",
                    "y":90
                },
                "facing=east,enabled=false":{
                    "model":"yacg:block/${id.namespace}_${id.path}_off",
                    "y":90
                },
                "facing=south,enabled=true":{
                    "model":"yacg:block/${id.namespace}_${id.path}",
                    "y":180
                },
                "facing=south,enabled=false":{
                    "model":"yacg:block/${id.namespace}_${id.path}_off",
                    "y":180
                },
                "facing=west,enabled=true":{
                    "model":"yacg:block/${id.namespace}_${id.path}",
                    "y":270
                },
                "facing=west,enabled=false":{
                    "model":"yacg:block/${id.namespace}_${id.path}_off",
                    "y":270
                }
            }
        }
    """.trimIndent()

    @Language("JSON")
    fun getBlockModel(id: Identifier) = """
        {
            "parent":"block/cube_all",
            "textures":{
                "down":"yacg:block/${id.namespace}_${id.path}/bottom",
                "east":"yacg:block/${id.namespace}_${id.path}/left",
                "north":"yacg:block/${id.namespace}_${id.path}/front",
                "south":"yacg:block/${id.namespace}_${id.path}/back",
                "up":"yacg:block/${id.namespace}_${id.path}/top",
                "west":"yacg:block/${id.namespace}_${id.path}/right",
                "particle":"yacg:block/${id.namespace}_${id.path}/back"
            }
        }
    """.trimIndent()

    @Language("JSON")
    fun getOffBlockModel(id: Identifier) = """
        {
            "parent":"block/cube_all",
            "textures":{
                "down":"yacg:block/${id.namespace}_${id.path}/off/bottom",
                "east":"yacg:block/${id.namespace}_${id.path}/off/left",
                "north":"yacg:block/${id.namespace}_${id.path}/off/front",
                "south":"yacg:block/${id.namespace}_${id.path}/off/back",
                "up":"yacg:block/${id.namespace}_${id.path}/off/top",
                "west":"yacg:block/${id.namespace}_${id.path}/off/right",
                "particle":"yacg:block/${id.namespace}_${id.path}/off/back"
            }
        }
    """.trimIndent()

    @Language("JSON")
    fun getItemModel(id: Identifier) = """
        {
            "parent":"yacg:block/${id.namespace}_${id.path}"
        }
    """.trimMargin()
}