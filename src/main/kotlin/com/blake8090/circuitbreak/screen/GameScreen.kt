package com.blake8090.circuitbreak.screen

import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.viewport.FillViewport
import com.blake8090.circuitbreak.GameContext
import com.blake8090.circuitbreak.entity.EntityList
import com.blake8090.circuitbreak.entity.components.PositionComponent
import com.blake8090.circuitbreak.entity.components.TextureComponent
import org.pmw.tinylog.Logger

private const val ART_WIDTH = 1920f
private const val ART_HEIGHT = 1080f

class GameScreen(context: GameContext) : Screen(context) {
    private val entities = EntityList()
    private val camera = OrthographicCamera()
    private val viewport = FillViewport(ART_WIDTH, ART_HEIGHT, camera)

    override fun init() {
        Logger.info("Game screen init")
        context.assets.loadGfx()
        context.assets.finishLoading()

        viewport.apply()
        camera.position.set((ART_WIDTH / 2), (ART_HEIGHT / 2), 0f)

        entities.createEntity(TextureComponent("background.png"), PositionComponent())
    }

    override fun render() {
        camera.update()
        context.batch.projectionMatrix = camera.combined
        entities.forEachWith(TextureComponent::class, PositionComponent::class) { _, tex, pos ->
            context.assets.getTexture(tex.texture)?.let {
                context.batch.draw(it, pos.x, pos.y)
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height)
    }
}
