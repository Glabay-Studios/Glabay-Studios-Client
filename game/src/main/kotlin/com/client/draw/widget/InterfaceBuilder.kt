package com.client.draw.widget

import com.client.Client
import com.client.draw.widget.InterfaceData.components
import com.client.draw.widget.InterfaceData.widgetInfo
import com.client.graphics.interfaces.RSInterface
import com.util.Position
import net.runelite.api.widgets.Widget
import java.awt.Dimension
import kotlin.system.exitProcess

@DslMarker
private annotation class InterfaceBuilderDslMarker

@DslMarker
private annotation class InterfaceComponentBuilderDslMarker

enum class InterfaceType { MAIN_SCREEN, TAB, CHATBOX, FULL_SCREEN }

object InterfaceData {


    data class WidgetInfo(val id : Int, val name : String)
    data class WidgetData(val id : Int, val childs : List<WidgetData>, val name : String)

    val widgetInfo = emptyMap<WidgetInfo, List<WidgetData>>().toMutableMap()
    val components = emptyMap<Int, WidgetData>().toMutableMap()

}

@InterfaceBuilderDslMarker
open class InterfaceBuilder(var id: Int, initWidth : Int, initHeight : Int,type: InterfaceType = InterfaceType.MAIN_SCREEN) : InterfaceComponent() {

    var name: String = ""

    /**
     * Parent interface
     */
    val widget : RSInterface = when (type) {
        InterfaceType.FULL_SCREEN -> RSInterface.addTabInterface(id)
        InterfaceType.MAIN_SCREEN -> RSInterface.addTabInterface(id)
        InterfaceType.CHATBOX -> RSInterface.addTabInterface(id)
        InterfaceType.TAB -> RSInterface.addTabInterface(id)
    }

    /**
     * All child widgets attached to this interface
     */
    val children = linkedSetOf<InterfaceComponent>()

    fun setAnchors(builder: () -> List<AnchorLocation>) {
        if(builder().size < 2) {
            println("You should only be using 2 Anchors Max [Fixed, Resizable]")
        }

        widget.anchorLocation = builder()
    }

    fun setAnchor(builder: () -> AnchorLocation) {
        widget.anchorLocation = listOf(builder())
    }

    fun setSize(builder: () -> Dimension) {
        val size = builder()
        widget.width = size.width
        widget.height = size.height
    }


    fun setName(builder: () -> String) { this.name = builder() }

    private fun totalChildren(): Int {
        var total = children.size
        children.filterIsInstance<InterfaceScroll>().forEach { total += (it).children.size }
        return total
    }

    fun nextId() = totalChildren() + 1 + id

}

@InterfaceBuilderDslMarker
open class InterfaceScroll(startID: Int, var width: Int, var height: Int, var max: Int) : InterfaceBuilder(startID,0,0)

fun InterfaceBuilder.scroll(width: Int, height: Int, max: Int,newScroll : Boolean = false, builder: InterfaceScroll.() -> Unit) {

    val list : MutableList<InterfaceData.WidgetData> = emptyList<InterfaceData.WidgetData>().toMutableList()

    val bld = InterfaceScroll(nextId(), width, height, max)
    bld.widget.scrollMax = max
    bld.widget.width = width
    bld.widget.height = height
    bld.widget.isScroll = true
    bld.widget.newScroller = newScroll
    bld.componentId = nextId()
    builder.invoke(bld)

    bld.widget.totalChildren(bld.children.size)

    bld.children.forEachIndexed { idx, child ->
        overlappingId(child.componentId)
        RSInterface.setBounds(child.componentId, child.position.x, child.position.y, idx, bld.widget)
        list.add(InterfaceData.WidgetData(child.componentId, emptyList(), child.childType.toString()))
    }
    components[bld.componentId] = InterfaceData.WidgetData(bld.componentId, list, WidgetType.SCROLL.toString())
    children.add(bld)
}

@InterfaceComponentBuilderDslMarker
open class InterfaceComponent {
    var componentId: Int = 0
    var size: Dimension = Dimension()
    var position: Position = Position(0, 0)
    var childType : WidgetType = WidgetType.SPRITE
    fun position(bld: () -> Position) {
        this.position = bld()
    }

}

fun InterfaceBuilder.widget(builder: WidgetComponent.() -> Unit) {
    val bld = WidgetComponent()
    builder.invoke(bld)

    bld.componentId = bld.widgetid
    children.add(bld)
}

@Markers
open class WidgetComponent : InterfaceComponent() {
    var widgetid = 0
    fun widgetID(bld: () -> Int) {
        this.widgetid = bld()
    }
}

fun buildInterface(id: Int,intWidth  : Int = 0,intHeight : Int = 0 ,center : Boolean = false, type : InterfaceType = InterfaceType.MAIN_SCREEN, builder: InterfaceBuilder.() -> Unit): RSInterface {
    val bld = InterfaceBuilder(id, intWidth,intHeight,type)
    builder.invoke(bld)
    bld.widget.totalChildren(bld.children.size)

    var padX = 0
    var padY = 0

    if(center) {
        val width = if (Client.instance.viewportWidth == 0) 512 else Client.instance.viewportWidth
        val height = if (Client.instance.viewportHeight == 0) 334 else Client.instance.viewportHeight
        padX = (width - intWidth) / 2
        padY = (height - intHeight) / 2
    }
    val data : MutableList<InterfaceData.WidgetData> = emptyList<InterfaceData.WidgetData>().toMutableList()

    bld.children.forEachIndexed { idx, child ->
        if(components.containsKey(child.componentId)) {
            data.add(components[child.componentId]!!)
        }
        overlappingId(child.componentId)
        RSInterface.setBounds(child.componentId, padX + child.position.x, padY + child.position.y, idx, bld.widget)
    }

    if(bld.name != "") {
        widgetInfo[InterfaceData.WidgetInfo(id, bld.name)] = data
    }

    return bld.widget
}

fun buildInterface(id: Int, offsetX : Int = 0, offsetY : Int = 0, type : InterfaceType = InterfaceType.MAIN_SCREEN,builder: InterfaceBuilder.() -> Unit): RSInterface {
    val bld = InterfaceBuilder(id,0,0,type)
    builder.invoke(bld)
    bld.widget.totalChildren(bld.children.size)
    val data : MutableList<InterfaceData.WidgetData> = emptyList<InterfaceData.WidgetData>().toMutableList()

    bld.children.forEachIndexed { idx, child ->
        if(components.containsKey(child.componentId)) {
            data.add(components[child.componentId]!!)
        }
        overlappingId(child.componentId)
        RSInterface.setBounds(child.componentId, offsetX + child.position.x, offsetY + child.position.y, idx, bld.widget)
    }
    if(bld.name != "") {
        widgetInfo[InterfaceData.WidgetInfo(id, bld.name)] = data
    }
    return bld.widget
}

fun overlappingId(id : Int) {
    if (RSInterface.interfaceCache[id] != null) {
        println("Overlapping Ids: ${id}")
    }
}