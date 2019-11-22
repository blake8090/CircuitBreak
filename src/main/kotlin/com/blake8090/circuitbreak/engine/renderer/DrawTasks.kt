package com.blake8090.circuitbreak.engine.renderer

interface DrawTask

data class TextureDrawTask(
    val textureName: String,
    val x: Float,
    val y: Float
) : DrawTask
