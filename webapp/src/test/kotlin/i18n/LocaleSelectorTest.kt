package kickstart.i18n

import com.vtence.molecule.Application
import com.vtence.molecule.Request
import com.vtence.molecule.Response
import com.vtence.molecule.http.HttpMethod
import com.vtence.molecule.lib.CookieJar
import com.vtence.molecule.testing.CookieJarAssert.assertThat
import com.vtence.molecule.testing.RequestAssert.assertThat
import com.vtence.molecule.testing.ResponseAssert.assertThat

import kickstart.attribute
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Nested

import kotlin.test.Test
import kotlin.test.BeforeTest
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.test.assertFails

class LocaleSelectorTest {
    val selector = LocaleSelector.usingDefaultLocale("en-US")

    val cookies = CookieJar()
    val request = Request.get("/")

    @BeforeTest
    fun `attach cookie jar`() {
        cookies.bind(request)
    }

    @Nested
    inner class RequestThatModifiesState {
        val app = selector
            .alsoSupporting("fr-CA")
            .then { request ->
                val locale = request.attribute<Locale>()
                Response.ok().done("Forwarded with locale $locale")
            }

        @Test
        fun `is forwarded down the chain`() {
            listOf(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE).forEach {
                val response = app.handle(request.method(it))

                assertForwarded(response)
            }
        }

        @Test
        fun `reads locale cookie`() {
            cookies.add("locale", "fr-CA")

            val response = app.handle(request.method(HttpMethod.POST))
            assertForwarded(response)
            assertThat(response).hasBodyText(endsWith("fr_CA"))
        }

        @Test
        fun `uses negotiated locale otherwise`() {
            val response = app.handle(request.method(HttpMethod.POST))
            assertForwarded(response)
            assertThat(response).hasBodyText(endsWith("en_US"))
        }

        private fun assertForwarded(response: Response) {
            assertThat(response)
                .hasStatusCode(200)
                .hasBodyText(startsWith("Forwarded"))
        }
    }

    @Nested
    inner class RequestToLocalizedResource {
        val ninetyDays = TimeUnit.DAYS.toSeconds(90).toInt()

        val app = Application { request ->
            val locale: Locale = request.attribute()
            Response.ok().done("${request.uri()} ($locale)")
        }

        @Test
        fun `forwards if locale supported`() {
            val response = selector.alsoSupporting("fr")
                .then(app)
                .handle(request.path("/fr/resource"))

            assertThat(response)
                .hasStatusCode(200)
                .hasBodyText("/resource (fr)")
        }

        @Test
        fun `forwards in case we support a locale with same requested language`() {
            val response = selector
                .alsoSupporting("fr-CA")
                .then(app)
                .handle(request.path("/fr"))

            assertThat(response)
                .hasStatusCode(200)
                .hasBodyText("/ (fr_CA)")
        }

        @Test
        fun `redirects if locale is not supported`() {
            val response = selector.then(app)
                .handle(request.path("/de/resource"))

            assertThat(response)
                .isRedirectedTo("/en/de/resource")
                .isDone
        }

        @Test
        fun `overrides cookie value with negotiated locale`() {
            cookies.add("locale", "fr-FR")

            selector.alsoSupporting("fr-CA")
                .then(app)
                .handle(request.path("/fr"))

            assertThat(cookies)
                .hasCookie("locale")
                .hasValue("fr-CA")
                .hasMaxAge(ninetyDays)
        }

    }

    @Nested
    inner class RequestToNonLocalizedResource {
        @Nested
        inner class WithALanguageCookie {
            val ok = Application { Response.ok() }

            @Test
            fun `redirects to negotiated locale if supported`() {
                cookies.add("locale", "fr-FR")

                val response = selector
                    .alsoSupporting("fr-CA")
                    .then(ok)
                    .handle(request.path("/resource"))

                assertThat(response)
                    .isRedirectedTo("/fr/resource")
                    .isDone
            }

            @Test
            fun `falls back to platform default for unsupported cookie values`() {
                cookies.add("locale", "es-ES")

                val response = selector
                    .then(ok)
                    .handle(request.path("/resource"))

                assertThat(response)
                    .isRedirectedTo("/en/resource")
                    .isDone
            }
        }

        @Nested
        inner class WithoutALanguageCookie {
            val app = selector.alsoSupporting("en", "fr")
                .then { Response.ok() }

            @Test
            fun `redirects to the platform language as the default`() {
                val response = app
                    .handle(request.path("/").header("Accept-Language", null))

                assertThat(response)
                    .isRedirectedTo("/en")
                    .isDone
            }

            @Test
            fun `preserves requested target`() {
                val response = app
                    .handle(request.path("/resource"))

                assertThat(response).isRedirectedTo("/en/resource")
            }

            @Test
            fun `redirects to the requested browser language if supported`() {
                val response = app
                    .handle(request.header("Accept-Language", "fr-CA"))

                assertThat(response)
                    .isRedirectedTo("/fr")
                    .isDone
            }

            @Test
            fun `redirects to the highest quality browser language supported`() {
                val response = app
                    .handle(request.header("Accept-Language", "en; q=0.8, fr"))

                assertThat(response)
                    .isRedirectedTo("/fr")
                    .isDone
            }

            @Test
            fun `falls back to default for unsupported browser languages`() {
                val response = app
                    .handle(request.header("Accept-Language", "es-ES"))

                assertThat(response)
                    .isRedirectedTo("/en")
                    .isDone
            }

            @Test
            fun `ignores malformed browser language tags`() {
                val response = app
                    .handle(request.header("Accept-Language", "-fr-"))

                assertThat(response)
                    .isRedirectedTo("/en")
                    .isDone
            }
        }
    }

    @Nested
    inner class OnceDone {
        @Test
        fun `unbinds preferred locale`() {
            val response = selector
                .alsoSupporting("fr")
                .then { Response.ok() }
                .handle(request.path("/fr"))

            assertThat(request).hasAttribute(Locale::class.java, notNullValue())
            response.done()

            assertThat(response).hasStatusCode(200)
            assertThat(request)
                .hasNoAttribute(Locale::class.java)
        }

        @Test
        fun `even when an error occurs`() {
            val app = selector
                .alsoSupporting("fr")
                .then { throw Exception("Error!") }

            assertFails { app.handle(request.path("/fr")) }

            assertThat(request)
                .hasNoAttribute(Locale::class.java)
        }

        @Test
        fun `also in case of deferred error`() {
            val app = selector
                .alsoSupporting("fr")
                .then { Response.ok() }

            val response = app.handle(request.path("/fr"))

            assertThat(request)
                .hasAttribute(Locale::class.java, notNullValue())

            response.done(Exception("Error !"))

            assertThat(request)
                .hasNoAttribute(Locale::class.java)
        }
    }
}