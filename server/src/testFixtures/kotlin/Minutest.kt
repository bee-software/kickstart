package kickstart

import dev.minutest.Annotatable
import dev.minutest.TestContextBuilder
import dev.minutest.test


fun <PF, F> TestContextBuilder<PF, F>.scenario(name: String, f: F.(fixture: F) -> Unit): Annotatable<F> = test(name, f)
