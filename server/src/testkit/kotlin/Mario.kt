package kickstart

import com.natpryce.hamkrest.MatchResult
import com.natpryce.hamkrest.Matcher
import com.vtence.mario.WebElementDriver
import org.hamcrest.Description

fun <T : Any?> Matcher<T>.asHamcrestMatcher(): org.hamcrest.Matcher<T> {
    return object : org.hamcrest.BaseMatcher<T>() {
        @Suppress("UNCHECKED_CAST")
        override fun matches(item: Any?): Boolean {
            return this@asHamcrestMatcher.invoke(item as T) is MatchResult.Match
        }

        override fun describeTo(description: Description) {
            description.appendText(this@asHamcrestMatcher.description)
        }
    }
}


fun WebElementDriver.hasText(matching: Matcher<String>) = hasText(matching.asHamcrestMatcher())
