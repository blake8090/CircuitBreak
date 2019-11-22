package com.blake8090.circuitbreak.engine.renderer

interface IRenderer {
    fun queueTask(task: DrawTask)
    fun render()
}
