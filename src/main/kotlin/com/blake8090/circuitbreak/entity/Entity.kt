package com.blake8090.circuitbreak.entity

import com.blake8090.circuitbreak.entity.components.Component
import kotlin.reflect.KClass
import kotlin.reflect.full.cast
import kotlin.reflect.full.isSubclassOf

class Entity {
    private val components = mutableMapOf<KClass<out Component>, Component>()

    fun addComponent(component: Component) {
        components[component::class] = component
    }

    fun removeComponent(clazz: KClass<out Component>) = components.remove(clazz)

    fun <T : Component> getComponent(clazz: KClass<T>): T? {
        val component = components[clazz] ?: return null
        if (component::class.isSubclassOf(clazz)) {
            return clazz.cast(component)
        }
        return null
    }

    fun <T : Component> getComponent(clazz: KClass<T>, consumer: (T) -> Unit) =
        getComponent(clazz)?.let { consumer.invoke(it) }

    fun hasComponents(vararg classes: KClass<out Component>): Boolean =
        components.keys.containsAll(classes.toList())
}
