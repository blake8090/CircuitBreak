package com.blake8090.circuitbreak.screen

import com.blake8090.circuitbreak.GameContext
import org.pmw.tinylog.Logger

class GameScreen(context: GameContext) : Screen(context) {
    override fun init() {
        Logger.info("Game screen init")
        context.assets.loadGfx()
        context.assets.finishLoading()
    }
}
