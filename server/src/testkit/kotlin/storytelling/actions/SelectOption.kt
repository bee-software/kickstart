package kickstart.storytelling.actions

import kickstart.storytelling.Target
import kickstart.storytelling.browsing.browsingAs

class SelectOption(private val optionText: String) {

    fun from(target: Target): Action = { actor ->
        val element = browsingAs(actor).find(optionWithText(optionText) within target)
        if (!isSelected(element)) element.click()
    }

    private fun optionWithText(optionText: String) = Target.byXPath(".//option[text() = '$optionText']")

    companion object {
        fun byText(text: String) = SelectOption(text)
    }
}

fun selectOptionByText(text: String) = SelectOption.byText(text)
