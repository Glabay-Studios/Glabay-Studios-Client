package com.client.draw.widget.impl

import com.client.draw.widget.*
import com.client.draw.widget.components.Background.background
import com.client.draw.widget.components.Button.button
import com.client.draw.widget.components.Button.buttons
import com.client.draw.widget.components.Rasterizer
import com.client.draw.widget.components.Rasterizer.rasterizer
import com.client.draw.widget.components.Rasterizer.rasterizers
import com.client.draw.widget.components.Sprites.sprite
import com.client.draw.widget.components.Sprites.sprites
import com.client.draw.widget.components.Text
import com.client.draw.widget.components.Text.text
import com.client.draw.widget.components.Text.texts
import com.client.engine.GameEngine.canvasHeight
import com.util.Position

class TeleportInterface : Interface() {

    val tabs = listOf(
        "Spotlight",
        "Cities",
        "Training",
        "Minigames",
        "Bosses",
        "Dungeons",
        "Wilderness",
        "Donator"
    )

    override val subInterface: List<() -> Unit> = listOf(
        { buildMain() },
        { buildTeleportList() },
        { buildTeleportListWarning() },
        { buildTeleportListWarningButtons() }
    )

    private fun buildMain() {
        buildInterface(24511,6,4) {
            val height = canvasHeight - 170
            background {
                width { 511 }
                height { height }
                title { "Teleports Menu" }
                closeButton { true }
                devider { true }
            }

            rasterizer {
                setColours { listOf(0x0E0E0C,0x474745,0x494034) }
                width { 85 }
                height { (height - 46) }
                position { Position(8,38) }
            }

            rasterizer {
                setColours { listOf(0x0E0E0C,0x474745,0x494034) }
                width { 411 }
                height { (height - 46) }
                position { Position(92,38) }
            }

            buttons(tabs.size,1) { idx ->
                normal { 2230 }
                hover { 2231 }
                tooltip { tabs[idx] }
                text {
                    text { tabs[idx] }
                    color { 0xFF981F }
                    position { Position(78 / 2,7) }
                }
                position { Position(11,41) }
            }

            sprite {
                id { 2235 }
                position { Position(13,38 + (height - 46 - 38)) }
            }

            sprite {
                id { 2233 }
                position { Position(13,38 + (height - 46 - 20)) }
            }


            text {
                text { "1 / 4" }
                type { Text.FontType.REGULAR }
                position { Position(33,38 + (height - 46 - 38) + 1) }
                center { false }
                color { Colors.GOLD }
            }

            text {
                text { "300k" }
                type { Text.FontType.REGULAR }
                position { Position(33,38 + (height - 46 - 20) + 2) }
                center { false }
                color { Colors.GOLD }
                hover { Colors.WHITE }
                tooltip { "Add to Coffer" }
                width { 62 }
            }

            widget {
                widgetID { 28229 }
                position { Position(90,40) }
            }

            widget {
                widgetID { 580 }
                position { Position(90,40) }
            }

        }
    }

    private fun buildTeleportList() {
        buildInterface(28229) {
            scroll(395,283,3000) {
                rasterizers(60,1,0,40) { idx ->
                    width { 384 }
                    height { 40 }
                    position { Position(7,3) }
                    setColour {if (idx % 2 == 0) 0x3D362B else 0x373027 }
                    setColourHover { if (idx % 2 == 0) 0x484033 else 0x433A2F }
                    tooltip { "Teleport" }
                }
                sprites(60,1,0,0) {
                    id { 2236 }
                    position { Position(7,3) }
                }
                texts(60,1,0,31) { idx ->
                    text { "Obor" }
                    center { false }
                    color { Colors.GOLD }
                    type { Text.FontType.BOLD }
                    position { Position(53,10) }
                }
                texts(60,1,0,31) { idx ->
                    text { "Location: Falador Park Underground" }
                    center { false }
                    color { 0x9E9E9E }
                    type { Text.FontType.REGULAR }
                    position { Position(53,24) }
                }
                buttons(60,1,0,24) { idx ->
                    normal { 2234 }
                    tooltip { "Favorite" }
                    position { Position(371,15) }
                }
                sprites(60,1,0,22) {
                    id { 2233 }
                    position { Position(321,14) }
                }

                texts(60,1,0,31) { idx ->
                    text { "24k" }
                    center { false }
                    color { Colors.GOLD }
                    type { Text.FontType.REGULAR }
                    position { Position(338,17) }
                }

            }
        }
    }

    private fun buildTeleportListWarning() {
        buildInterface(580) {
            rasterizer {
                width { 407 }
                height { 283 }
                opacity { 200 }
                position { Position(4,0) }
                setColour { 0x000000 }
                type { Rasterizer.RasterizerType.BOX }
            }
            sprite {
                id { 2237 }
                position { Position(165,16) }
            }
            text {
                text { "sdfsdfsdfsdfsdf" }
                width { 407 }
                color { Colors.GOLD }
                space { 5 }
                position { Position(407 / 2, 16 + 79 + 20) }
            }

            widget {
                widgetID { 59511 }
                position { Position(0,0) }
            }

        }
    }

    private fun buildTeleportListWarningButtons() {
        buildInterface(59511) {
            button {
                normal { 1467 }
                hover { 1468 }
                tooltip { "Confirm" }
                position { Position(79,241) }
                text {
                    text { "Yes" }
                    color { Colors.GOLD }
                    position { Position(90 / 2,4) }
                    type { Text.FontType.REGULAR }
                }
            }

            button {
                normal { 1467 }
                hover { 1468 }
                tooltip { "No" }
                position { Position(241,241) }
                text {
                    text { "No" }
                    color { Colors.GOLD }
                    position { Position(90 / 2,4) }
                    type { Text.FontType.REGULAR }
                }
            }

        }
    }

}