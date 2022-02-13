package kickstart.db

import com.natpryce.konfig.ConfigurationProperties
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding
import com.vtence.kabinet.StatementExecutor
import com.vtence.kabinet.Table
import kickstart.UnitOfWork
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger


private val env
    get() = System.getProperty("env.name", "test")


private val test =
    ConfigurationProperties.systemProperties() overriding
    EnvironmentVariables() overriding
    ConfigurationProperties.fromResource("etc/$env.properties")


abstract class AbstractDatabaseTest {

    private val dataSource = DataSources.configure(config = test)
    private var connection = dataSource.connection
    private val transactor = JdbcTransactor(connection)

    protected val executor = StatementExecutor(connection)
    private val migrator = DatabaseMigrator(dataSource)
    private val cleaner = DatabaseCleaner(transactor, executor)

    @BeforeEach
    fun prepareDatabase() {
        migrator.migrate()
    }

    @AfterEach
    fun closeConnection() {
        connection.close()
    }

    fun clean(vararg tables: Table) {
        cleaner.clean(*tables)
    }

    fun <T> transaction(work: UnitOfWork<T>): T = transactor(work)

    companion object {
        @JvmStatic
        @BeforeAll
        fun silenceLogging() {
            Logging.silence()
        }
    }
}

private object Logging {
    fun silence() {
        LogManager.getLogManager().reset()
        val globalLogger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)
        globalLogger.level = Level.OFF
    }
}

