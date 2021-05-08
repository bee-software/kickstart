package kickstart.storytelling

class Info<out T>(private val type: Class<T>) {

    fun cast(value: Any) = type.cast(value)

    companion object {
        inline fun <reified T> create() = Info(T::class.java)
    }
}
