package kickstart

import com.natpryce.konfig.Configuration
import com.vtence.kabinet.StatementExecutor
import com.vtence.molecule.Request
import kickstart.db.DataSources
import kickstart.security.EmailPasswordAuthenticator
import kickstart.security.UsersDatabase


class ApplicationContext private constructor(val config: Configuration) {
    val dataSource = DataSources.configure(config)

    fun contextualize(request: Request) = RequestContext(request)

    companion object {
        fun load(config: Configuration) = ApplicationContext(config)
    }
}


class RequestContext(request: Request) {
    private val db = StatementExecutor(request.attribute())

    val users = UsersDatabase(db)
    val authenticator = EmailPasswordAuthenticator(users)
}
