package com.client.draw.widget.impl

import com.client.draw.widget.Interface
import com.client.draw.widget.buildInterface
import com.client.draw.widget.components.Button.button
import com.client.draw.widget.components.Dropdown.dropdown
import com.client.draw.widget.components.Text
import com.client.draw.widget.components.Text.text
import com.client.draw.widget.scroll
import com.util.Position

class LoginScreenSettings : Interface() {

    override val subInterface: List<() -> Unit> = listOf(
        { buildMain() }
    )

    fun buildMain() {
        buildInterface(44000,512,331,true) {
            scroll(315,200,370) {
                //Background Section

                text {
                    text { "Background" }
                    color { 0xFFFFFF }
                    position { Position((362 / 2) - 10,5) }
                    type { Text.FontType.BOLD }
                }
                button {
                    normal { 2212 }
                    hover { 2213 }
                    tooltip { "Toggle" }
                    position { Position(0,25) }
                }
                text {
                    text { "Game World Background" }
                    color { 0xFFFFFF }
                    position { Position(23,28) }
                    center { false }
                }

                button {
                    normal { 2212 }
                    hover { 2213 }
                    tooltip { "Toggle" }
                    position { Position(0,45) }
                }
                text {
                    text { "Game World Npc's" }
                    color { 0xFFFFFF }
                    position { Position(23,48) }
                    center { false }
                }
                button {
                    normal { 2212 }
                    hover { 2213 }
                    tooltip { "Toggle" }
                    position { Position(0,65) }
                }
                text {
                    text { "Disable Login Flames" }
                    color { 0xFFFFFF }
                    position { Position(23,68) }
                    center { false }
                }

                //Audio Section

                text {
                    text { "Audio" }
                    color { 0xFFFFFF }
                    position { Position((362 / 2) - 10,88) }
                    type { Text.FontType.BOLD }
                }
                text {
                    text { "Login Music" }
                    color { 0xFFFFFF }
                    position { Position(4,112) }
                    center { false }
                }
                dropdown {
                    options { listOf("dfdf","sdfsd") }
                    position { Position(75,108) }
                }

                text {
                    text { "Login Music Volume" }
                    color { 0xFFFFFF }
                    position { Position(4,142) }
                    center { false }
                }

                //Other Section

                text {
                    text { "Other" }
                    color { 0xFFFFFF }
                    position { Position((362 / 2) - 10,162) }
                    type { Text.FontType.BOLD }
                }

                button {
                    normal { 2212 }
                    hover { 2213 }
                    tooltip { "Toggle" }
                    position { Position(0,182) }
                }

                text {
                    text { "Disable Hotkeys" }
                    color { 0xFFFFFF }
                    position { Position(23,185) }
                    center { false }
                }

            }
        }
    }

}