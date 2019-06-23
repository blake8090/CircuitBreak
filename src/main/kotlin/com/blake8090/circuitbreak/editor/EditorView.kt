package com.blake8090.circuitbreak.editor

import com.badlogic.gdx.utils.Align
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.widget.MenuBar
import com.kotcrab.vis.ui.widget.Separator
import com.kotcrab.vis.ui.widget.VisLabel
import com.kotcrab.vis.ui.widget.VisTable

class EditorView(controller: EditorController) {
    val root = VisTable()
    val menuBar = EditorMenuBar()

    init {
        root.setFillParent(true)
        root.align(Align.topLeft)

        val controlTable = VisTable()
        controlTable.background = VisUI.getSkin().get("default", MenuBar.MenuBarStyle::class.java).background

        val titleLabel = VisLabel()
        titleLabel.setText("Circuit Break Editor")
        titleLabel.setAlignment(Align.center)
        controlTable.add(titleLabel).fillX().expandX().row()

        root.add(controlTable).fillX().expandX().row()
        root.add(Separator()).fillX().expandX().row()

        menuBar.createMenu(controller)
        root.add(menuBar.getTable()).expandX().fillX().row()
    }
}
