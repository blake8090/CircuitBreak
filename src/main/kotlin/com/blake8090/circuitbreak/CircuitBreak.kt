package com.blake8090.circuitbreak

import com.badlogic.gdx.Game
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import org.pmw.tinylog.Configurator
import org.pmw.tinylog.Level
import org.pmw.tinylog.Logger
import org.pmw.tinylog.writers.ConsoleWriter
import org.pmw.tinylog.writers.FileWriter

private const val LOG_FORMAT =
    "{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class_name}.{method}() Line:{line} {level}: {message}"

class CircuitBreak : Game() {
    private lateinit var context: GameContext

    override fun create() {
        setupLogger()
        context = GameContext()
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
    val batch = SpriteBatch()
}
