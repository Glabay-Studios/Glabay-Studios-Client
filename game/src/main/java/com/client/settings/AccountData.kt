package com.client.settings

import com.client.Client
import com.client.Player
import com.client.PlayerRights
import com.client.draw.Rasterizer3D
import com.client.entity.model.Model
import kotlin.math.sin


data class AccountData(
    val rights: Array<PlayerRights> = emptyArray<PlayerRights>(),
    val lastLogin: Long = 0L,
    val username: String,
    val password: String,
    var appearanceColors: IntArray = IntArray(5),
    var equipment: IntArray = IntArray(12),
    var gender: Int = 0
) {

    init {
        //player.myGender = gender
        //player.equipment = equipment
        //player.appearanceColors = appearanceColors
    }

    fun drawHead(x : Int, y : Int) {



    }

    companion object {

        fun save() {
            update(
                AccountData(Client.localPlayer.rights, System.currentTimeMillis(), Client.instance.myUsername, Client.instance.myUsername,
                    Client.localPlayer.appearanceColors,
                    Client.localPlayer.equipment,
                    Client.localPlayer.myGender
                )
            )
        }

        fun update(data: AccountData) {
            with(Client.instance.settingManager.accounts) {
                data.appearanceColors = Client.localPlayer.appearanceColors
                data.equipment = Client.localPlayer.equipment
                data.gender = Client.localPlayer.myGender
                put(data.username, data)
            }
            SettingManager.save(Client.instance.settingManager)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AccountData

        if (!rights.contentEquals(other.rights)) return false
        if (lastLogin != other.lastLogin) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (!appearanceColors.contentEquals(other.appearanceColors)) return false
        if (!equipment.contentEquals(other.equipment)) return false
        if (gender != other.gender) return false

        return true
    }

    override fun hashCode(): Int {
        var result = rights.contentHashCode()
        result = 31 * result + lastLogin.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + appearanceColors.contentHashCode()
        result = 31 * result + equipment.contentHashCode()
        result = 31 * result + gender
        return result
    }
}