package com.client.draw.widget

import com.client.draw.widget.impl.LoginScreenSettings
import com.client.draw.widget.impl.TeleportInterface
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object InterfaceLoader {

    var loaded : Boolean = false

    private val interfacesClass = listOf(
        TeleportInterface(),
        LoginScreenSettings()
    )


    @OptIn(DelicateCoroutinesApi::class)
    fun init() {

        GlobalScope.launch {
            val time = measureTimeMillis {
                interfacesClass.flatMap { it.subInterface }.map { GlobalScope.launch { it.invoke() } }.forEach {
                    it.join()
                }
                loaded = true
            }
            println("Interfaces read -> ${interfacesClass.size} (${time})")
        }


    }

}