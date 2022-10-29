package kickstart.security

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present
import com.vtence.konstruct.Provider
import com.vtence.konstruct.a
import com.vtence.konstruct.make
import com.vtence.konstruct.with
import dev.minutest.*
import dev.minutest.junit.JUnit5Minutests
import kickstart.db.DatabaseFixture
import kickstart.security.UserMaker.password
import kickstart.security.UserMaker.user
import kickstart.security.UserMaker.username
import org.junit.jupiter.api.assertThrows
import security.UserThat.hasSameStateAs
import security.UserThat.hasUsername
import java.sql.SQLException


class Fixture : DatabaseFixture() {
    val users = UsersDatabase(executor)

    fun persisted(user: User): User {
        return transaction {
            users.add(user)
        }
    }

    fun assertCanBePersisted(user: User) {
        assertReloadsWithSameState(persisted(user))
    }

    fun assertReloadsWithSameState(original: User) {
        val persisted: User? = users.findBy(original.username)
        assertThat("persisted entity", persisted, present(hasSameStateAs(original)))
    }

    fun persist(vararg users: Provider<User>) {
        users.forEach { persisted(it()) }
    }

    fun cleanUp() {
        clean(Users)
    }
}


class UsersDatabaseTest : JUnit5Minutests {
    fun tests() = rootContext<Fixture> {
        given {
            Fixture()
        }

        beforeEach {
            cleanUp()
        }

        afterEach {
            dispose()
        }

        context("storing") {
            val samples = make(
                a(user, with(username, "dany@persuaders.com"), with(password, "american")),
                a(user, with(username, "brett@persuaders.com"), with(password, "british")),
            )

            samples.forEach { sample ->
                test("round trips user ${sample.username}") {
                    assertCanBePersisted(sample)
                }
            }

            test("fails if username is already taken") {
                persist(a(user, with(username, "remington.steel@gmail.com")))

                assertThrows<SQLException> { persist(a(user, with(username, "remington.steel@gmail.com"))) }
            }
        }

        context("querying") {
            test("finds user by username") {
                persist(
                    a(user, with(username, "dany@persuaders.com")),
                    a(user, with(username, "brett@persuaders.com")),
                    a(user, with(username, "remington.steel@gmail.com")),
                )
                val match = users.findBy(Username("brett@persuaders.com"))

                assertThat("found", match, present(hasUsername("brett@persuaders.com")))
            }
        }
    }
}