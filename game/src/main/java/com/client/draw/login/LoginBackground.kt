package com.client.draw.login

import com.client.Sprite
import com.util.AssetUtils

enum class LoginBackground(val spriteID : Int = -1) {
    FADING_BACKGROUNDS,
    NORMAL(4),
    OLD(5),
    CHRISTMAS(6),
    CHAMBERS_OF_XERIC(7),
    DARKMEYER(8),
    DRAGON_SLAYER_2(9),
    FOSSIL_ISLAND(10),
    HALLOWEEN(11),
    HALLOWEEN_2019(12),
    INFERNO(13),
    KEBOS(14),
    MONKEY_MADNESS_2(15),
    PRIFDDINAS(16),
    THEATRE_OF_BLOOD(17),
    A_KINGDOM_DIVIDED(18),
    NEX(19),
    TOMBS_OF_AMASCUT(20);

    companion object {
        val backGroundSprite : MutableMap<Int,Sprite> = emptyMap<Int,Sprite>().toMutableMap()
        fun load() {
            values().forEach {
                if (it.spriteID != -1) {
                    backGroundSprite[it.spriteID] = Sprite(AssetUtils.getResource("${2240 + it.spriteID}.png"))
                }
            }
        }

    }


}