package kickstart.security

import com.vtence.kabinet.StatementExecutor
import kickstart.Views
import kickstart.routes

class SecurityModule(db: StatementExecutor, views: Views) {
    private val users = UsersDatabase(db)
    private val authenticator = EmailPasswordAuthenticator(users)

    private val sessions = SessionsController(authenticator, views.named("sessions/new"))

    val routes = routes {
        get("/login").to { sessions.new(it) }
        post("/login").to { sessions.create(it) }
        delete("/logout").to { sessions.delete(it) }
    }
}