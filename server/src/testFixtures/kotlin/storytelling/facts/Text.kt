package kickstart.storytelling.facts

import kickstart.storytelling.Fact
import kickstart.storytelling.Target
import kickstart.storytelling.browsing.browsingAs
import org.hamcrest.Matcher

class Text(private val target: Target) {

    fun `is`(text: Matcher<in String>): Fact = { actor ->
        browsingAs(actor).check(target).hasText(text)
    }
}

fun textOf(target: Target) = Text(target)