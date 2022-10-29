package kickstart.storytelling.facts

import kickstart.storytelling.Actor
import kickstart.storytelling.Fact


class SeriesOfFacts(private val facts: List<Fact>) : Fact {

    override fun invoke(actor: Actor) {
        facts.forEach { actor.checks(it) }
    }

    infix fun then(other: Fact) = SeriesOfFacts(facts + other)
}


fun seriesOf(vararg facts: Fact) = SeriesOfFacts(facts.toList())
