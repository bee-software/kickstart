package kickstart.db

import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding
import com.vtence.kabinet.Delete
import com.vtence.kabinet.StatementExecutor
import com.vtence.kabinet.Table
import kickstart.UnitOfWork
import java.util.logging.LogManager
import javax.sql.DataSource


private val env
    get() = System.getProperty("env.name", "test")


private val test =
    ConfigurationProperties.systemProperties() overriding
    EnvironmentVariables() overriding
    ConfigurationProperties.fromResource("etc/$env.properties")



open class DatabaseFixture (
    private val dataSource: DataSource = DataSources.configure(config = test) {
        Logging.silence()
        migrateDatabase(it)
    }
) {
    private val connection by lazy { dataSource.connection }

    val executor get() = StatementExecutor(connection)
    val transactor get() = JdbcTransactor(connection)

    fun <T> transaction(work: UnitOfWork<T>): T {
        return transactor.run { this(work) }
    }

    fun clean(vararg tables: Table) {
        tables.map { delete(it) }
    }

    fun dispose() {
        connection.close()
    }

    private fun delete(table: Table) {
        transaction {
            Delete.from(table).execute(executor)
        }
    }
}


private object Logging {
    fun silence() {
        LogManager.getLogManager().reset()
    }
}

