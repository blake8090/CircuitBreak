package com.blake8090.circuitbreak.engine.renderer

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Array
import com.blake8090.circuitbreak.engine.asset.Assets

class LibGdxRenderer(private val assets: Assets) : IRenderer {
    private val batch = SpriteBatch()
    private val tasks = Array<DrawTask>()

    override fun queueTask(task: DrawTask) = tasks.add(task)

    override fun render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)

        batch.begin()
        tasks.forEach(::processTask)
        batch.end()

        tasks.clear()
    }

    private fun processTask(task: DrawTask) {
        when (task) {
            is TextureDrawTask -> {
                val tex = assets.getTexture(task.textureName) ?: return
                batch.draw(tex, task.x, task.y)
            }
        }
    }
}
