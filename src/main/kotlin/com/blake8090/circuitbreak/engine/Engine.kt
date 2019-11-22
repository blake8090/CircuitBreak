package com.blake8090.circuitbreak.engine

import com.blake8090.circuitbreak.engine.asset.Assets
import com.blake8090.circuitbreak.engine.ioc.Lifetime
import com.blake8090.circuitbreak.engine.ioc.container
import com.blake8090.circuitbreak.engine.renderer.IRenderer
import com.blake8090.circuitbreak.engine.renderer.LibGdxRenderer
import com.blake8090.circuitbreak.engine.screen.GameScreen
import com.blake8090.circuitbreak.engine.screen.Screen
import com.blake8090.circuitbreak.engine.screen.ScreenStack
import org.pmw.tinylog.Configurator
import org.pmw.tinylog.Level
import org.pmw.tinylog.Logger
import org.pmw.tinylog.writers.ConsoleWriter
import org.pmw.tinylog.writers.FileWriter

private const val ROOT_PACKAGE = "com.blake8090.circuitbreak"
private const val LOG_PATH = "./log.txt"
private const val LOG_FORMAT =
    "{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class_name}.{method}() Line:{line} {level}: {message}"

class Engine {
    private val container = container {
        scan(ROOT_PACKAGE)
        bind(IRenderer::class, LibGdxRenderer::class, Lifetime.SINGLETON)
    }

    fun start() {
        setupLogger()
        container.get(ScreenStack::class).changeScreen(GameScreen::class)
        container.get(Assets::class) {
            loadGfx()
            finishLoading()
        }
    }

    fun update() {
        container.get(ScreenStack::class) {
            forEachScreen {
                it.input()
                it.update()
                it.render()
            }

            renderFrame()

            // clear any deleted screens in the stack
            update()
        }
    }

    fun stop() {
        container.get(ScreenStack::class).dispose()
        Logger.info("Stopping engine")
    }

    private fun renderFrame() {
        container.get(IRenderer::class).render()
    }

    private fun setupLogger() {
        Configurator.defaultConfig()
            // todo: use app level config
            .writer(FileWriter(LOG_PATH))
            .addWriter(ConsoleWriter())
            .formatPattern(LOG_FORMAT)
            .level(Level.TRACE)
            .activate()
        Logger.info("Initialized logger")
    }
}
