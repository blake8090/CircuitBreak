package com.blake8090.circuitbreak.screen

import com.blake8090.circuitbreak.GameContext
import com.blake8090.circuitbreak.entity.EntityList
import com.blake8090.circuitbreak.entity.components.PositionComponent
import com.blake8090.circuitbreak.entity.components.TextureComponent
import org.pmw.tinylog.Logger

class GameScreen(context: GameContext) : Screen(context) {
    private val entities = EntityList()

    override fun init() {
        Logger.info("Game screen init")
        context.assets.loadGfx()
        context.assets.finishLoading()

        entities.createEntity(TextureComponent("circle.png"), PositionComponent())
    }

    override fun render() {
        entities.forEachWith(TextureComponent::class, PositionComponent::class) { _, tex, pos ->
            context.assets.getTexture(tex.texture)?.let {
                context.batch.draw(it, pos.x, pos.y)
            }
        }
    }
}
