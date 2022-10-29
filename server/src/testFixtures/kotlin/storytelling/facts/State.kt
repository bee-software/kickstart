package kickstart.storytelling.facts

import kickstart.storytelling.Fact
import kickstart.storytelling.Target
import kickstart.storytelling.browsing.browsingAs
import org.hamcrest.Matchers.containsString
import org.hamcrest.Matchers.not

val Target.isHidden: Fact
    get() = { actor -> browsingAs(actor).check(this).isHidden() }


val Target.isShowingOnScreen: Fact
    get() = { actor -> browsingAs(actor).check(this).isShowingOnScreen() }


val Target.isMissing: Fact
    get() = { actor -> browsingAs(actor).check(this).isMissing() }


fun Target.hasClass(cssClass: String): Fact = { actor ->
    browsingAs(actor).check(this).hasAttribute("class", containsString(cssClass))
}


fun Target.hasNoClass(cssClass: String): Fact = { actor ->
    browsingAs(actor).check(this).hasAttribute("class", not(containsString(cssClass)))
}