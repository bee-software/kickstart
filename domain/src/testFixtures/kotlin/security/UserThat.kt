package security

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import kickstart.security.User
import kickstart.security.Username

object UserThat {

    fun hasSameStateAs(other: User): Matcher<User> {
        return hasUsername(other.username) and
                hasPasswordHash(other.passwordHash)
    }

    fun hasUsername(username: String) = hasUsername(Username(username))

    fun hasUsername(username: Username) = hasUsername(equalTo(username))

    fun hasUsername(matching: Matcher<Username>) = has(User::username, matching)

    fun hasPasswordHash(hash: String) = hasPasswordHash(equalTo(hash))

    fun hasPasswordHash(matching: Matcher<String>) = has(User::passwordHash, matching)
}