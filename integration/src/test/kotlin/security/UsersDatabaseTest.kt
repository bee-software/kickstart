package kickstart.security

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import kickstart.db.AbstractDatabaseTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import security.UserThat.hasSameStateAs
import security.UserThat.hasUsername
import security.Users
import security.UsersDatabase
import java.sql.SQLException

class UsersDatabaseTest : AbstractDatabaseTest() {

    val db = UsersDatabase(executor)

    @BeforeEach
    fun cleanDatabase() {
        clean(Users)
    }

    @Test
    fun `round trips users`() {
        val samples = listOf(
            user { username = "dany@persuaders.com"; password = "american" },
            user { username = "brett@persuaders.com"; password = "british" }
        )

        samples.forEach {  assertCanBePersisted(it) }
    }

    @Test
    fun `finds user by username`() {
        persist(
            user { username = "dany@persuaders.com" },
            user { username = "brett@persuaders.com" },
            user { username = "remington.steel@gmail.com"}
        )
        val match = db.findBy(Username("brett@persuaders.com"))

        assertThat("found", match, present(hasUsername("brett@persuaders.com")))
    }

    @Test
    fun `refuses to add account if username is already taken`() {
        persist(user { username = "remington.steel@gmail.com" })

        assertThrows<SQLException> { db.add(user { username = "remington.steel@gmail.com" }.build()) }
    }

    private fun assertCanBePersisted(user: UserBuilder) {
        assertReloadsWithSameState(persisted(user))
    }

    private fun assertReloadsWithSameState(original: User) {
        val persisted: User? = db.findBy(original.username)
        assertThat("persisted entity", persisted, present(hasSameStateAs(original)))
    }

    private fun persist(vararg users: UserBuilder) {
        users.forEach { persisted(it) }
    }

    private fun persisted(user: UserBuilder): User {
        return transaction {
            user.build().also { db.add(it) }
        }
    }
}