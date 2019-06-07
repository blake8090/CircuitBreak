package com.blake8090.circuitbreak.screen

import com.blake8090.circuitbreak.GameContext

abstract class Screen(val context: GameContext) {
    open fun init() {}
    open fun input() {}
    open fun update() {}
    open fun render() {}
    open fun resize(width: Int, height: Int) {}
    open fun dispose() {}
}
