package com.blake8090.circuitbreak.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.*
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.blake8090.circuitbreak.asset.Assets
import com.blake8090.circuitbreak.screen.GameModel
import com.kotcrab.vis.ui.widget.VisWindow

class EditorController(
    private val model: GameModel,
    private val assets: Assets
) {
    // TODO: move stage to view?
    val stage: Stage = Stage(ScreenViewport())
    private val view = EditorView(this)

    private var entityWindowOpen = false

    fun init() {
        // instantly close windows, no fading out
        VisWindow.FADE_TIME = 0f

        // TODO: use input multiplexer
        Gdx.input.inputProcessor = stage

        stage.addActor(view.root)
        view.menuBar.enableSaveLevelItems(false)

        onOpenEntityWindow()
    }

    fun render() {
        stage.act()
        stage.draw()
    }

    fun resize(width: Int, height: Int) {
        stage.viewport.update(width, height, true)
    }

    fun dispose() {
        stage.dispose()
    }

    fun onExit() {
        model.editorMode = false
    }

    fun onOpenEntityWindow() {
        if (!entityWindowOpen) {
            view.openEntityWindow(assets)
            entityWindowOpen = true
        }
    }

    fun onEntityWindowClosed() {
        entityWindowOpen = false
    }
}
