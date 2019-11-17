package com.blake8090.circuitbreak.engine.ioc

@Target(AnnotationTarget.CLASS)
annotation class Component(val lifetime: Lifetime = Lifetime.DEFAULT)
