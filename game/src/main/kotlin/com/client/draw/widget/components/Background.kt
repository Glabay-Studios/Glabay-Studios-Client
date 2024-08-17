package com.client.draw.widget.components

import com.client.Sprite
import com.client.draw.ImageCache
import com.client.draw.widget.*
import com.client.draw.widget.components.Button.button
import com.client.draw.widget.components.Divider.divider
import com.client.draw.widget.components.Text.text
import com.client.graphics.interfaces.RSInterface
import com.client.util.MathUtilss.d2Tod1
import com.client.util.MathUtilss.fillPixels
import com.client.util.MathUtilss.insertPixels
import com.util.Position

object Background {

    fun InterfaceBuilder.background(builder: BackgroundComponent.() -> Unit) {
        val bld = BackgroundComponent()
        builder.invoke(bld)
        bld.componentId = nextId()
        addBackgroundImage(bld.componentId, bld.width,bld.height,bld.thin,bld.style)
        children.add(bld)
        bld.childType = WidgetType.BACKGROUND
        InterfaceData.components[bld.componentId] = InterfaceData.WidgetData(bld.componentId, emptyList(), WidgetType.BACKGROUND.toString())
        if(bld.title.isNotEmpty()) {
            text {
                text { bld.title }
                color { Colors.HEADER }
                type { Text.FontType.BOLD }
                position { Position(bld.width /2 ,10) }
            }
        }

        if(bld.devider) {
            divider {
                size { bld.width - 12 }
                position { Position(6,30) }
                force { bld.style }
            }
        }

        if(bld.closeButton) {
            button {
                normal { 2167 }
                hover { 2168 }
                tooltip { "Close" }
                position { Position(bld.width - 29,8) }
            }
        }

    }

    enum class ForceType {
        OSRS,
        NEW,
        NONE
    }

    private fun addBackgroundImage(id: Int, width: Int, height: Int, thin: Boolean, force : ForceType) {
        val tab: RSInterface = RSInterface.addInterface(id)
        tab.id = id
        tab.parentID = id
        tab.type = RSInterface.TYPE_BACKGROUND
        tab.contentType = 0
        tab.forceStyle = force
        tab.opacity = 0
        tab.thin = thin
        tab.width = width
        tab.height = height
    }

    fun buildBackground(width: Int, height: Int, skinnyborder: Boolean, osrs : Boolean): Sprite {
        val pixels = Array(height) { IntArray(width) }

        // Background
        fillPixels(pixels, ImageCache.get(if(osrs) 2153  else 2154), 0, 0, width, height)

        // Top border
        fillPixels(pixels, ImageCache.get(if(osrs) 2149  else 2150), 32, 0, width - 32, 7)

        // Right border
        fillPixels(pixels, ImageCache.get(if(osrs) 2155  else 2156), 0, 32, 7, height - 32)

        // Right border
        fillPixels(pixels, ImageCache.get(if(osrs) 2151  else 2152), width - 7, 32, width, height - 32)

        // Bottom border
        fillPixels(pixels, ImageCache.get(if(osrs) 2157  else 2158), 32, height - 7, width - 32, height)

        // Top left corner
        insertPixels(pixels, ImageCache.get(if(osrs) 2159  else 2160), 0, 0)

        // Top right corner
        insertPixels(pixels, ImageCache.get(if(osrs) 2161  else 2162), width - 32, 0)

        // Bottom left corner
        insertPixels(pixels, ImageCache.get(if(osrs) 2163  else 2164), 0, height - 32)

        // Bottom right corner
        insertPixels(pixels, ImageCache.get(if(osrs) 2165  else 2166), width - 32, height - 32)
        return Sprite(width, height, 0, 0, d2Tod1(pixels))
    }


    @Markers
    open class BackgroundComponent : InterfaceComponent() {
        var width : Int = 0
        var height : Int = 0
        var thin : Boolean = true
        var title : String = ""
        var closeButton : Boolean = false
        var devider : Boolean = false
        var style : ForceType = ForceType.NEW
        fun width(bld: () -> Int) {
            this.width = bld()
        }
        fun force(bld: () -> ForceType) {
            this.style = bld()
        }
        fun height(bld: () -> Int) {
            this.height = bld()
        }
        fun title(bld: () -> String) {
            this.title = bld()
        }
        fun closeButton(bld: () -> Boolean) {
            this.closeButton = bld()
        }
        fun devider(bld: () -> Boolean) {
            this.devider = bld()
        }

        fun thin(bld: () -> Boolean) {
            this.thin = bld()
        }
    }

}