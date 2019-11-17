package com.blake8090.circuitbreak.engine.screen

import com.blake8090.circuitbreak.engine.ioc.Component
import org.pmw.tinylog.Logger

@Component
class GameScreen : Screen {
    override fun init() {
        Logger.info("Game screen started")
    }

    override fun input() {
    }

    override fun update() {
    }

    override fun render() {
    }

    override fun dispose() {
        Logger.info("Game screen end")
    }
}
