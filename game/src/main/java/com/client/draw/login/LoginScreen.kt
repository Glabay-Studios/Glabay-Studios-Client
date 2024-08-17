package com.client.draw.login

import com.client.*
import com.client.draw.ImageCache
import com.client.draw.login.LoginBackground.Companion.backGroundSprite
import com.client.draw.login.flames.FlameManager
import com.client.draw.login.worlds.WorldManager
import com.client.draw.login.worlds.WorldManager.openWorldSectionScreen
import com.client.draw.login.worlds.WorldManager.worldList
import com.client.engine.GameEngine
import com.client.settings.SettingManager
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.value.ObservableValue
import org.apache.commons.lang3.time.StopWatch
import kotlin.math.ceil
import kotlin.system.exitProcess


class LoginScreen(val client : Client) {

    var loginState = LoginState.LOADING
    var loginScreenCursorPos = 0
    private val flameManager = FlameManager()


    private var backgroundStopWatch: StopWatch? = null
    var opacity = 0
    private var backgroundSprite = LoginBackground.NORMAL.spriteID

    private val fadeToBlack: DoubleProperty = SimpleDoubleProperty(256.0)

    private val eulaText = listOf(
        "Before using this app, please read and accept our",
        "@gol@terms of use@whi@, @gol@privacy policy@whi@, and @gol@end user licence",
        "@gol@agreement (EULA)@whi@.",
        "By accepting, you agree to these documents."
    )

    val BACKGROUND_ACTIVE_TIME_SECONDS = 10


    fun setup() {
        fadeToBlack.addListener { observable: ObservableValue<out Number>?, oldVal: Number?, newVal: Number ->
            if (newVal.toDouble() >= 256) {
                backgroundSprite = backgroundSprite()
                backgroundStopWatch!!.reset()
                backgroundStopWatch!!.start()
            }
            if (newVal.toDouble() <= 0) {
                fadeToBlack.set(256.0)
            }

        }
    }

    private fun backgroundSprite() : Int {
        return when(client.settingManager.loginBackground) {
            LoginBackground.FADING_BACKGROUNDS -> LoginBackground.values().filter {
                it.spriteID != -1
            }.toList().shuffled().first().spriteID
            else -> client.settingManager.loginBackground.spriteID
        }
    }

    private fun handleBackgrounds() {
        when(client.settingManager.loginBackground) {
            LoginBackground.FADING_BACKGROUNDS -> {
                if(backgroundStopWatch == null) {
                    backgroundStopWatch = StopWatch()
                    backgroundStopWatch!!.start()
                }

                val end: Long = backgroundStopWatch!!.startTime + 1000L * BACKGROUND_ACTIVE_TIME_SECONDS
                val increment: Long = (end - backgroundStopWatch!!.startTime) / 100
                if (increment > 0) {
                    fadeToBlack.set(fadeToBlack.get() - 0.9)
                    opacity = ceil(fadeToBlack.get()).toInt()
                }
            }
            else -> backgroundSprite = backgroundSprite()
        }
    }

    fun draw() {
        when (loginState) {
            LoginState.LOADING -> drawLoadingScreen()
            else -> drawLoginScreen()
        }
    }

    private fun drawLoadingScreen() {
        val centerX = GameEngine.canvasWidth / 2
        val centerY = GameEngine.canvasHeight / 2

        LoginBackground.backGroundSprite.get(LoginBackground.NORMAL.spriteID)!!.drawSprite(0, 0)
        Client.LOGO.drawAdvancedSprite(centerX - (444 / 2),centerY - (GameEngine.canvasHeight / 2) + 17)

        val barWidth = 304

        val x: Int = centerX - (barWidth / 2)
        val y = centerY - (34 / 2)

        Rasterizer2D.drawBox(x, y, barWidth, 34, 0x8C1111)
        Rasterizer2D.drawBox(x + 1, y + 1, 302, 32, 0x000000)
        Rasterizer2D.drawBox(x + 2,y + 2,Client.instance.getPixelAmt(client.loadingPercent,300),30,0x8C1111);

        Client.instance.newBoldFont.drawCenteredString((client.loadingText + " - " + client.loadingPercent) + "%", (x + 1) + (302 / 2), y + 21, 0xFFFFFF,0)
        Client.instance.newBoldFont.drawCenteredString("Xeros" + " is loading - please wait...", (x) + (barWidth / 2), y - 14, 0xFFFFFF,0)
    }

