package com.client.util

import com.client.Sprite

object MathUtilss {

    fun getPixelAmt(current : Int, pixels : Int) = (pixels * .01 * current).toInt()

    fun d1Tod2(array: IntArray, width: Int): Array<IntArray> {
        val newArray = Array(array.size / width) { IntArray(width) }
        for (i in array.indices) {
            newArray[i / width][i % width] = array[i]
        }
        return newArray
    }


    fun fillPixels(pixels: Array<IntArray>, color: Int, x: Int, y: Int, width: Int, height: Int) {
        for (j in y until height) {
            for (i in x until width) {
                pixels[j][i] = color
            }
        }
    }

    fun insertPixels(pixels: Array<IntArray>, image: Sprite, x: Int, y: Int, ignoreTransparency: Boolean) {
        val imagePixels: Array<IntArray> =  d1Tod2(image.myPixels, image.myWidth)
        for (j in y until y + image.myHeight) {
            for (i in x until x + image.myWidth) {
                if (ignoreTransparency && imagePixels[j - y][i - x] == 0) continue
                pixels[j][i] = imagePixels[j - y][i - x]
            }
        }
    }

    fun fillPixels(pixels: Array<IntArray>, image: Sprite, x: Int, y: Int, width: Int, height: Int) {
        val imagePixels: Array<IntArray> = d1Tod2(image.myPixels, image.myWidth)
        for (j in y until height) {
            for (i in x until width) {
                pixels[j][i] = imagePixels[(j - y) % image.myHeight][(i - x) % image.myWidth]
            }
        }
    }

    fun d2Tod1(array: Array<IntArray>): IntArray {
        val newArray = IntArray(array.size * array[0].size)
        for (i in array.indices) for (j in array[i].indices) {
            newArray[i * array[0].size + j] = array[i][j]
        }
        return newArray
    }


    fun insertPixels(pixels: Array<IntArray>, image: Sprite, x: Int, y: Int) {
        val imagePixels: Array<IntArray> = d1Tod2(image.myPixels, image.myWidth)
        for (j in y until y + image.myHeight) {
            for (i in x until x + image.myWidth) {
                pixels[j][i] = imagePixels[j - y][i - x]
            }
        }
    }

}