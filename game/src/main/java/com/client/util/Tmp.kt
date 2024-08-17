package com.client.util

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File


data class MapData(
    val land317 : Int,
    val map317 : Int,
    val landOSRS : String,
    val mapOSRS: String
)

lateinit var data: Map<Int,MapData>
lateinit var dataPre: Map<Int,MapData>

fun main() {
    val jsonContentAfterSHort = File("C:\\Users\\Home\\Desktop\\afterShort.json").readText(Charsets.UTF_8)
    val jsonContentBeforeSHort = File("C:\\Users\\Home\\Desktop\\beforeShort.json").readText(Charsets.UTF_8)
    // Define the correct type using TypeToken
    val mapDataType = object : TypeToken<Map<Int,MapData>>() {}.type

    // Deserialize the JSON string into a List of MapData
    data = Gson().fromJson(jsonContentAfterSHort, mapDataType)
    dataPre = Gson().fromJson(jsonContentBeforeSHort, mapDataType)


    val directory = File("C:\\Users\\Home\\Desktop\\MAPS_TO_PACL")

    walkDirectory(directory)


    val newIds : MutableMap<Int,String> = emptyMap<Int,String>().toMutableMap()

    dirToOldRegion.forEach {
        val newData = data[it.value]
        newIds[it.key.first] = newData!!.landOSRS
        newIds[it.key.second] = newData.mapOSRS
    }

    directory.walkTopDown().filter { it.isFile && it.extension == "gz" }.forEach {

        val newFile = File(it.parentFile, "${newIds[it.nameWithoutExtension.toInt()]}.gz")
        it.renameTo(newFile)
    }

    println(newIds)


}

val dirToOldRegion : MutableMap<Pair<Int,Int>, Int> = emptyMap<Pair<Int,Int>, Int>().toMutableMap()

fun walkDirectory(directory: File, parentPath: String = "") {
    // Check if the current file is indeed a directory
    if (directory.isDirectory) {
        // Update the path to include the current directory
        val currentPath = if (parentPath.isEmpty()) directory.name else "$parentPath/${directory.name}"

        // List all files and directories within the current directory
        val filesAndDirs = directory.listFiles()
        if (filesAndDirs != null) {
            // Filter and sort files that end with ".gz"
            val filteredFiles = filesAndDirs.filter { it.isFile && it.name.endsWith(".gz") }
                .sortedBy { it.name }

            // Pairing mechanism assuming the files are correctly sorted and named
            val pairs = mutableListOf<Pair<String, String>>()
            for (i in 0 until filteredFiles.size step 2) {
                if (i + 1 < filteredFiles.size) {
                    val firstFile = filteredFiles[i].nameWithoutExtension.split("_")[0]
                    val secondFile = filteredFiles[i + 1].nameWithoutExtension.split("_")[0]
                    pairs.add(Pair(firstFile, secondFile))
                }
            }

            // Print the pairs along with the subdirectory name
            for ((first, second) in pairs) {
                val regionID = dataPre.filter { it.value.map317 == second.toInt() }.keys.first()
                //println("${currentPath.split("/")[1]}: $first, $second, ${regionID}")
                dirToOldRegion[Pair(first.toInt(),second.toInt())] = regionID
            }

            // Process directories recursively
            filesAndDirs.filter { it.isDirectory }
                .forEach { subDir -> walkDirectory(subDir, currentPath) }
        }
    }
}