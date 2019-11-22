package com.blake8090.circuitbreak.engine.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.math.Vector2
import com.blake8090.circuitbreak.engine.ioc.Component
import com.blake8090.circuitbreak.engine.renderer.IRenderer
import com.blake8090.circuitbreak.engine.renderer.TextureDrawTask
import org.pmw.tinylog.Logger

@Component
class GameScreen(private val renderer: IRenderer) : Screen {
    private var pos = Vector2()
    private val speed = 100f

    override fun init() {
        Logger.info("Game screen started")
    }

    override fun input() {
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            pos.x += Gdx.graphics.deltaTime * speed
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            pos.x -= Gdx.graphics.deltaTime * speed
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            pos.y += Gdx.graphics.deltaTime * speed
        } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            pos.y -= Gdx.graphics.deltaTime * speed
        }
    }

    override fun update() {
    }

    override fun render() {
        renderer.queueTask(TextureDrawTask("background.png", 0f, 0f))
        renderer.queueTask(TextureDrawTask("circle.png", pos.x, pos.y))
    }

    override fun dispose() {
        Logger.info("Game screen end")
    }
}
