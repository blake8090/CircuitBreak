package com.blake8090.circuitbreak.engine.ioc

import kotlin.reflect.KClass

class Binding(val implementationClass: KClass<*>, val lifetime: Lifetime)
