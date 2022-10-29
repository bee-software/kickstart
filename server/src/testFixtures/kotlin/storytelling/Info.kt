package kickstart.storytelling

import kotlin.reflect.KClass
import kotlin.reflect.cast

class Info<out T: Any>(private val type: KClass<T>) {

    fun cast(value: Any): T = type.cast(value)

    companion object {
        inline fun <reified T: Any> create() = Info(T::class)
    }
}
