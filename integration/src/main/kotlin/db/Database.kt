package kickstart.db

import com.natpryce.konfig.*
import com.vtence.kabinet.AutoSelectDataSource
import kickstart.db.DatabaseConfiguration.db.driver
import kickstart.db.DatabaseConfiguration.db.host
import kickstart.db.DatabaseConfiguration.db.migrate
import kickstart.db.DatabaseConfiguration.db.name
import kickstart.db.DatabaseConfiguration.db.password
import kickstart.db.DatabaseConfiguration.db.port
import kickstart.db.DatabaseConfiguration.db.user
import org.flywaydb.core.Flyway
import javax.sql.DataSource


object DatabaseConfiguration {
    object db : PropertyGroup() {
        val driver by stringType
        val host by stringType
        val port by intType
        val name by stringType
        val user by stringType
        val password by stringType
        val migrate by booleanType
    }
}

object DataSources {
    fun configure(config: Configuration, prepare: (DataSource) -> Unit = {}): DataSource {
        return AutoSelectDataSource(
            "jdbc:${config[driver]}://${config[host]}:${config[port]}/${config[name]}",
            config[user],
            config[password],
            autoCommit = false
        ).also(prepare)
    }
}

private val flyway = Flyway.configure()
    .sqlMigrationPrefix("")
    .sqlMigrationSeparator("_")
    .placeholderReplacement(false)
    .table("schema_history")


fun migrateDatabase(dataSource: DataSource) {
    flyway.dataSource(dataSource).load().migrate()
}