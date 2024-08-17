package com.client.draw.widget.components

import com.client.RSFont
import com.client.StringUtils
import com.client.TextDrawingArea
import com.client.draw.widget.*
import com.client.graphics.interfaces.RSInterface
import com.util.Position

object Text {


    /**
     * Stores the id and height of the 4 different font types
     * @param id - font index id
     * @param height - font height
     */
    enum class FontType(val id: Int, val height: Int) {
        SMALL(0, 9), REGULAR(1, 11), BOLD(2, 11), FANCY(3, 12),FANCY_LARGE(4,18);
    }

    fun InterfaceBuilder.text(builder: TextComponent.() -> Unit) {
        val bld = TextComponent()
        builder.invoke(bld)
        bld.componentId = nextId()
        bld.childType = WidgetType.TEXT
        addText(bld.componentId, bld.text, RSInterface.defaultTextDrawingAreas, bld.type.id, bld.color, bld.center, bld.shadow,bld.lineSpace,bld.width,bld.tooltips,bld.hover)
        children.add(bld)
        InterfaceData.components[bld.componentId] = InterfaceData.WidgetData(bld.componentId, emptyList(), WidgetType.TEXT.toString())
    }

    fun InterfaceBuilder.text(component : TextComponent) {
        component.componentId = nextId()
        addText(component.componentId, component.text, RSInterface.defaultTextDrawingAreas, component.type.id, component.color, component.center, component.shadow, component.lineSpace, component.width,component.tooltips,component.hover)
        component.childType = WidgetType.TEXT
        children.add(component)
        InterfaceData.components[component.componentId] = InterfaceData.WidgetData(component.componentId, emptyList(), WidgetType.TEXT.toString())
    }

    fun InterfaceBuilder.texts(amount: Int, builder: TextComponent.(Int) -> Unit) {
        repeat(amount) { text { builder(it) } }
    }

    fun InterfaceBuilder.texts(amount: Int, rowSize: Int, builder: TextComponent.(Int, Int) -> Unit) {
        repeat(amount) { text { builder(it % rowSize, Math.floorDiv(it, rowSize)) } }
    }

    fun InterfaceBuilder.texts(amount: Int, rowSize: Int, padX: Int = 0, padY: Int = 0, builder: TextComponent.(id : Int) -> Unit) {
        repeat(amount) {
            val bld = TextComponent()
            val row = (it % rowSize)
            val col = Math.floorDiv(it, rowSize)
            val width = RSInterface.newFonts[bld.type.id].getTextWidth(bld.text) + padX
            val height = bld.type.height + padY

            builder.invoke(bld,it)
            bld.componentId = nextId()
            bld.position = Position(bld.position.x + (row * width), bld.position.y + (col * height))
            bld.childType = WidgetType.TEXT

            addText(bld.componentId, bld.text, RSInterface.defaultTextDrawingAreas, bld.type.id, bld.color, bld.center, bld.shadow,bld.lineSpace,bld.width,bld.tooltips,bld.hover)
            children.add(bld)
            InterfaceData.components[bld.componentId] = InterfaceData.WidgetData(bld.componentId, emptyList(), WidgetType.TEXT.toString())
        }
    }

    fun addText(
        id: Int,
        text: String?,
        tda: Array<TextDrawingArea>,
        idx: Int,
        color: Int,
        center: Boolean,
        shadow: Boolean,
        lineSpace: Int,
        width: Int,
        tooltips: List<String>
    ) {
        RSInterface.interfaceCache[id] = RSInterface()
        val tab = RSInterface.interfaceCache[id]
        tab.parentID = id
        tab.id = id
        tab.type = 4
        tab.atActionType = 1
        tab.width = 0
        tab.contentType = 0
        tab.opacity = 0
        tab.hoverType = -1
        tab.fontType = FontType.values().first { it.id == idx }
        tab.centerText = center
        tab.textShadow = shadow
        tab.textDrawingAreas = tda[idx]
        tab.message = if (width == 0) text else StringUtils.getWrappedText(tda[idx], text, width)
        tab.aString228 = ""
        tab.textColor = color
        tab.secondaryColor = 0
        tab.defaultHoverColor = 0
        tab.secondaryHoverColor = 0
        tab.lineSpace = lineSpace
        tab.wrapSize = width
        if (!tooltips.isEmpty()) {
            tab.width = tda[idx].getTextWidth(text)
            tab.height = FontType.values()[idx].height
            tab.atActionType = 1
            tab.tooltips = tooltips.toTypedArray()
        }
    }

    fun addText(id: Int, text: String, tda: Array<TextDrawingArea>, idx: Int, color: Int, center: Boolean, shadow: Boolean, lineSpace: Int, width: Int, tooltips: List<String>, hover: Int) {
        RSInterface.interfaceCache[id] = RSInterface()
        val tab = RSInterface.interfaceCache[id]
        tab.parentID = id
        tab.id = id
        tab.type = 4
        tab.atActionType = 43535
        tab.contentType = 0
        tab.opacity = 0
        tab.hoverType = -1
        tab.centerText = center
        tab.fontType = FontType.values().filter { it.id == idx }.first()
        tab.textShadow = shadow
        tab.textDrawingAreas = tda[idx]
        tab.message = if (width == 0) text else StringUtils.getWrappedText(tda[idx], text, width)
        tab.aString228 = ""
        tab.textColor = color
        tab.secondaryColor = hover
        tab.defaultHoverColor = hover
        tab.secondaryHoverColor = hover
        tab.defaultHoverColor = hover
        tab.lineSpace = lineSpace
        tab.wrapSize = width
        if (tooltips.isNotEmpty()) {
            tab.width = width
            tab.height = 11
            tab.atActionType = RSInterface.TYPE_TOOLTIP
            tab.tooltips = tooltips.toTypedArray()
        }
    }

    @Markers
    open class TextComponent : InterfaceComponent() {
        var text = ""
        var type = FontType.SMALL
        var color = 0
        var hover = 0
        var center = true
        var shadow = true
        var lineSpace = 0
        var width = 0
        var tooltips : MutableList<String> = emptyList<String>().toMutableList()
        fun text(bld: () -> String) {
            this.text = bld()
        }
        fun type(bld: () -> FontType) {
            this.type = bld()
        }
        fun color(bld: () -> Int) {
            this.color = bld()
        }
        fun hover(bld: () -> Int) {
            this.hover = bld()
        }
        fun space(bld: () -> Int) {
            this.lineSpace = bld()
        }
        fun center(bld: () -> Boolean) {
            this.center = bld()
        }
        fun shadow(bld: () -> Boolean) {
            this.shadow = bld()
        }
        fun width(bld: () -> Int) {
            this.width = bld()
        }
        fun tooltip(bld: () -> String) {
            this.tooltips.add(bld())
        }

        fun tooltips(bld: () -> List<String>) {
            this.tooltips.addAll(bld())
        }

    }


}