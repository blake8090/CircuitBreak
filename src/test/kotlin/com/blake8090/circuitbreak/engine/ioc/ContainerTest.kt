package com.blake8090.circuitbreak.engine.ioc

import com.blake8090.circuitbreak.engine.ioc.service.MyService
import org.junit.Test
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import java.util.concurrent.ThreadLocalRandom


class ContainerTest {
    @Test
    fun `Return proper implementation when binding interfaces`() {
        container {
            bind(Repository::class, Postgres::class)
            bind(Mapper::class, Mapper::class)
            assertThat(get(Repository::class)).isInstanceOf(Postgres::class.java)
        }
    }

    @Test
    fun `Create nested dependencies`() {
        class UserService(val repository: Repository)
        class Controller(val userService: UserService)

        container {
            bind(Controller::class, Controller::class)
            bind(UserService::class, UserService::class)
            bind(Repository::class, MongoDB::class)
            bind(Mapper::class, Mapper::class)
            get(Controller::class)
        }
    }

    @Test
    fun `Only create bound singleton once`() {
        class Singleton {
            val id = ThreadLocalRandom.current().nextInt()
        }

        container {
            bind(Singleton::class, Singleton::class, Lifetime.SINGLETON)
            val singleton1 = get(Singleton::class)
            val singleton2 = get(Singleton::class)
            assertThat(singleton1.id).isEqualTo(singleton2.id)
        }
    }

    @Test
    fun `Only create bound singleton once when resolving dependencies`() {
        class Singleton {
            val id = ThreadLocalRandom.current().nextInt()
        }

        class Component(val singleton: Singleton)

        container {
            bind(Singleton::class, Singleton::class, Lifetime.SINGLETON)
            bind(Component::class, Component::class)
            val component1 = get(Component::class)
            val component2 = get(Component::class)
            assertThat(component1.singleton.id).isEqualTo(component2.singleton.id)
        }
    }

    @Test
    fun `Create a new instance of default lifetime objects each time`() {
        class Component {
            val id = ThreadLocalRandom.current().nextInt()
        }

        container {
            bind(Component::class, Component::class)
            val component1 = get(Component::class)
            val component2 = get(Component::class)
            assertThat(component1.id).isNotEqualTo(component2.id)
        }
    }

    @Test
    fun `Bind components from package scan`() {
        container {
            scan("com.blake8090.circuitbreak.engine.ioc")
            get(MyService::class)
        }
    }

    @Test
    fun `Automatically bind container using custom supplier`() {
        container {
            assertThat(get(Container::class)).isEqualTo(this)
        }
    }

    @Test
    fun `Use custom supplier when set`() {
        class Record {
            var id = 0
        }

        container {
            bind(Record::class, Record::class)
            setCustomSupplier(Record::class) {
                val record = Record()
                record.id = 50
                record
            }
            val instance = get(Record::class)
            assertThat(instance.id).isEqualTo(50)
        }
    }

    @Test
    fun `Throw exception when implementation class is an interface`() {
        container {
            val thrown = catchThrowable { bind(TestInterface::class, TestInterface::class) }
            assertThat(thrown).isInstanceOf(BindingException::class.java)
        }
    }

    @Test
    fun `Throw exception when implementation class is an abstract class`() {
        abstract class AbstractClass : TestInterface

        container {
            val thrown = catchThrowable { bind(TestInterface::class, AbstractClass::class) }
            assertThat(thrown).isInstanceOf(BindingException::class.java)
        }
    }

    // todo: finish rest of exception tests
//    @Test
//    fun `Throw exception when implementation class is not a subclass of the interface class`() {
//        container {
//            val t =  catchThrowable({ bind(Mapper::class, Repository::class) });
////            val thrown = catchThrowable { bind(Mapper::class, Repository::class) }
//        }
//    }
}

internal interface Repository
internal class Postgres(val mapper: Mapper) : Repository
internal class MongoDB(val mapper: Mapper) : Repository

internal class Mapper

interface TestInterface
