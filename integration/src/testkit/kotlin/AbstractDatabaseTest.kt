package kickstart

import com.vtence.kabinet.StatementExecutor
import com.vtence.kabinet.Table
import kickstart.db.DataSources
import kickstart.db.DatabaseCleaner
import kickstart.db.DatabaseMigrator
import kickstart.db.JdbcTransactor
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import java.util.logging.Level
import java.util.logging.LogManager
import java.util.logging.Logger

abstract class AbstractDatabaseTest {

    private val dataSource = DataSources.test(9999)
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

