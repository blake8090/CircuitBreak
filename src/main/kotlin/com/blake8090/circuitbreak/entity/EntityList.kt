package com.blake8090.circuitbreak.entity

import com.blake8090.circuitbreak.entity.components.Component
import kotlin.reflect.KClass

class EntityList {
    private val entities = mutableListOf<Entity>()

    fun createEntity(vararg components: Component): Entity {
        val entity = Entity()
        components.forEach { entity.addComponent(it) }
        entities.add(entity)
        return entity
    }

    fun <T : Component> forEachWith(clazz: KClass<T>, consumer: (Entity, T) -> Unit) {
        entities.filter { it.hasComponents(clazz) }
            .forEach { entity ->
                val component = entity.getComponent(clazz) ?: return@forEach
                consumer.invoke(entity, component)
            }
    }

    fun <T1 : Component, T2 : Component> forEachWith(class1: KClass<T1>,
                                                     class2: KClass<T2>,
                                                     consumer: (Entity, T1, T2) -> Unit) {
        entities.filter { it.hasComponents(class1, class2) }
            .forEach { entity ->
                val c1 = entity.getComponent(class1) ?: return@forEach
                val c2 = entity.getComponent(class2) ?: return@forEach
                consumer.invoke(entity, c1, c2)
            }
    }
}
