package com.blake8090.circuitbreak.entity.components

open class Component

data class PositionComponent(
    var x: Float = 0f,
    var y: Float = 0f
) : Component()

data class TextureComponent(var texture: String = "") : Component()
