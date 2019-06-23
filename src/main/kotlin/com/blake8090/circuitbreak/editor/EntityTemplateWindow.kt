package com.blake8090.circuitbreak.editor

import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Array
import com.blake8090.circuitbreak.asset.Assets
import com.kotcrab.vis.ui.VisUI
import com.kotcrab.vis.ui.util.adapter.ArrayAdapter
import com.kotcrab.vis.ui.widget.*
import com.kotcrab.vis.ui.widget.VisTable
import com.kotcrab.vis.ui.widget.ListView.UpdatePolicy

class EntityTemplateWindow(private val assets: Assets) : VisWindow("Entities") {
    var onCloseListener: () -> Unit = {}

    init {
        addCloseButton()
        createContents()
        isResizable = true
    }

    private fun createContents() {
        val tex = assets.getTexture("circle.png") ?: return
        val models = Array<Model>()
        for (x in 0..20) {
            models.add(Model(tex, "Entity Template #$x"))
        }

        val view = ListView<Model>(TemplateAdapter(models))
        view.updatePolicy = UpdatePolicy.ON_DRAW

        val button = VisTextButton("New")
        add(button).left().row()
        add(view.mainTable).grow()

        setSize(250f, 200f)
    }

    override fun close() {
        onCloseListener.invoke()
        super.close()
    }
}

// todo: extend adapter and make a util class to hide the select/deselect etc. methods
internal class TemplateAdapter(array: Array<Model>) : ArrayAdapter<Model, VisTable>(array) {
    private val bg = VisUI.getSkin().getDrawable("window-bg")
    private val selection = VisUI.getSkin().getDrawable("list-selection")

    init {
        selectionMode = SelectionMode.SINGLE
    }

    override fun createView(item: Model): VisTable {
        val table = VisTable()
        table.left().padBottom(5f).padTop(5f)
        table.add(Image(item.texture)).padRight(5f)
        table.add(Label(item.name, VisUI.getSkin())).expand().fill().row()
        return table
    }

    override fun selectView(view: VisTable) {
        view.background = selection
    }

    override fun deselectView(view: VisTable) {
        view.background = bg
    }
}

internal class Model (
    val texture: Texture,
    val name: String
)
