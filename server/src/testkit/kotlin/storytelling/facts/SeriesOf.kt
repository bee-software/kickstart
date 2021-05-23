package kickstart.storytelling.facts

import kickstart.storytelling.Actor
import kickstart.storytelling.Fact

class SeriesOf(private val facts: List<Fact>) : Fact {

    override fun invoke(actor: Actor) = facts.forEach { actor.checks(it) }

    infix fun then(other: Fact) = SeriesOf(facts + other)

    companion object {
        fun facts(vararg facts: Fact) = SeriesOf(facts.toList())

        fun first(fact: Fact) = facts(fact)
    }
}

fun seriesOf(vararg facts: Fact) = SeriesOf.facts(*facts)
