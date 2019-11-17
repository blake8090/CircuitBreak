package com.blake8090.circuitbreak.asset

import com.blake8090.circuitbreak.entity.components.Component

class EntityTemplate(
    val name: String = "",
    val components: List<Component> = listOf(),
    val serializedComponents: String = ""
)
