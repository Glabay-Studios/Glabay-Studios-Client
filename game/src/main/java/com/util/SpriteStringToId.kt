package com.util

import com.client.sign.Signlink
import com.google.gson.GsonBuilder
import java.io.File

object SpriteStringToId {

    val ENABLED = true

    val spriteMapped = emptyMap<String,Int>().toMutableMap()
    var STARTING_ID = 0

    fun init() {
        val sourceDir = File(Signlink.getCacheDirectory(), "sprites")
        val destinationDir = File(Signlink.getCacheDirectory(), "toPack/")

        sourceDir.walk().forEach { file ->
            // Convert the extension to lowercase for case-insensitive comparison
            val extension = file.extension.lowercase()

            // Check if the file is a PNG or JPEG
            if (extension == "png" || extension == "jpg" || extension == "jpeg") {
                val uid = file.path.replace(sourceDir.absolutePath,"").replace(".png","").replaceFirst("/","")
                val destinationFile = File(destinationDir, "$STARTING_ID.png")
                file.copyTo(destinationFile, true)
                spriteMapped[uid.replace("\\","/")] = STARTING_ID
                STARTING_ID++
            }
        }

        println("Write File")
        val json = GsonBuilder().setPrettyPrinting().create()
        File("./game/src/main/resources/spriteMappings.json").writeText(json.toJson(spriteMapped))
    }

}

fun main() {
    SpriteStringToId.init()
}
