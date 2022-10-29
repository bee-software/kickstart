package kickstart.db


import com.vtence.kabinet.Delete
import com.vtence.kabinet.StatementExecutor
import com.vtence.kabinet.Table
import kickstart.Transactor


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