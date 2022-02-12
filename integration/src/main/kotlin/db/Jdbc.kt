package kickstart.db

import kickstart.Transactor
import kickstart.UnitOfWork
import java.sql.Connection

class JdbcTransactor(private val connection: Connection) : Transactor {

    override fun <T> invoke(work: UnitOfWork<T>): T {
        try {
            return work().also { connection.commit() }
        } catch (e: Throwable) {
            connection.rollback()
            throw e
        }
    }
}
