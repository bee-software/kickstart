package kickstart.i18n

import java.nio.file.Path
import java.nio.file.Paths
import java.text.MessageFormat
import java.util.*


class MessageBundle(
    private val bundle: ResourceBundle,
    private val location: Location = Location.intrinsic()
) : Messages {

    override fun interpolate(key: String, vararg args: Any): String {
        val formatter = MessageFormat(bundle.getString(key), bundle.locale)
        return formatter.format(args)
    }

    override fun searchPath(key: String) = listOf(MessageLocation(key, location))

    companion object {
        fun load(path: Path, locale: Locale): MessageBundle {
            val bundle = ResourceBundle.getBundle(path.toString(), locale)
            return MessageBundle(bundle, Location(bundle.description, path.toUri()))
        }
    }
}

val ResourceBundle.description get() =
    "bundle $baseBundleName in ${locale.toLanguageTag()}"

val <T> List<T>.head: T
    get() = first()

val <T> List<T>.tail: List<T>
    get() = drop(1)


class BundledMessages(private val root: Path) {
    fun at(path: Path): BundledMessages = BundledMessages(resolve(path))

    fun load(path: Path, locale: Locale): Messages {
        val loadPaths = path.runningFold(root) { dir, name -> dir.resolve(name) }.asReversed()

        return loadPaths.tail.fold(loadBundle(loadPaths.head, locale)) { composition, dir ->
            composition + loadBundle(dir.resolve("defaults"), locale)
        }
    }

    private fun resolve(path: Path) = root.resolve(path)

    companion object {
        fun rootedAt(path: String) = BundledMessages(Paths.get(path))
    }
}

fun BundledMessages.loadBundle(path: String, name: String, locale: Locale) =
    at(Paths.get(path)).load(Paths.get(name), locale)


private fun loadBundle(path: Path, locale: Locale): Messages {
    return runCatching {
        MessageBundle.load(path, locale)
    }.getOrElse {
        MessageBundle(EmptyResourceBundle,
            Location("inexistant bundle $path in ${locale.toLanguageTag()}"))
    }
}


object EmptyResourceBundle : ResourceBundle() {
    override fun handleGetObject(key: String) = null

    override fun getKeys(): Enumeration<String> = Collections.emptyEnumeration()
}
