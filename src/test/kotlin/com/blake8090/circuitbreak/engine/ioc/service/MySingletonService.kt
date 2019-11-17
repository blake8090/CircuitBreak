package com.blake8090.circuitbreak.engine.ioc.service

import com.blake8090.circuitbreak.engine.ioc.Component
import com.blake8090.circuitbreak.engine.ioc.Lifetime

@Component(lifetime = Lifetime.SINGLETON)
internal class MySingletonService
