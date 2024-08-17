package com.client.draw.widget

enum class WidgetType {
    TEXT,
    SPRITE,
    BACKGROUND,
    CHAR,
    DIVIDER,
    BUTTON,
    DROPDOWN,
    HEAD,
    INPUT,
    ITEM,
    PROGRESS,
    SLIDER,
    RASTERIZER,
    SPRITES,
    TEST,
    TOOLTIPS,
    PLAYER,
    MODEL,
    SCROLL;

    override fun toString(): String {
        return name.toLowerCase().capitalize()
    }

}