package kickstart

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.present
import com.vtence.molecule.testing.ResponseAssert
import kickstart.ViewAssert.Companion.assertThat

class ViewAssert<T : Any>(private val view: TestView<T>) {

    infix fun renderedWith(model: T) {
        renderedWith(equalTo(model))
    }

    infix fun renderedWith(model: Matcher<T>) {
        assertThat("rendering context", view.context, present(model))
    }

    companion object {
        fun <T : Any> assertThat(view: TestView<T>) = ViewAssert(view)
    }
}

infix fun <T: Any> ResponseAssert.and(view: TestView<T>) = assertThat(view)