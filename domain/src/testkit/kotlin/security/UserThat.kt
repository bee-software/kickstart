package security

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.has
import kickstart.security.User
import kickstart.security.Username

object UserThat {

    fun hasUsername(username: String) = hasUsername(equalTo(Username(username)))

    fun hasUsername(matching: Matcher<Username>) = has(User::username, matching)
}