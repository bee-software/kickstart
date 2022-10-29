package kickstart.storytelling.actions

import kickstart.storytelling.Actor

class SequenceOfActions(private val actions: List<Action>) : Action {

    override fun invoke(actor: Actor) {
        actions.forEach { actor.does(it) }
    }

    infix fun then(other: Action): SequenceOfActions = SequenceOfActions(actions + other)
}


fun sequenceOf(vararg actions: Action) = SequenceOfActions(actions.toList())
