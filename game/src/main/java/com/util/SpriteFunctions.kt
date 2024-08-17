package com.util

import com.client.Client
import com.client.Sprite

enum class Action {
    CLICK, HOVER
}

fun Sprite.drawAdvancedSprite(x: Int, y: Int, action: Action, task: () -> Unit) {
    this.drawAdvancedSprite(x, y)
    fireEvent(x, y, action, this,task)
}

fun Sprite.drawAdvancedSprite(x: Int, y: Int, sprite: Sprite, action: Action, task: () -> Unit) {
    if (Client.instance.newmouseInRegion(x, y, sprite)) {
        sprite.drawAdvancedSprite(x, y)
    } else {
        this.drawAdvancedSprite(x, y)
    }
    fireEvent(x, y, action, this,task)
}

fun Sprite.drawAdvancedSprite(x: Int, y: Int, sprite: Sprite) {
    if (Client.instance.newmouseInRegion(x, y, sprite)) {
        sprite.drawAdvancedSprite(x, y)
    } else {
        this.drawAdvancedSprite(x, y)
    }
}

fun Sprite.drawSprite(x: Int, y: Int, action: Action, task: () -> Unit) {
    this.drawSprite(x, y)
    fireEvent(x, y, action, this,task)
}

fun Sprite.drawSprite(x: Int, y: Int, sprite: Sprite, action: Action, task: () -> Unit) {
    if (Client.instance.newmouseInRegion(x, y, sprite)) {
        sprite.drawSprite(x, y)
    } else {
        this.drawSprite(x, y)
    }
    fireEvent(x, y, action, this,task)
}

fun Sprite.drawSprite(x: Int, y: Int, sprite: Sprite) {
    if (Client.instance.newmouseInRegion(x, y, sprite)) {
        sprite.drawSprite(x, y)
    } else {
        this.drawSprite(x, y)
    }
}

private fun fireEvent(x: Int, y: Int, action: Action, sprite: Sprite, task: () -> Unit) {
    when (action) {
        Action.CLICK -> {
            if (Client.instance.newclickInRegion(x, y, sprite)) {
                task.invoke()
            }
        }
        Action.HOVER -> {
            if (Client.instance.newclickInRegion(x, y, sprite)) {
                task.invoke()
            }
        }
    }
}