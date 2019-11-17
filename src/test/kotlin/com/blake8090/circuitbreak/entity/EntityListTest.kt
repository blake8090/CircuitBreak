package com.blake8090.circuitbreak.entity

import com.blake8090.circuitbreak.asset.EntityTemplate
import com.blake8090.circuitbreak.entity.components.Component
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

class EntityListTest {
    @Test
    fun createEntityFromTemplate() {
        val color = Color("red")
        val entityTemplate = EntityTemplate("test", listOf(color))
        val entityList = EntityList()

        val entity = entityList.createEntityFromTemplate(entityTemplate)
        assertThat(entity.hasComponents(Color::class)).isTrue()
    }

    @Test
    fun t() {
        val components = mutableListOf<Component>()
        components.add(Color("test"))

        components.forEach {
            comp(it)
        }
    }

    private fun comp(component: Any) {
//        T::class.simpleName
//        val map = mutableMapOf<String, Any?>()
        component::class.memberProperties
//            .forEach { map[it.name] = it.get(component) as KProperty1<Any, *> }
            .map { it as KProperty1<Any, Any> }
            .forEach {
                it.get(component)
            }
//
//        map.forEach { (t, u) -> println("$t ={${u?.toString()}") }
    }

    private data class Color(val colorName: String = "") : Component()
}
