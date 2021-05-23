package kickstart.storytelling.actions

import kickstart.storytelling.Actor

class SequenceOf(private val actions: List<Action>) : Action {

    override fun invoke(actor: Actor) = actions.forEach { actor.does(it) }

    infix fun then(other: Action): SequenceOf = SequenceOf(actions + other)

    companion object {
        fun actions(vararg actions: Action) = SequenceOf(actions.toMutableList())

        fun actions(actions: List<Action>) = SequenceOf(actions.toList())
    }
}

fun sequenceOf(vararg actions: Action) = SequenceOf.actions(*actions)