    private fun drawLoginScreen() {
        if (WorldManager.selectedWorld == null) {
            openWorldSectionScreen()
            WorldManager.selectedWorld = worldList.first()
            WorldManager.selectedWorld = worldList.first()
        }

        val centerX = GameEngine.canvasWidth / 2
        val centerY = GameEngine.canvasHeight / 2
        val alpha = if (Client.instance.settingManager.loginBackground != LoginBackground.FADING_BACKGROUNDS) 226 else opacity
        handleBackgrounds()

        Rasterizer2D.drawBox(0,0,GameEngine.canvasWidth,GameEngine.canvasHeight,0x000000)
        if (backgroundSprite != -1) {
            backGroundSprite.get(backgroundSprite)!!.drawAdvancedSprite(centerX - (766 / 2),centerY - (503 / 2),alpha)
        }
        flameManager.draw(centerX - (766 / 2) -10, 0,Client.loopCycle,alpha)
        flameManager.draw(GameEngine.canvasWidth - 110, 0,Client.loopCycle,alpha)

        ImageCache.getLazy(0).drawAdvancedSprite(centerX - (444 / 2),centerY - (GameEngine.canvasHeight / 2) + 17)
        ImageCache.getLazy(1).drawSprite(centerX - (360 / 2),centerY - (200 / 2) + 21)

        val loginBoxX = centerX - (360 / 2)
        val loginBoxY = centerY - (200 / 2) + 21

        ImageCache.getLazy(if(Client.instance.settingManager.enableMusic) 25 else 26).drawAdvancedSprite(GameEngine.canvasWidth - 38 - 5,GameEngine.canvasHeight - 45 + 7)
        if(WorldManager.loadedWorlds) {
            ImageCache.getLazy(3).drawAdvancedSprite(centerX - (GameEngine.canvasWidth / 2) + 5,GameEngine.canvasHeight - 45 + 8)
            client.newBoldFont.drawCenteredString("World: ${WorldManager.selectedWorld?.name}", centerX - (GameEngine.canvasWidth / 2) + 5 + (100 / 2),GameEngine.canvasHeight - 45 + 23,0xFFFFFF,1)
            client.newSmallFont.drawCenteredString(WorldManager.worldStatusText, centerX - (GameEngine.canvasWidth / 2) + 5 + (100 / 2),GameEngine.canvasHeight - 45 + 38,0xFFFFFF,1)

        }

        when(loginState) {
            LoginState.EULA -> {
                eulaText.forEachIndexed { index, line ->
                    client.newRegularFont.drawCenteredString(line,loginBoxX + (360 / 2), loginBoxY + 43 + (20 * index),0xFFFFFF,1)
                }
                listOf("Accept","Decline").forEachIndexed { index, buttonText ->
                    val buttonX = loginBoxX + 28 + if(index == 1) 160 else 0
                    ImageCache.getLazy(2).drawSprite(buttonX,loginBoxY + 121)
                    client.newBoldFont.drawCenteredString(buttonText,buttonX + (147 / 2) - 1, loginBoxY + 146,0xFFFFFF,1)
                }
            }
            LoginState.WELCOME -> {

                client.newBoldFont.drawCenteredString("Welcome to Xeros",loginBoxX + (360 / 2), loginBoxY + 82,0xFFFF00,1)

                listOf("New User","Existing User").forEachIndexed { index, buttonText ->
                    val buttonX = loginBoxX + 28 + if(index == 1) 160 else 0
                    ImageCache.getLazy(2).drawSprite(buttonX - 1,loginBoxY + 100)
                    client.newBoldFont.drawCenteredString(buttonText,buttonX + (147 / 2) - 3, loginBoxY + 124,0xFFFFFF,1)
                }

            }
            LoginState.LOGIN -> {

                client.newBoldFont.drawCenteredString(client.firstLoginMessage, loginBoxX + (360 / 2), loginBoxY + 63 - 17, 0xFFFF00, 1)


                listOf("Login","Cancel").forEachIndexed { index, buttonText ->
                    val buttonX = loginBoxX + 28 + if(index == 1) 160 else 0
                    ImageCache.getLazy(2).drawSprite(buttonX,loginBoxY + 131)
                    client.newBoldFont.drawCenteredString(buttonText,buttonX + (147 / 2) - 1, loginBoxY + 156,0xFFFFFF,1)
                }

                client.newBoldFont.drawBasicString("Login: ", loginBoxX + 70, loginBoxY + 83, 0xFFFFFF, 1)
                client.newBoldFont.drawBasicString("Password: ", loginBoxX + 72, loginBoxY + 98, 0xFFFFFF, 1)

                client.newBoldFont.drawBasicString(
                    (if(!Client.instance.settingManager.hiddenUsername) client.myUsername else StringUtils.passwordAsterisks(client.myUsername)) + flash(0),
                    loginBoxX + 110, loginBoxY + 83,
                    0xFFFFFF, 1
                )

                client.newBoldFont.drawBasicString(
                    StringUtils.passwordAsterisks(client.myPassword) + flash(1),
                    loginBoxX + 123 + 20, loginBoxY + 98,
                    0xFFFFFF, 1
                )
                ImageCache.getLazy(if(!Client.instance.settingManager.rememberUsername) 21 else 23).drawHoverSprite(loginBoxX + 63,loginBoxY + 107,ImageCache.getLazy(if(!Client.instance.settingManager.rememberUsername) 22 else 24))
                ImageCache.getLazy(if(!Client.instance.settingManager.hiddenUsername) 21 else 23).drawHoverSprite(loginBoxX + 204,loginBoxY + 107,ImageCache.getLazy(if(!Client.instance.settingManager.hiddenUsername) 22 else 24))

                client.newSmallFont.drawBasicString(
                    "Remember Username",
                    loginBoxX + 63 + 22, loginBoxY + 119,
                    0xFFFF00, 1
                )

                client.newSmallFont.drawBasicString(
                    "Hide Username",
                    loginBoxX + 204 + 22, loginBoxY + 119,
                    0xFFFF00, 1
                )

            }
            LoginState.WORLD_SELECT -> WorldManager.renderWorldSelect()
            else -> {}
        }
    }

