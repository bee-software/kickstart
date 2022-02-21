package kickstart

import com.vtence.molecule.middlewares.FileServer
import com.vtence.molecule.middlewares.StaticAssets
import java.nio.file.Path


fun assets(root: Path) = StaticAssets(FileServer(root.resolve("assets").toFile())).apply {
    serve("/favicon", "/apple-touch-icon", "/safari-pinned-tab", "/android-chrome")
    serve("/css")
    serve("/fomantic")
}