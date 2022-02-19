package kickstart.security

import com.vtence.kabinet.StatementExecutor
import com.vtence.kabinet.insert
import com.vtence.kabinet.selectWhere
import kickstart.db.eq

class UsersDatabase(private val db: StatementExecutor) : UserBase {

    fun add(user: User) {
        Users.insert(user.record).execute(db)
    }

    override fun findBy(username: Username): User? {
        return Users
            .selectWhere(Users.username eq username.value)
            .firstOrNull(db) { it.user }
    }
}
