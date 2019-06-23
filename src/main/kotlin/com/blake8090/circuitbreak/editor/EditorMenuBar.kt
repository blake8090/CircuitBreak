package com.blake8090.circuitbreak.editor

import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener
import com.kotcrab.vis.ui.widget.Menu
import com.kotcrab.vis.ui.widget.MenuBar
import com.kotcrab.vis.ui.widget.MenuItem

class EditorMenuBar {
    private val menuBar = MenuBar()

    private lateinit var itemSaveLevelAs: MenuItem
    private lateinit var itemSaveLevel: MenuItem
    private lateinit var itemLoadLevel: MenuItem

    fun createMenu(controller: EditorController) {
        val menuEditor = Menu("Editor")
        addMenuItem(menuEditor, "Exit") {
            controller.onExit()
        }
        menuBar.addMenu(menuEditor)

        val menuLevel = Menu("Level")
        addMenuItem(menuLevel, "New Level")
        itemLoadLevel = addMenuItem(menuLevel, "Load Level")
        itemSaveLevel = addMenuItem(menuLevel, "Save Level")
        itemSaveLevelAs = addMenuItem(menuLevel, "Save Level As")
        menuBar.addMenu(menuLevel)

        val menuView = Menu("View")
        addMenuItem(menuView, "Entity Templates") {
            controller.onShowEntityTemplates()
        }
        addMenuItem(menuView, "Tiles")
        menuBar.addMenu(menuView)
    }

    fun getTable(): Table = menuBar.table

    fun enableSaveLevelItems(enabled: Boolean) {
        itemSaveLevel.isDisabled = !enabled
        itemSaveLevelAs.isDisabled = !enabled
    }

    private fun addMenuItem(menu: Menu, name: String) =
        addMenuItem(menu, name) {}

    private fun addMenuItem(menu: Menu, name: String, func: () -> Unit): MenuItem {
        val item = MenuItem(name)
        item.addListener(object : ChangeListener() {
            override fun changed(event: ChangeEvent?, actor: Actor?) {
                func.invoke()
            }
        })
        menu.addItem(item)
        return item
    }
}
