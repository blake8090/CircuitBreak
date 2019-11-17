package com.blake8090.circuitbreak.engine.screen

interface Screen {
    fun init()
    fun input()
    fun update()
    fun render()
    fun dispose()
}
