package com.blake8090.circuitbreak.engine.screen

import com.blake8090.circuitbreak.engine.ioc.Component
import com.blake8090.circuitbreak.engine.ioc.Container
import com.blake8090.circuitbreak.engine.ioc.Lifetime
import java.util.Stack
import kotlin.reflect.KClass

// TODO: unit test this
@Component(lifetime = Lifetime.SINGLETON)
class ScreenStack(private val container: Container) {
    private val screens = Stack<Screen>()
    private val screensToDelete = mutableSetOf<Screen>()

    fun changeScreen(screenClass: KClass<out Screen>) =
        changeScreen(container.get(screenClass))

    fun changeScreen(screen: Screen) {
        getCurrentScreen()?.let(::removeScreen)
        pushScreen(screen)
    }

    fun pushScreen(screenClass: KClass<out Screen>) =
        pushScreen(container.get(screenClass))

    fun pushScreen(screen: Screen) {
        screen.init()
        screens.push(screen)
    }

    fun removeScreen(screen: Screen) = screensToDelete.add(screen)

    fun getCurrentScreen(): Screen? {
        if (screens.isEmpty()) {
            return null
        }
        return screens.peek().takeIf { !screensToDelete.contains(it) }
    }

    fun dispose() = screens.forEach(Screen::dispose)

    fun update() {
        if (screensToDelete.isNotEmpty()) {
            screensToDelete.forEach(Screen::dispose)
            screens.removeAll(screensToDelete)
            screensToDelete.clear()
        }
    }

    fun getScreens() = screens.filter { !screensToDelete.contains(it) }

    fun forEachScreen(func: (Screen) -> Unit) = getScreens().forEach(func)
}
