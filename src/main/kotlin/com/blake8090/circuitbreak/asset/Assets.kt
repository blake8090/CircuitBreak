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
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
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
    private val entityTemplates = mutableMapOf<String, String>()

    init {
        val resolver = InternalFileHandleResolver()
        assetManager.setLoader(FreeTypeFontGenerator::class.java, FreeTypeFontGeneratorLoader(resolver))
        assetManager.setLoader(BitmapFont::class.java, ".ttf", FreetypeFontLoader(resolver))
    }

    // todo: async loader for this?
    fun loadTemplates() {
        val files = fileUtil.getFilesInPath(Paths.TEMPLATES_PATH)
            .filter { it.extension() == "yaml" }
        files.forEach { handle ->
            Logger.debug("Reading entity template file '${handle.path()}'")
            // file contents will be stored and generated on demand
            // in order to create fresh instances of Components
//            entityTemplates[handle.name()] = fileUtil.readFile(handle)
            val content = fileUtil.readFile(handle)
//            val template
        }
    }

    fun getTemplates() = entityTemplates.entries

    fun getTemplate(templateName: String): String? = entityTemplates[templateName]

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
    const val TEMPLATES_PATH = "$DATA_PATH/entities/"
//    const val FONTS_PATH = "$DATA_PATH/fonts/"
}
