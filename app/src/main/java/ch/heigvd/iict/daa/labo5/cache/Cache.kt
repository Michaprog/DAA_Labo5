package ch.heigvd.daa.labo5.cache

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.nio.file.Files


object Cache {
    private const val EXP_DELAY = 60 * 5000L
    private lateinit var cachingDir: File

    fun setDir(cacheDir: File) {
        Cache.cachingDir = cacheDir
        createCacheDir()
    }


    fun get(name: String): Bitmap? {
        val file = File(cachingDir, name)
        if (file.exists() && file.canRead() && file.length() != 0L && !isExpired(file)) {
            return BitmapFactory.decodeFile(file.path);
        }
        return null
    }

    fun set(name: String, image: Bitmap) = File(cachingDir, name).writeBitmap(image)

    fun clear() = cachingDir.deleteRecursively().also { createCacheDir() }

    private fun createCacheDir() {
        if (!cachingDir.exists())
            Files.createDirectory(cachingDir.toPath())
    }

    private fun isExpired(file: File) = file.lastModified() < System.currentTimeMillis() - EXP_DELAY

    private fun File.writeBitmap(bitmap: Bitmap, quality: Int = 100) {
        parentFile?.let {if (!it.exists()) it.mkdirs()}
        outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, it)
            it.flush()
        }
    }
}