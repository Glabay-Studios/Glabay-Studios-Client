package com.client.draw.login.worlds

import com.client.Client
import com.client.Rasterizer2D
import com.client.draw.ImageCache
import com.client.draw.login.LoginState
import com.client.engine.GameEngine
import com.client.engine.impl.MouseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.util.AssetUtils
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import java.lang.reflect.Type

object WorldManager {

    var CLIENT = OkHttpClient()
    var GSON: Gson = Gson()

    private const val LOAD_WORLDS_ONLINE = false
    val WORLD_DATA_URL = ""

    var loadedWorlds: Boolean = false
    var worldList: MutableList<WorldData> = emptyList<WorldData>().toMutableList()
    var worldStatusText = "Click to switch"
    var selectedWorld: WorldData? = null

    fun openWorldSectionScreen(switchView: Boolean = false) {
        worldStatusText = "Loading..."
        var worldContent = ""
        if (LOAD_WORLDS_ONLINE) {
            try {
                CLIENT.newCall(Request.Builder().url(WORLD_DATA_URL.toHttpUrlOrNull()?.newBuilder()?.build()!!).build()).execute().use { res ->
                    if (res.body != null) {
                        worldContent = res.body!!.string()
                    } else {
                        worldStatusText = "Error"
                        loadedWorlds = false
                    }
                }
            } catch (e: IOException) {
                worldStatusText = "Error"
                loadedWorlds = false
            }
        } else {
            worldContent = AssetUtils.getResource("worldData.json").openStream().reader().readText()
        }


        val types: Type = object : TypeToken<ArrayList<WorldData?>?>() {}.type
        worldList = GSON.fromJson<ArrayList<WorldData>>(worldContent, types)
        worldStatusText = "Click to switch"
        loadedWorlds = true
        if (switchView) {
            Client.instance.loginScreen.loginState = LoginState.WORLD_SELECT
        }
    }

    fun renderWorldSelect() {
        Rasterizer2D.drawBox(0, 0, GameEngine.canvasWidth, GameEngine.canvasHeight, 0x000000)
        ImageCache.getLazy(27).drawAdvancedSprite(0, 0)

        if (Client.instance.newclickInRegion(GameEngine.canvasWidth - 57, 4, 50, 16)) {
            Client.instance.loginScreen.loginState = LoginState.LOGIN
        }

        val worldListX = 56
        val worldListY = 35

        var xOffset = 0
        var yOffset = 0

        worldList.forEachIndexed { index, world ->

            val worldButtonX = worldListX + xOffset
            val worldButtonY = worldListY + yOffset

            ImageCache.getLazy(31).drawSprite(worldButtonX, worldButtonY)

            if(Client.instance.newclickInRegion(worldButtonX,worldButtonY,ImageCache.getLazy(31))) {
                Client.server = world.ip
                Client.instance.loginScreen.loginState = LoginState.LOGIN
            }

            if (Client.instance.newmouseInRegion(worldButtonX, worldButtonY, ImageCache.getLazy(31))) {
                drawTooltip(world)
            }

            Client.instance.newBoldFont.drawBasicString(world.name, worldButtonX + 1, worldButtonY + 14, 0x111111,0)

            Client.instance.newSmallFont.drawCenteredString(
                "?",
                worldButtonX + 37 + (37 / 2), worldButtonY + 15,
                0xFFFFFF, 0
            )

            if (index % 7 == 0) {
                yOffset += 19
                xOffset = 0
            } else {
                xOffset += 94
                yOffset = 0
            }
        }
    }

    fun drawTooltip(world: WorldData) {
        val height = (10 * world.description.split("<br>").size) + 4
        var width = 2
        world.description.split("<br>").forEach {
            val textWidth = Client.instance.smallText.getTextWidth(it)
            if (textWidth >= width) {
                width = textWidth
            }
        }

        width += 6

        val x = MouseHandler.mouseX - (width / 2)
        val y = MouseHandler.mouseY + 20

        Rasterizer2D.drawBoxOutline(x, y, width + 2, height + 2, 0x000000)
        Rasterizer2D.drawBox(x + 1, y + 1, width, height, 0xFFFFA0)
        world.description.split("<br>").forEachIndexed { index, text ->
            Client.instance.newSmallFont.drawCenteredString(text, x + (width / 2), y + 12 + (10 * index), 0x111111,0)
        }

    }

}