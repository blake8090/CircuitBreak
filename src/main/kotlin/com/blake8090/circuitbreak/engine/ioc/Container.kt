package com.blake8090.circuitbreak.engine.ioc

import io.github.classgraph.ClassGraph
import io.github.classgraph.ClassInfo
import kotlin.Exception
import kotlin.jvm.internal.Reflection
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.cast

class Container {
    private val singletonInstanceMap = mutableMapOf<KClass<*>, Any>()
    private val bindings = mutableMapOf<KClass<*>, Binding>()

    fun <T : Any> bind(
        interfaceClass: KClass<T>,
        implementationClass: KClass<out T>,
        lifetime: Lifetime = Lifetime.DEFAULT
    ) {
        if (implementationClass.isAbstract || implementationClass.java.isInterface) {
            throw BindingException("The implementation class cannot be abstract or an interface")
        } else {
            bindings[interfaceClass] = Binding(implementationClass, lifetime)
        }
    }

    fun <T : Any> get(clazz: KClass<T>): T {
        val binding = bindings[clazz]
            ?: throw ComponentInstantiationException("Missing binding for class ${clazz.simpleName}")
        return clazz.cast(resolveInstance(binding))
    }

    private fun resolveInstance(binding: Binding): Any {
        val instanceClass = binding.implementationClass
        var instance: Any? = null

        val isSingleton = binding.lifetime == Lifetime.SINGLETON
        if (isSingleton) {
            instance = singletonInstanceMap[instanceClass]
        }

        if (instance == null) {
            instance = constructInstance(instanceClass)
            if (isSingleton) {
                singletonInstanceMap[instanceClass] = instance
            }
        }
        return instance
    }

    private fun <T : Any> constructInstance(clazz: KClass<T>): T {
        val constructor = getConstructor(clazz)
        val dependencies = constructor.parameters
            .map { it.type.classifier as KClass<*> }
            .map { get(it) }
        // convert to an exploded typed array to ensure that the collection isn't mistaken as the only parameter
        return constructor.call(*dependencies.toTypedArray())
    }

    private fun <T : Any> getConstructor(clazz: KClass<T>): KFunction<T> {
        val constructors = clazz.constructors
        if (constructors.isEmpty()) {
            throw ComponentInstantiationException("Missing constructors for class ${clazz.simpleName}")
        } else if (constructors.size > 1) {
            throw ComponentInstantiationException("Only one constructor is supported")
        }
        return constructors.firstOrNull() ?: throw ComponentInstantiationException("Unexpected null constructor")
    }

    fun scan(packageName: String) {
        ClassGraph()
            .enableClassInfo()
            .enableAnnotationInfo()
            .whitelistPackages(packageName)
            .scan()
            .use {
                it.getClassesWithAnnotation(Component::class.java.name)
                    .forEach(::createBinding)
            }
    }

    private fun createBinding(classInfo: ClassInfo) {
        // todo: handle interfaces as Components
        if (classInfo.isInterface) {
            return
        }
        val annotationInfo = classInfo.getAnnotationInfo(Component::class.java.name)
            .loadClassAndInstantiate() as Component
        val kClass = Reflection.createKotlinClass(classInfo.loadClass())
        bind(kClass, kClass, annotationInfo.lifetime)
    }
}

class BindingException(message: String) : Exception(message)
class ComponentInstantiationException(message: String) : Exception(message)

enum class Lifetime {
    SINGLETON,
    DEFAULT
}

fun container(block: Container.() -> Unit): Container = Container().apply(block)
