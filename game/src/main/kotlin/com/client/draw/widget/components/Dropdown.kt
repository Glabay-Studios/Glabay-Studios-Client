package com.client.draw.widget.components

import com.client.draw.widget.*
import com.client.graphics.interfaces.MenuItem
import com.client.graphics.interfaces.RSInterface

object Dropdown {

    fun InterfaceBuilder.dropdown(builder: DropdownComponent.(id : Int) -> Unit) {
        val bld = DropdownComponent()
        builder.invoke(bld,id)
        bld.componentId = nextId()
        RSInterface.dropdownMenu(bld.componentId,bld.width,bld.defaultOption, bld.options.toTypedArray(),bld.menuItem,RSInterface.defaultTextDrawingAreas,bld.fontType.id)
        children.add(bld)
        bld.childType = WidgetType.DROPDOWN
        InterfaceData.components[bld.componentId] = InterfaceData.WidgetData(bld.componentId, emptyList(), WidgetType.DROPDOWN.toString())
    }

    @Markers
    open class DropdownComponent : InterfaceComponent() {
        var defaultOption = 0
        var width = 100
        var fontType : Text.FontType = Text.FontType.REGULAR
        var options : List<String> = emptyList()
        var menuItem : MenuItem? = null
        fun defaultOption(bld: () -> Int) {
            this.defaultOption = bld()
        }
        fun options(bld: () -> List<String>) {
            this.options = bld()
        }

        fun width(bld: () -> Int) {
            this.width = bld()
        }

        fun fontType(bld: () -> Text.FontType) {
            this.fontType = bld()
        }


        fun menuAction(bld: () -> MenuItem) {
            this.menuItem = bld()
        }
    }


}