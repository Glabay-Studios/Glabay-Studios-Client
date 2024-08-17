package com.client.settings

import com.client.draw.login.LoginBackground
import com.google.gson.Gson
import net.runelite.client.RuneLite
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class SettingManager(
    var accounts: MutableMap<String, AccountData> = mutableMapOf(),
    var rememberMe: Boolean = true,
    var disableLoginScreenMusic: Boolean = true,
    var soundEffectVolume: Int = 127,
    var areaSoundEffectVolume: Int = 127,
    var musicVolume: Int = 127,
    var rememberUsername: Boolean = true,
    var hiddenUsername : Boolean = false,
    var enableMusic : Boolean = true,
    var eulaAccepted : Boolean = false,
    var loginBackground : LoginBackground = LoginBackground.NORMAL
) {

    companion object {
        private val settingSaveLocation = File(RuneLite.PROFILES_DIR, "settings.json")
        private val logger: Logger = LoggerFactory.getLogger(SettingManager::class.java)

        fun load(): SettingManager {
            return runCatching {
                if (settingSaveLocation.exists()) {
                    settingSaveLocation.readText().let { json ->
                        Gson().fromJson(json, SettingManager::class.java)?.let { loadedSettings ->
                            SettingManager(loadedSettings.accounts, loadedSettings.rememberMe)
                        } ?: run {
                            logger.warn("Failed to parse settings from JSON.")
                            null
                        }
                    }
                } else null
            }.getOrNull() ?: SettingManager()
        }

        fun save(data : SettingManager) {
            runCatching {
                settingSaveLocation.parentFile?.mkdirs() // Ensure parent directories exist.
                val settingData = data
                settingSaveLocation.writeText(Gson().toJson(settingData))
            }.onFailure {
                logger.error("Failed to save settings.", it)
            }
        }
    }
}