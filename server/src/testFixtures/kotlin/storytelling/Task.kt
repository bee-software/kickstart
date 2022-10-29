package kickstart.storytelling

typealias Task = Performable

inline fun <P, R> on(page: P, actions: P.() -> R): R {
    return page.actions()
}
