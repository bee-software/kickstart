package kickstart.i18n

fun interface Messages {
    fun interpolate(key: String, vararg args: Any): String
}
