package com.blake8090.circuitbreak.entity.components

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes(
    JsonSubTypes.Type(value = PositionComponent::class),
    JsonSubTypes.Type(value = TextureComponent::class)
)
open class Component

data class PositionComponent(
    var x: Float = 0f,
    var y: Float = 0f
) : Component()

data class TextureComponent(var texture: String = "") : Component()
