package com.client.draw.widget.components

import com.client.draw.widget.*
import com.client.graphics.interfaces.MenuItem
import com.client.graphics.interfaces.RSInterface

object Slider {

    fun InterfaceBuilder.slider(builder: SliderComponent.(id : Int) -> Unit) {
        val bld = SliderComponent()
        builder.invoke(bld,id)
        bld.componentId = nextId()
        RSInterface.slider(bld.componentId,bld.min,bld.max, bld.icon,bld.background,bld.contentType)
        children.add(bld)
        bld.childType = WidgetType.SLIDER
        InterfaceData.components[bld.componentId] = InterfaceData.WidgetData(bld.componentId, emptyList(), WidgetType.SLIDER.toString())
    }

    @Markers
    open class SliderComponent : InterfaceComponent() {

        //double min, double max, int icon, int background, int contentType

        var min = 0.6
        var max = 10.0
        var icon = 37
        var background = 54
        var contentType = 3

        fun min(bld: () -> Double) {
            this.min = bld()
        }
        fun max(bld: () -> Double) {
            this.max = bld()
        }

        fun icon(bld: () -> Int) {
            this.icon = bld()
        }

        fun background(bld: () -> Int) {
            this.background = bld()
        }

        fun contentType(bld: () -> Int) {
            this.contentType = bld()
        }

    }


}