    fun handleInput() {

        val centerX = GameEngine.canvasWidth / 2
        val centerY = GameEngine.canvasHeight / 2
        val loginBoxX = centerX - (360 / 2)
        val loginBoxY = centerY - (200 / 2) + 21

        if(client.newclickInRegion(centerX - (GameEngine.canvasWidth / 2) + 5,GameEngine.canvasHeight - 45 + 8,ImageCache.getLazy(3))) {
            openWorldSectionScreen(true)
        }

        if(client.newclickInRegion(GameEngine.canvasWidth - 38 - 5,GameEngine.canvasHeight - 45 + 7,ImageCache.getLazy(25))) {
            client.settingManager.enableMusic = !client.settingManager.enableMusic
        }

        when(loginState) {
            LoginState.EULA -> {
                repeat(2) {
                    val buttonX = loginBoxX + 28 + if(it == 1) 160 else 0
                    if(client.newclickInRegion(buttonX + (147 / 2) - 1, loginBoxY + 146,ImageCache.getLazy(2))) {
                        when(it) {
                            0 -> {
                                loginState = LoginState.LOGIN
                                client.settingManager.eulaAccepted = true
                                SettingManager.save(client.settingManager)
                            }
                            1 -> exitProcess(0)
                        }
                    }
                }
            }
            LoginState.WELCOME -> {
                repeat(2) {
                    val buttonX = loginBoxX + 28 + if(it == 1) 160 else 0
                    if(client.newclickInRegion(buttonX - 1,loginBoxY + 100,ImageCache.getLazy(2))) {
                        when(it) {
                            0 -> client.launchURL("https://www.google.com/")
                            1 -> loginState = LoginState.LOGIN
                        }
                    }
                }
            }
            LoginState.LOGIN -> {
                repeat(2) {
                    val buttonX = loginBoxX + 28 + if (it == 1) 160 else 0
                    if (client.newclickInRegion(buttonX, loginBoxY + 131, ImageCache.getLazy(2))) {
                        when (it) {
                            0 -> client.login(client.myUsername, client.myPassword, false)
                            1 -> loginState = LoginState.WELCOME
                        }
                    }
                }

                if (client.newclickInRegion(loginBoxX + 110, loginBoxY + 70, 200, 15)) {
                    loginScreenCursorPos = 0
                }

                if (client.newclickInRegion(loginBoxX + 140, loginBoxY + 87, 200, 15)) {
                    loginScreenCursorPos = 1
                }


                if (client.newclickInRegion(loginBoxX + 63, loginBoxY + 107, ImageCache.getLazy(21))) {
                    client.settingManager.rememberUsername = !client.settingManager.rememberUsername
                }

                if (client.newclickInRegion(loginBoxX + 204, loginBoxY + 107, ImageCache.getLazy(21))) {
                    client.settingManager.hiddenUsername = !client.settingManager.hiddenUsername
                }
                do {
                    while (true) {
                        label1776@ do {
                            while (true) {
                                while (Client.keyManager.hasNextKey()) {
                                    val l1 = Client.keyManager.lastTypedCharacter.code
                                    if (l1 == -1 || l1 == 96) break
                                    var flag1 = false
                                    for (element in validUserPassChars) {
                                        if (l1 != element.code) continue
                                        flag1 = true
                                        break
                                    }

                                    if (loginScreenCursorPos == 0) {
                                        client.myUsername = processLoginInput(client.myUsername, l1, flag1, 12, { loginScreenCursorPos = 1 }, { loginScreenCursorPos = 1 })
                                    } else if (loginScreenCursorPos == 1) {
                                        client.myPassword = processLoginInput(client.myPassword, l1, flag1, 15, { loginScreenCursorPos = 0 }, { client.login(client.myUsername, client.password, false) })
                                    }
                                }
                                return
                            }
                        } while (true)
                    }
                } while (true)
            }
            else -> {}
        }
    }


