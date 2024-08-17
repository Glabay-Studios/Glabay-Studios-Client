package com.client.util

object AssetUtils {

    fun getResource(vararg dirs: String) = getResource(java.lang.String.join("/", *dirs), getClassLoaderForClass(this::class.java))!!

    fun getResource(resourceName: String) = getResource(resourceName, getClassLoaderForClass(this::class.java))

    fun getResource(resourceName: String?, classLoader: ClassLoader) = classLoader.getResource(resourceName)!!

    private fun getClassLoaderForClass(clazz: Class<*>): ClassLoader {
        return clazz.classLoader ?: return ClassLoader.getSystemClassLoader()
    }


}