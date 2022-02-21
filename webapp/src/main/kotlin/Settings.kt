package kickstart

import com.natpryce.konfig.*
import java.nio.file.Paths
import java.util.*

val localeType = propertyType { ParseResult.Success(Locale.forLanguageTag(it)) }
val pathType = propertyType { ParseResult.Success(Paths.get(it)) }

object Settings {
    val env by stringType

    object server : PropertyGroup() {
        val host by stringType
        val port by intType
        val quiet by booleanType
    }

    object www: PropertyGroup() {
        val root by pathType
        val lang by localeType
    }
}
