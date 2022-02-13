package kickstart.db


import com.natpryce.konfig.*
import com.vtence.kabinet.AutoSelectDataSource
import com.vtence.kabinet.Delete
import com.vtence.kabinet.StatementExecutor
import com.vtence.kabinet.Table
import kickstart.Transactor
import kickstart.db.DatabaseConfiguration.db.driver
import kickstart.db.DatabaseConfiguration.db.host
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
    }
}

object DataSources {
    fun configure(config: Configuration): DataSource {
        return AutoSelectDataSource(
            "jdbc:${config[driver]}://${config[host]}:${config[port]}/${config[name]}",
            config[user],
            config[password],
            autoCommit = false
        )
    }
}


class DatabaseMigrator(dataSource: DataSource) {
    private val flyway = Flyway
        .configure()
        .sqlMigrationPrefix("")
        .sqlMigrationSeparator("_")
        .dataSource(dataSource)
        .placeholderReplacement(false)
        .table("schema_history")
        .load()

    fun migrate() {
        flyway.migrate()
    }
}

class DatabaseCleaner(private val transactor: Transactor, private val executor: StatementExecutor) {

    fun clean(vararg tables: Table) {
        tables.map { delete(it) }
    }

    private fun delete(table: Table) {
        transactor {
            Delete.from(table).execute(executor)
        }
    }
}