package kickstart.db


import com.vtence.kabinet.AutoSelectDataSource
import com.vtence.kabinet.Delete
import com.vtence.kabinet.StatementExecutor
import com.vtence.kabinet.Table
import kickstart.Transactor
import org.flywaydb.core.Flyway
import javax.sql.DataSource


object DataSources {
    fun test(port: Int = 5432): DataSource {
        return AutoSelectDataSource("jdbc:postgresql://127.0.0.1:$port/kickstart_test", "test", "test", autoCommit = false)
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