package com.blake8090.circuitbreak

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import com.blake8090.circuitbreak.engine.Engine

class CircuitBreak : ApplicationAdapter() {
    private val engine = Engine()

    override fun create() {
        engine.start()
    }

    override fun render() {
        engine.update()
    }

    override fun dispose() {
        engine.stop()
    }
}

fun main() {
    LwjglApplication(CircuitBreak(), LwjglApplicationConfiguration())
}
