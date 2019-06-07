package com.blake8090.circuitbreak.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle

/**
 * Simple wrapper around Gdx methods for handling files and folders.
 * Intended to be used as a field in classes that need to be unit tested
 */
class FileUtil {
    fun getFileHandle(path: String): FileHandle = Gdx.files.internal(path)

    fun getFilesInPath(path: String): List<FileHandle> {
        val handle = getFileHandle(path)
        if (!handle.exists() || !handle.isDirectory) {
            return emptyList()
        }
        val results = mutableListOf<FileHandle>()
        getFilesInPath(handle, results)
        return results
    }

    private fun getFilesInPath(handle: FileHandle, results: MutableList<FileHandle>) {
        if (handle.isDirectory) {
            handle.list().forEach { getFilesInPath(it, results) }
        } else {
            results.add(handle)
        }
    }
}
