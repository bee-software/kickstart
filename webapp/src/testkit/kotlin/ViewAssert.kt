package kickstart

import com.natpryce.hamkrest.Matcher
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.present

class ViewAssert<T : Any>(private val view: TestView<T>) {

    fun renderedWith(model: Matcher<T>) {
        assertThat("rendering context", view.context, present(model))
    }

    companion object {
        fun <T : Any> assertThat(view: TestView<T>) = ViewAssert(view)
    }
}