package dev.syoritohatsuki.yacg.util

import net.minecraft.util.Identifier

object JsonTemplates {
    fun getBlockState(id: Identifier): String = """
        {
            "variants":{
                "facing=north": {
                    "model":"yacg:block/${id.namespace}_${id.path}"
                },
                "facing=east":{
                    "model":"yacg:block/${id.namespace}_${id.path}","y":90
                },
                "facing=south":{
                    "model":"yacg:block/${id.namespace}_${id.path}","y":180
                },
                "facing=west":{
                    "model":"yacg:block/${id.namespace}_${id.path}","y":270
                }
            }
        }
    """.trimIndent()

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

    fun getItemModel(id: Identifier) = """
        {
            "parent":"yacg:block/${id.namespace}_${id.path}"
        }
    """.trimMargin()
}