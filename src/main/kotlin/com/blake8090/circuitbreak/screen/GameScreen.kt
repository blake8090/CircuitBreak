package com.blake8090.circuitbreak.screen

import com.blake8090.circuitbreak.GameContext
import org.pmw.tinylog.Logger

class GameScreen(context: GameContext) : BaseScreen(context) {
    override fun show() {
        Logger.info("Game screen init")
    }
}
