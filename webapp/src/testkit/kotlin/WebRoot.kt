package kickstart

import java.nio.file.Path
import java.nio.file.Paths

object WebRoot {
    val location: Path
        get() = Paths.get(System.getProperty("www.root", "src/www"))
}