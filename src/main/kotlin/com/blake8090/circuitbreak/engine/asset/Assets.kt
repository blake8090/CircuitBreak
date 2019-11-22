package com.blake8090.circuitbreak.engine.asset

import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.files.FileHandle
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.ObjectMap
import com.blake8090.circuitbreak.engine.FileUtil
import com.blake8090.circuitbreak.engine.ioc.Component
import com.blake8090.circuitbreak.engine.ioc.Lifetime
import org.pmw.tinylog.Logger
import kotlin.reflect.KClass

// list of all image extensions supported by libGDX
private val IMAGE_EXTENSIONS = arrayOf("jpg", "jpeg", "bmp", "png")

@Component(lifetime = Lifetime.SINGLETON)
class Assets(
    assetManagerProvider: AssetManagerProvider,
    private val assetConfig: AssetConfig,
    private val fileUtil: FileUtil
) {
    private val assetManager = assetManagerProvider.getAssetManager()
    private val textureAliases = ObjectMap<String, AssetDescriptor<Texture>>()

    fun loadGfx() {
        val imageFiles = fileUtil.getFilesInPath(assetConfig.getGfxPath(), true)
            .filter { IMAGE_EXTENSIONS.contains(it.extension()) }
        if (imageFiles.isEmpty()) {
            Logger.info("No images available to load")
        } else {
            imageFiles.forEach { loadAsset(it, Texture::class, textureAliases) }
        }
    }

    private fun <T : Any> loadAsset(
        file: FileHandle,
        clazz: KClass<T>,
        map: ObjectMap<String, AssetDescriptor<T>>
    ) {
        if (!file.exists()) {
            return
        }

        val path = file.path()
        val fileName = file.name()
        if (map.containsKey(fileName)) {
            Logger.info("Found duplicate ${clazz.simpleName} asset '$fileName' when loading '$path'")
            return
        }

        Logger.debug("Loading ${clazz.simpleName}: '$path'")
        val descriptor = AssetDescriptor(path, clazz.java)
        assetManager.load(descriptor)
        map.put(fileName, descriptor)
    }

    fun getTexture(fileName: String): Texture? {
        val descriptor = textureAliases[fileName] ?: return null
        return assetManager.get(descriptor)
    }

    fun hasTexture(fileName: String) = textureAliases.containsKey(fileName)

    fun finishLoading() = assetManager.finishLoading()
}

@Component
class AssetManagerProvider {
    fun getAssetManager() = AssetManager()
}
