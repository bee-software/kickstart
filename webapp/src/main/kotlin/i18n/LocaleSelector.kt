package kickstart.i18n

import com.vtence.molecule.Application
import com.vtence.molecule.Middleware
import com.vtence.molecule.Request
import com.vtence.molecule.Response
import com.vtence.molecule.http.AcceptLanguage
import com.vtence.molecule.http.Cookie
import com.vtence.molecule.http.HttpMethod.*
import com.vtence.molecule.lib.CookieJar
import java.util.*
import java.util.concurrent.TimeUnit

class LocaleSelector private constructor(
    private val defaultLocale: Locale,
    private val supportedLocales: Set<Locale> = setOf()
) : Middleware {

    fun alsoSupporting(vararg locales: Locale) = alsoSupporting(locales.toSet())

    fun alsoSupporting(locales: Iterable<Locale>) = LocaleSelector(defaultLocale, supportedLocales + locales)

    override fun then(next: Application): Application {
        return Application { request ->
            if (request.method() in listOf(POST, PUT, DELETE)) {
                request.attribute(Locale::class.java, negotiatedLocaleOf(request))
                return@Application next.handle(request)
            }

            val locale = figureOutLocaleOf(request) ?: return@Application Response.redirect(negotiatedUri(request)).done()

            request.setCookieLocale(locale, duration = FOR_3_MONTHS)
            request.attribute(Locale::class.java, locale)

            try {
                return@Application next.handle(request).whenComplete { _, _ -> unbindLocaleFrom(request) }
            } catch (t: Throwable) {
                unbindLocaleFrom(request)
                throw t
            }
        }
    }

    private fun unbindLocaleFrom(request: Request) = request.removeAttribute(Locale::class.java)

    private fun figureOutLocaleOf(request: Request): Locale? {
        return LANGUAGE_URI.find(request.path().toString())?.let {
            val (language, target) = it.destructured

            selectBest(Locale.forLanguageTag(language))?.also {
                request.path(target.ifEmpty { "/" })
            }
        }
    }

    private fun selectBest(locale: Locale): Locale? = when (locale) {
        in supportedLocales -> locale
        else -> supportedLocales.firstOrNull { it.language == locale.language }
    }

    private fun negotiatedUri(request: Request): String {
        return "/${negotiatedLocaleOf(request).language + request.path()}".removeSuffix("/")
    }	

    private fun negotiatedLocaleOf(request: Request): Locale {
        return readCookieLocale(request) ?: readBrowserLocale(request) ?: defaultLocale
    }

    private fun readCookieLocale(request: Request): Locale? {
        val cookies = openCookieJar(request)
        val locale = cookies[LOCALE_COOKIE]
        return locale?.let { selectBest(Locale.forLanguageTag(it.value())) }
    }

    private fun openCookieJar(request: Request): CookieJar {
        return CookieJar.get(request) ?: throw IllegalStateException("Cookie jar not found")
    }

    private fun readBrowserLocale(request: Request): Locale? {
        val languages = AcceptLanguage.of(request)
        return languages.selectBest(supportedLocales)
    }

    private fun Request.setCookieLocale(locale: Locale, duration: Int): Cookie {
        val cookies = openCookieJar(this)
        return cookies.add(LOCALE_COOKIE, locale.toLanguageTag()).maxAge(duration)
    }

    companion object {
        private const val LOCALE_COOKIE = "locale"
        private val LANGUAGE_URI = Regex("""/(\w{2})(/.*)?""")
        private val FOR_3_MONTHS = TimeUnit.DAYS.toSeconds(90).toInt()

        fun usingDefaultLocale(locale: String): LocaleSelector {
            return usingDefaultLocale(Locale.forLanguageTag(locale))
        }

        fun usingDefaultLocale(locale: Locale = Locale.getDefault()): LocaleSelector {
            return LocaleSelector(locale, setOf(locale))
        }
    }
}

fun LocaleSelector.alsoSupporting(vararg languages: String) =
    alsoSupporting(languages.map { Locale.forLanguageTag(it) })
