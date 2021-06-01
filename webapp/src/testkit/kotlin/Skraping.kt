package kickstart

import it.skrape.selects.DocElement


fun errorLabelFor(name: String) = ".field.error label[for=$name] ~ .red.label"

val DocElement.value get() = attribute("value")
