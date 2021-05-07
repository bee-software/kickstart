package kickstart

import com.natpryce.konfig.*

object Settings {
    val env by stringType
    val quiet by booleanType

    object server : PropertyGroup() {
        val host by stringType
        val port by intType
    }

    object www: PropertyGroup() {
        val root by stringType
    }
}