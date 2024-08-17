package com.client.draw.widget

enum class AnchorLocation {

    /**
     * Interface places itself where user wants it
     */
    MOVABLE,

    /**
     * Interface places itself where it wants
     */
    STATIC,

    /**
     * Place Interface in the top left viewport area
     */
    TOP_LEFT,

    /**
     * Place Interface in the top center viewport area
     */
    TOP_CENTER,

    /**
     * Place Interface in the top right viewport area
     */
    TOP_RIGHT,

    /**
     * Place Interface in the top right viewport area including the minimap size
     */
    TOP_RIGHT_MINIMAP,

    /**
     * Place Interface in the bottom left viewport area
     */
    BOTTOM_LEFT,

    /**
     * Place Interface in the bottom right viewport area
     */
    BOTTOM_RIGHT,

    /**
     * Place Interface directly above right side of chatbox
     */
    ABOVE_CHATBOX_RIGHT,

    /**
     * Place Interface directly above left side of chatbox
     */
    ABOVE_CHATBOX_LEFT,

}