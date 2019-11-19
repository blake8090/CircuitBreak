package com.blake8090.circuitbreak.engine

import com.badlogic.gdx.files.FileHandle
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class FileUtilTest {
    @Test
    fun `Get all files in path`() {
        val rootDirectoryPath = "base"

        val rootFile1 = FileHandleMock("root1")
        val rootFile2 = FileHandleMock("root2")
        val rootDirectory = FileHandleMock(rootDirectoryPath, true, arrayOf(rootFile1, rootFile2))

        val provider = mock(FileHandlerProvider::class.java)
        `when`(provider.getFileHandle(rootDirectoryPath)).thenReturn(rootDirectory)
        val fileUtil = FileUtil(provider)

        val filesInPath = fileUtil.getFilesInPath(rootDirectoryPath, true)
        assertThat(filesInPath).containsExactlyInAnyOrder(rootFile1, rootFile2)
    }

    @Test
    fun `Get all files in path recursively`() {
        val rootDirectoryPath = "base"
        val subDirectoryPath = "base/sub"

        val subFile1 = FileHandleMock("sub1")
        val subFile2 = FileHandleMock("sub2")
        val subDirectory = FileHandleMock(subDirectoryPath, true, arrayOf(subFile1, subFile2))

        val rootFile1 = FileHandleMock("root1")
        val rootFile2 = FileHandleMock("root2")
        val rootDirectory = FileHandleMock(rootDirectoryPath, true, arrayOf(rootFile1, rootFile2, subDirectory))

        val provider = mock(FileHandlerProvider::class.java)
        `when`(provider.getFileHandle(rootDirectoryPath)).thenReturn(rootDirectory)
        val fileUtil = FileUtil(provider)

        val filesInPath = fileUtil.getFilesInPath(rootDirectoryPath, true)
        assertThat(filesInPath).containsExactlyInAnyOrder(subFile1, subFile2, rootFile1, rootFile2)
    }

    @Test
    fun `When recursive search is disabled, do not search subdirectories`() {
        val rootDirectoryPath = "base"
        val subDirectoryPath = "base/sub"

        val subFile1 = FileHandleMock("sub1")
        val subFile2 = FileHandleMock("sub2")
        val subDirectory = FileHandleMock(subDirectoryPath, true, arrayOf(subFile1, subFile2))

        val rootFile1 = FileHandleMock("root1")
        val rootFile2 = FileHandleMock("root2")
        val rootDirectory = FileHandleMock(rootDirectoryPath, true, arrayOf(rootFile1, rootFile2, subDirectory))

        val provider = mock(FileHandlerProvider::class.java)
        `when`(provider.getFileHandle(rootDirectoryPath)).thenReturn(rootDirectory)
        val fileUtil = FileUtil(provider)

        val filesInPath = fileUtil.getFilesInPath(rootDirectoryPath, false)
        assertThat(filesInPath).containsExactlyInAnyOrder(rootFile1, rootFile2)
    }
}

class FileHandleMock(
    private val path: String = "",
    private val directory: Boolean = false,
    private val children: Array<FileHandle> = arrayOf(),
    private val extension: String = ""
) : FileHandle() {
    override fun list(): Array<FileHandle> = children

    override fun path(): String = path

    override fun isDirectory(): Boolean = directory

    override fun exists(): Boolean = true

    override fun extension(): String = extension

    // ensures that when debugging this object doesn't display as a NullPointerException
    override fun toString(): String = path
}
