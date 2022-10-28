package kickstart.security

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.vtence.konstruct.Provider
import com.vtence.konstruct.a
import com.vtence.konstruct.make
import com.vtence.konstruct.with
import kickstart.db.AbstractDatabaseTest
import kickstart.security.UserMaker.password
import kickstart.security.UserMaker.user
import kickstart.security.UserMaker.username
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import security.UserThat.hasSameStateAs
import security.UserThat.hasUsername
import java.sql.SQLException

class UsersDatabaseTest : AbstractDatabaseTest() {

    val db = UsersDatabase(executor)

    @BeforeEach
    fun cleanDatabase() {
        clean(Users)
    }

    @Test
    fun `round trips users`() {
        val samples = make(
            a(user, with(username, "dany@persuaders.com"), with(password,"american")),
            a(user, with(username, "brett@persuaders.com"), with(password,"british")),
        )

        samples.forEach {  assertCanBePersisted(it) }
    }

    @Test
    fun `finds user by username`() {
        persist(
            a(user, with(username, "dany@persuaders.com")),
            a(user, with(username, "brett@persuaders.com")),
            a(user, with(username, "remington.steel@gmail.com")),
        )
        val match = db.findBy(Username("brett@persuaders.com"))

        assertThat("found", match, present(hasUsername("brett@persuaders.com")))
    }

    @Test
    fun `refuses to add account if username is already taken`() {
        persist(a(user, with(username, "remington.steel@gmail.com")),)

        assertThrows<SQLException> { persist(a(user, with(username, "remington.steel@gmail.com"))) }
    }

    private fun assertCanBePersisted(user: User) {
        assertReloadsWithSameState(persisted(user))
    }

    private fun assertReloadsWithSameState(original: User) {
        val persisted: User? = db.findBy(original.username)
        assertThat("persisted entity", persisted, present(hasSameStateAs(original)))
    }

    private fun persist(vararg users: Provider<User>) {
        users.forEach { persisted(it()) }
    }

    private fun persisted(user: User): User {
        return transaction {
            db.add(user)
        }
    }
}