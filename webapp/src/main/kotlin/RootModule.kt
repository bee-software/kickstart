package kickstart

class RootModule(views: Views) {
    private val home = HomeController(views.named("home"))

    val routes = routes {
        get("/").to { home.render(it) }
    }
}