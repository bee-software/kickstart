package kickstart

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import it.skrape.selects.DocElement


fun errorLabelFor(name: String) = ".field.error label[for=$name] ~ .red.label"

val DocElement.value get() = attribute("value")


infix fun <T> T.should(match: Matcher<T>) =
    assertThat(this, present(match))

infix fun <T> T.shouldBe(value: T) = this should equalTo(value)


