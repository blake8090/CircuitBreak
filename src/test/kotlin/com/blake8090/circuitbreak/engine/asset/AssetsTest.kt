package com.blake8090.circuitbreak.engine.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.blake8090.circuitbreak.engine.FileUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

class AssetsTest {
    private val gfxPath = "gfx"

    private val config = mock(AssetConfig::class.java)
    private val provider = mock(AssetManagerProvider::class.java)
    private val fileUtil = mock(FileUtil::class.java)

    private val manager = mock(AssetManager::class.java)

    private lateinit var assets: Assets

    @Before
    fun setup() {
        `when`(config.getGfxPath()).thenReturn(gfxPath)
        `when`(provider.getAssetManager()).thenReturn(manager)
        assets = Assets(provider, config, fileUtil)
    }

    @Test
    fun `loadGfx - Skip non-image files`() {
        val txtFile = fileHandleMock { extension = "txt" }
        `when`(fileUtil.getFilesInPath(gfxPath, true)).thenReturn(listOf(txtFile))

        assets.loadGfx()
        verifyNoInteractions(manager)
    }

    @Test
    fun `loadGfx - Load image file`() {
        val path = "gfx/image.jpg"
        val fileName = "image.jpg"

        val imgFile = fileHandleMock {
            this.path = path
            this.fileName = fileName
            extension = "jpg"
        }
        `when`(fileUtil.getFilesInPath(gfxPath, true)).thenReturn(listOf(imgFile))

        `when`(manager.load(ArgumentMatchers.any())).thenAnswer {
            assertThat(it.arguments[0]).isInstanceOf(AssetDescriptor::class.java)
            val descriptor = it.arguments[0] as AssetDescriptor<*>
            assertThat(descriptor.fileName).isEqualTo(path)
            assertThat(descriptor.type).isEqualTo(Texture::class.java)
            it
        }

        assets.loadGfx()
        verify(manager, times(1)).load(ArgumentMatchers.any<AssetDescriptor<*>>())
        assertThat(assets.hasTexture(fileName)).isTrue()
    }

    @Test
    fun `Return texture when queried`() {
        val path = "gfx/image.jpg"
        val fileName = "image.jpg"

        val imgFile = fileHandleMock {
            this.path = path
            this.fileName = fileName
            extension = "jpg"
        }
        `when`(fileUtil.getFilesInPath(gfxPath, true)).thenReturn(listOf(imgFile))

        val texture = mock(Texture::class.java)
        `when`(manager.get(ArgumentMatchers.any<AssetDescriptor<*>>())).thenReturn(texture)

        assets.loadGfx()
        assertThat(assets.getTexture(fileName)).isEqualTo(texture)
    }

    @Test
    fun `Skip if handle for asset does not exist`() {
        val file = fileHandleMock {
            exists = false
            extension = "jpg"
        }
        `when`(fileUtil.getFilesInPath(gfxPath, true)).thenReturn(listOf(file))

        assets.loadGfx()
        verifyNoInteractions(manager)
    }

    @Test
    fun `Do not load duplicate assets`() {
        val path = "gfx/image.jpg"
        val path2 = "gfx/sub/image.jpg"
        val fileName = "image.jpg"

        val imgFile = fileHandleMock {
            this.path = path
            this.fileName = fileName
            extension = "jpg"
        }
        val imgFile2 = fileHandleMock {
            this.path = path2
            this.fileName = fileName
            extension = "jpg"
        }

        `when`(fileUtil.getFilesInPath(gfxPath, true)).thenReturn(listOf(imgFile, imgFile2))

        `when`(manager.load(ArgumentMatchers.any())).thenAnswer {
            val descriptor = it.arguments[0] as AssetDescriptor<*>
            assertThat(descriptor.fileName).isEqualTo(path)
            it
        }

        assets.loadGfx()
        verify(manager, times(1)).load(ArgumentMatchers.any<AssetDescriptor<*>>())
        assertThat(assets.hasTexture(fileName)).isTrue()
    }
}

class FileHandleMockBuilder {
    var path = ""
    var fileName = ""
    var directory = false
    var extension = ""
    val children = arrayOf<FileHandle>()
    var exists = true

    fun build(): FileHandle =
        object : FileHandle() {
            override fun list(): Array<FileHandle> = children

            override fun path(): String = path

            override fun isDirectory(): Boolean = directory

            override fun exists(): Boolean = exists

            override fun extension(): String = extension

            override fun name(): String = fileName

            override fun toString(): String = path
        }
}

private fun fileHandleMock(consumer: FileHandleMockBuilder.() -> Unit): FileHandle {
    val fileHandleMockBuilder = FileHandleMockBuilder()
    consumer(fileHandleMockBuilder)
    return fileHandleMockBuilder.build()
}
