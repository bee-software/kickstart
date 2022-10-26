package kickstart.storytelling.actions

import kickstart.storytelling.Target
import kickstart.storytelling.browsing.browsingAs

class EnterText(private val text: CharSequence) {

    fun into(target: Target): Action = { actor ->
        browsingAs(actor).find(target).run {
            clear()
            type(text)
        }
    }
}


fun enterText(text: CharSequence) = EnterText(text)