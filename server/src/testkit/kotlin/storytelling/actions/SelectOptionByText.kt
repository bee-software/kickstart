package kickstart.storytelling.actions

import kickstart.storytelling.Target
import kickstart.storytelling.browsing.browsingAs
import kickstart.storytelling.byXPath

class SelectOptionByText(private val optionText: String) {

    fun from(target: Target): Action = { actor ->
        val element = browsingAs(actor).find(optionWithText(optionText) within target)
        if (!isSelected(element)) element.click()
    }

    private fun optionWithText(optionText: String) = byXPath(".//option[text() = '$optionText']")
}


fun selectOptionByText(text: String) = SelectOptionByText(text)
