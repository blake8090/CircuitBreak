package com.blake8090.circuitbreak.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader
import com.blake8090.circuitbreak.util.FileUtil
import org.pmw.tinylog.Logger

//TODO: add font
//const val DEFAULT_FONT_SIZE = 12

// list of all image extensions supported by libGDX
private val IMAGE_EXTENSIONS = arrayOf("jpg", "jpeg", "bmp", "png")

/**
 * Manages loading and accessing game assets. Assets are stored and obtained using their filename only,
 * instead of the absolute path.
 */
class Assets {
    private val fileUtil = FileUtil()
    private val assetManager = AssetManager()
    private val textureAliases = mutableMapOf<String, AssetDescriptor<Texture>>()

    init {
        val resolver = InternalFileHandleResolver()
        assetManager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assetManager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))
    }

    fun loadGfx() {
        val imageFiles = fileUtil.getFilesInPath(Paths.GFX_PATH)
            .filter { IMAGE_EXTENSIONS.contains(it.extension()) }
        if (imageFiles.isEmpty()) {
            Logger.info("No images available to load")
        } else {
            imageFiles.forEach(this::loadTexture)
        }
    }

    fun getTexture(fileName: String): Texture? {
        val descriptor = textureAliases[fileName] ?: return null
        return assetManager.get(descriptor)
    }

    private fun loadTexture(file: FileHandle) {
        if (!file.exists() || file.isDirectory) {
            return
        }

        val path = file.path()
        val fileName = file.name()
        if (textureAliases.containsKey(fileName)) {
            Logger.info("Found duplicate texture file '$fileName' when loading '$path'")
            return
        }

        Logger.debug("Loading texture: '$path'")
        val descriptor = AssetDescriptor(path, Texture::class.java)
        assetManager.load(descriptor)
        textureAliases[fileName] = descriptor
    }

    fun finishLoading() = assetManager.finishLoading()
}

/**
 * Lists all the file directories from which assets are loaded
 */
object Paths {
    private const val DATA_PATH = "data"
    const val GFX_PATH = "$DATA_PATH/gfx/"
//    const val FONTS_PATH = "$DATA_PATH/fonts/"
}
