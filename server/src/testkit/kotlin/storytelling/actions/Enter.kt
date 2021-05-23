package kickstart.storytelling.actions

import kickstart.storytelling.Target
import kickstart.storytelling.browsing.browsingAs

class Enter(private val text: CharSequence) {

    fun into(target: Target): Action = { actor ->
        browsingAs(actor).find(target).run {
            clear()
            type(text)
        }
    }

    companion object {
        fun text(text: CharSequence) = Enter(text)
    }
}

fun enterText(text: CharSequence) = Enter.text(text)