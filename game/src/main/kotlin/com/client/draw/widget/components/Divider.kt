package com.client.draw.widget.components

import com.client.Sprite
import com.client.draw.ImageCache
import com.client.draw.widget.*
import com.client.graphics.interfaces.RSInterface
import com.client.util.MathUtilss.d2Tod1
import com.client.util.MathUtilss.fillPixels

object Divider {

    fun InterfaceBuilder.divider(builder: DeviderComponent.() -> Unit) {
        val bld = DeviderComponent()
        builder.invoke(bld)
        bld.componentId = nextId()
        addDividerImage(bld.componentId, bld.width,bld.across,bld.style)
        children.add(bld)
        bld.childType = WidgetType.BUTTON
        InterfaceData.components[bld.componentId] = InterfaceData.WidgetData(bld.componentId, emptyList(), WidgetType.DIVIDER.toString())
    }

    fun addDividerImage(id: Int, width: Int, across: Boolean, forceType: Background.ForceType) {
        val tab: RSInterface = RSInterface.addInterface(id)
        tab.id = id
        tab.parentID = id
        tab.type = RSInterface.TYPE_DIVIDER

        tab.contentType = 0
        tab.opacity = 0
        tab.forceStyle = forceType
        tab.across = across
        tab.width = width
        tab.height = 7
    }



    fun buildDevider(across : Boolean = true,width: Int,osrs : Boolean): Sprite {
        if (!across) {
            return buildDeviderHeight(width,osrs)
        }
        val pixels = Array(7) { IntArray(width) }
        fillPixels(pixels, ImageCache.get(if(osrs) 2149  else 2150), 0, 0, width, 7)
        return Sprite(width, 7, 0, 0, d2Tod1(pixels))
    }

    fun buildDeviderHeight(height: Int,osrs : Boolean): Sprite {
        val pixels = Array(height) { IntArray(7) }
        fillPixels(pixels, ImageCache.get(if(osrs) 2151  else 2152), 0, 0, 7, height)
        return Sprite(7, height, 0, 0, d2Tod1(pixels))
    }


    @Markers
    open class DeviderComponent : InterfaceComponent() {
        var width : Int = 0
        var across : Boolean = true
        var style : Background.ForceType = Background.ForceType.NEW
        fun size(bld: () -> Int) {
            this.width = bld()
        }
        fun across(bld: () -> Boolean) {
            this.across = bld()
        }

        fun force(bld: () -> Background.ForceType) {
            this.style = bld()
        }
    }

}