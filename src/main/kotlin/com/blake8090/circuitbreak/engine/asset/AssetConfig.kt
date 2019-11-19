package com.blake8090.circuitbreak.engine.asset

import com.blake8090.circuitbreak.engine.ioc.Component

@Component
class AssetConfig {
    fun getBasePath() = "data/"
    fun getGfxPath() = getBasePath() + "gfx/"
}
