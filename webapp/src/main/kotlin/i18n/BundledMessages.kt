package kickstart.i18n

import java.nio.file.Path
import java.nio.file.Paths
import java.util.*

class BundledMessages(private val root: Path) {

    fun at(path: Path): BundledMessages = BundledMessages(resolve(path))

    fun load(path: Path, locale: Locale): Messages {
        val messages = compose(loadBundle(resolve(path), locale),
                loadBundle(resolve(path.resolveSibling("defaults")), locale))

        return path.parent?.let { messages + load(it, locale) } ?: messages
    }

    private fun loadBundle(path: Path, locale: Locale): Messages {
        return try {
            MessageBundle.load(path.toString(), locale)
        } catch (e: MissingResourceException) {
            notAvailable(path, locale)
        }
    }

    private fun resolve(path: Path) = root.resolve(path)

    companion object {
        fun rootedAt(path: String) = BundledMessages(Paths.get(path))
    }
}

fun BundledMessages.at(path: String) = at(Paths.get(path))

fun BundledMessages.loadBundle(name: String, locale: Locale) = load(Paths.get(name), locale)


private fun notAvailable(bundlePath: Path, locale: Locale): Messages {
    return Messages { message, _ ->
        throw MissingResourceException(
            "Can't find resource bundle for path $bundlePath and locale $locale",
            bundlePath.toString(),
            message
        )
    }
}
