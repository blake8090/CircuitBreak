package com.blake8090.circuitbreak.engine

import com.blake8090.circuitbreak.engine.ioc.container
import org.pmw.tinylog.Configurator
import org.pmw.tinylog.Level
import org.pmw.tinylog.Logger
import org.pmw.tinylog.writers.ConsoleWriter
import org.pmw.tinylog.writers.FileWriter

private const val LOG_PATH = "./log.txt"
private const val ROOT_PACKAGE = "com.blake8090.circuitbreak"
private const val LOG_FORMAT =
    "{date:yyyy-MM-dd HH:mm:ss} [{thread}] {class_name}.{method}() Line:{line} {level}: {message}"

class Engine {
    private val container = container {
        scan(ROOT_PACKAGE)
    }

    fun start() {
        setupLogger()
    }

    fun update() {

    }

    fun stop() {
        Logger.info("Stopping engine")
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
