package com.blake8090.circuitbreak.engine

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.files.FileHandle
import com.blake8090.circuitbreak.engine.ioc.Component

@Component
class FileUtil(private val provider: FileHandlerProvider) {
    fun getFilesInPath(path: String, recursive: Boolean = false): List<FileHandle> {
        val results = mutableListOf<FileHandle>()
        getFilesInPath(
            provider.getFileHandle(path),
            results,
            recursive
        )
        return results
    }

    private fun getFilesInPath(handle: FileHandle, results: MutableList<FileHandle>, recursive: Boolean = false) {
        if (!handle.exists() || !handle.isDirectory) {
            return
        }
        handle.list().forEach {
            // we only want file handles in the results, not directories
            if (!it.isDirectory) {
                results.add(it)
            }

            if (recursive) {
                getFilesInPath(it, results, recursive)
            }
        }
    }
}

/**
 * Wrapper around the LibGDX static method for obtaining file handles, to facilitate unit testing
 */
@Component
class FileHandlerProvider {
    fun getFileHandle(path: String): FileHandle = Gdx.files.internal(path)
}
