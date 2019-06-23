package com.blake8090.circuitbreak.editor

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.blake8090.circuitbreak.screen.GameModel

class EditorController(private val model: GameModel) {
    private var stage: Stage = Stage(ScreenViewport())
    private val view = EditorView(this)

    init {
        // TODO: use input multiplexer
        Gdx.input.inputProcessor = stage

        stage.addActor(view.root)
        view.menuBar.enableSaveLevelItems(false)
    }

    fun render() {
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

    fun onShowEntityTemplates() {
    }
}
