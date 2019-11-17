package com.blake8090.circuitbreak.entity

import com.blake8090.circuitbreak.asset.EntityTemplate
import com.blake8090.circuitbreak.entity.components.Component
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.memberProperties

class EntityList {
    private val entities = mutableListOf<Entity>()

    fun createEntity(vararg components: Component): Entity =
            createEntity(components.toList())

    fun createEntity(components: List<Component>): Entity {
        val entity = Entity()
        components.forEach { entity.addComponent(it) }
        entities.add(entity)
        return entity
    }

    fun createEntityFromTemplate(entityTemplate: EntityTemplate): Entity {
        val components = entityTemplate.components
            .map { copy(it) }.toList()
        return createEntity(components)
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

    private inline fun <reified T : Any> copy(original: T): T {
        val newInstance = T::class.createInstance()
        T::class.memberProperties
            .filterIsInstance<KMutableProperty<*>>()
            .forEach { prop ->
                val call = prop.getter.call(original)
                prop.setter.call(newInstance, call)
            }
        return newInstance
    }
}