    private fun processLoginInput(currentInput: String, keyCode: Int, shouldAppendChar: Boolean, maxLength: Int, tabAction: Runnable?, enterAction: Runnable?): String {
        var updatedInput = currentInput

        if (keyCode == 8 && updatedInput.isNotEmpty()) { // Backspace
            updatedInput = updatedInput.substring(0, updatedInput.length - 1)
        } else if (keyCode == 9) { // Tab
            tabAction?.run()
        } else if (keyCode == 10 || keyCode == 13) { // Enter or Return
            enterAction?.run()
            return updatedInput
        }

        if (shouldAppendChar) updatedInput += keyCode.toChar()

        if (updatedInput.length > maxLength) {
            updatedInput = updatedInput.substring(0, maxLength)
        }

        return updatedInput
    }


    private val validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\u00a3$%^&*()-_=+[{]};:'@#~,<.>/?\\| "

    private fun flash(state: Int): String = if ((loginScreenCursorPos == state) and (Client.loopCycle % 40 < 20)) "|" else ""

    fun missingUsername(): Boolean {
        if (client.username == null || client.username.isEmpty()) {
            loginScreenCursorPos = 0
            return true
        }
        return false
    }

    fun missingPassword(): Boolean {
        if (client.password == null || client.password.isEmpty()) {
            loginScreenCursorPos = 0
            return true
        }
        return false
    }

}