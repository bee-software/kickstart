package kickstart

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.overriding

object EnvironmentFile {

    fun load(name: String): Configuration {
        return ConfigurationProperties.fromResource("etc/$name.properties") overriding
                ConfigurationMap("env" to name) overriding
                ConfigurationProperties.fromResource("etc/default.properties")
    }
}