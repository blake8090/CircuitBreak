package com.blake8090.circuitbreak

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.blake8090.circuitbreak.asset.Assets
import com.blake8090.circuitbreak.screen.GameScreen
import com.blake8090.circuitbreak.screen.Screen
import org.pmw.tinylog.Configurator
import org.pmw.tinylog.Level
import org.pmw.tinylog.Logger
import org.pmw.tinylog.writers.ConsoleWriter
import org.pmw.tinylog.writers.FileWriter

private const val LOG_FORMAT =
    "{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class_name}.{method}() Line:{line} {level}: {message}"

class CircuitBreak : ApplicationAdapter() {
    private var screen: Screen? = null

    private lateinit var context: GameContext

    override fun create() {
        setupLogger()
        context = GameContext()
        setScreen(GameScreen(context))
    }

    override fun render() {
        screen?.input()
        screen?.update()

        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        context.batch.begin()
        screen?.render()
        context.batch.end()

        screen?.update()
    }

    private fun setScreen(screen: Screen) {
        this.screen = screen
        screen.init()
    }

    private fun setupLogger() {
        Configurator.defaultConfig()
            .writer(FileWriter("log.txt"))
            .addWriter(ConsoleWriter())
            .formatPattern(LOG_FORMAT)
            .level(Level.TRACE)
            .activate()
        Logger.info("Initialized logger")
    }
}

/**
 * Used for injecting game-wide dependencies into other classes
 */
class GameContext {
    val assets = Assets()
    val batch = SpriteBatch()
}
