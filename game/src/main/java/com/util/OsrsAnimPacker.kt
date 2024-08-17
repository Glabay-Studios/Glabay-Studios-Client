package com.util

import com.displee.cache.CacheLibrary
import io.netty.buffer.Unpooled
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.zip.Deflater
import java.util.zip.GZIPOutputStream

object OsrsAnimPacker {

    private const val GRAPHICS = true
    private const val FRAME_BASES = true
    private const val FRAMES = true
    private const val ANIMATIONS = true

    fun pack(cacheFrom: CacheLibrary, cacheTo: CacheLibrary) {
        if (GRAPHICS) graphics(cacheFrom, cacheTo)
        if (FRAME_BASES) frameBases(cacheFrom, cacheTo)
        if (FRAMES) frames(cacheFrom, cacheTo)
        if (ANIMATIONS) animations(cacheFrom, cacheTo)
    }


    private fun graphics(cacheFrom: CacheLibrary, cacheTo: CacheLibrary) {
        var highestFileId = -1
        var biggestSize = 0
        val buf = Unpooled.buffer()
        buf.writeShort(0)

        val configIndex = cacheFrom.index(2)
        configIndex.cache()

        val fromArchive = configIndex.archive(13)!!
        for (file in fromArchive.files()) {
            val data = file.data!!
            val dataSize = data.size
            if (dataSize < 1) {
                println("skipped spotanim file ${file.id} (no data)")
                buf.writeShort(-1)
            } else {
                val fileId = file.id
                if (dataSize >= 65535) throw IllegalStateException("TOO LARGE GRAPHIC! ${fromArchive.id}:$fileId size was $dataSize")
                buf.writeShort(fileId)
                buf.writeShort(dataSize)
                buf.writeBytes(data)
                if (fileId > highestFileId)
                    highestFileId = fileId
                if (dataSize > biggestSize)
                    biggestSize = dataSize
            }
        }

        buf.writeShort(-1)

        buf.setShort(0, highestFileId)

        buf.readerIndex(0)
        val array = ByteArray(buf.readableBytes())
        buf.readBytes(array)

        println("spotanim highest $highestFileId and biggest was $biggestSize (total bytes=${array.size})")

        cacheTo.put(0, 2, "spotanim.dat", array)

        cacheTo.index(0).update()
    }

    private fun frameBases(cacheFrom: CacheLibrary, cacheTo: CacheLibrary) {
        val indexFrom = cacheFrom.index(1)
        indexFrom.cache()

        val buf = Unpooled.buffer()
        var count = 0
        buf.writeShort(count) // placeholder

        for (archive in indexFrom.archives()) {
            val groupId = archive.id
            if (!archive.containsData()) throw IllegalStateException("MUST HAVE DATA! $groupId")
            val file = archive.file(0)!!
            val data = file.data!!
            val dataSize = data.size
            if (dataSize >= 65535) throw IllegalStateException("TOO LARGE DATA! $groupId size $dataSize")
            buf.writeShort(groupId)
            buf.writeShort(dataSize)
            buf.writeBytes(data)
            count++
        }

        buf.setShort(0, count)

        buf.readerIndex(0)
        val array = ByteArray(buf.readableBytes())
        buf.readBytes(array)

        val compressedArray = compress(array)

        cacheTo.put(0, 2, "framebases.dat", compressedArray)

        cacheTo.index(0).update()

        println("frame bases count $count and raw size ${array.size}, compressed size ${compressedArray.size}")
    }

    private fun frames(cacheFrom: CacheLibrary, cacheTo: CacheLibrary) {
        val indexFrom = cacheFrom.index(0)
        indexFrom.cache()

        val indexTo = cacheTo.index(2)
        indexTo.clear()

        var count = 0
        for (archive in indexFrom.archives()) {
            if (!archive.containsData()) continue

            val buf = Unpooled.buffer()
            var highestFileId = 0
            buf.writeShort(highestFileId) // placeholder

            for (file in archive.files()) {
                val fileId = file.id
                val data = file.data!!
                buf.writeShort(fileId)
                buf.writeMedium(data.size)
                buf.writeBytes(data)
                if (fileId > highestFileId)
                    highestFileId = fileId
            }

            buf.setShort(0, highestFileId)

            buf.readerIndex(0)
            val array = ByteArray(buf.readableBytes())
            buf.readBytes(array)

            cacheTo.put(2, archive.id, array)

            count++
        }

        indexTo.update()

        println("frames count $count")
    }

    private fun animations(cacheFrom: CacheLibrary, cacheTo: CacheLibrary) {
        var highestFileId = -1
        var biggestSize = 0
        val buf = Unpooled.buffer()
        buf.writeShort(0)

        val configIndex = cacheFrom.index(2)
        configIndex.cache()

        val fromArchive = configIndex.archive(12)!!
        for (file in fromArchive.files()) {
            val data = file.data!!
            val dataSize = data.size
            if (dataSize < 1) {
                throw IllegalStateException("skipped seq file ${file.id} (no data)")
                buf.writeShort(-1)
            } else {
                val fileId = file.id
                buf.writeShort(fileId)
                if (dataSize >= 65535) throw IllegalStateException("TOO LARGE DATA! ${fromArchive.id}:$fileId size $dataSize")
                buf.writeShort(dataSize)
                buf.writeBytes(data)
                if (fileId > highestFileId)
                    highestFileId = fileId
                if (data.size > biggestSize)
                    biggestSize = data.size

                //println("seq $fileId length ${data.size}")
            }
        }

        buf.setShort(0, highestFileId)

        buf.readerIndex(0)
        val array = ByteArray(buf.readableBytes())
        buf.readBytes(array)

        val compressedArray = compress(array)

        println("seq highest $highestFileId and biggest was $biggestSize (total bytes=${array.size}, compressed bytes=${compressedArray.size})")

        cacheTo.put(0, 2, "seq.dat", compressedArray)

        cacheTo.index(0).update()
    }

    fun compress(array: ByteArray, compressionLevel: Int = Deflater.BEST_COMPRESSION): ByteArray {
        ByteArrayOutputStream().use { bout ->
            object : GZIPOutputStream(bout) {
                init {
                    def.setLevel(compressionLevel)
                }
            }.use { os ->
                os.write(array)
                os.finish()
            }
            return bout.toByteArray()
        }
    }


}

fun main() {
    OsrsAnimPacker.pack(
        CacheLibrary("C:\\Users\\Home\\.xeros-custom\\cache\\osrs"),
        CacheLibrary("C:\\Users\\Home\\.xeros-custom\\cache")
    )
}
