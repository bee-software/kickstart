package kickstart.storytelling

import kotlin.reflect.KClass
import kotlin.reflect.cast

typealias Performable = (actor: Actor) -> Unit

typealias Verifiable = (actor: Actor) -> Unit

typealias Answerable<A> = (actor: Actor) -> A


class Actor(private val abilities: MutableList<Ability>) {
    private val memory = hashMapOf<Info<*>, Any>()

    fun can(doThings: Ability) {
        abilities += doThings
    }

    inline fun <reified T : Ability> ability(): T? = abilityTo(T::class)

    fun <T : Ability> abilityTo(doThings: KClass<T>): T? {
        return abilities.firstOrNull { it::class == doThings }?.let { doThings.cast(it) }
    }

    fun leave() {
        abilities.forEach { it.drop() }
    }

    fun wasAbleTo(vararg thingsDone: Performable) {
        does(*thingsDone)
    }

    fun has(vararg thingsDone: Performable) {
        does(*thingsDone)
    }

    fun attemptsTo(vararg thingsToDo: Performable) {
        does(*thingsToDo)
    }

    fun does(vararg thingsToDo: Performable) {
        thingsToDo.forEach { apply(it) }
    }

    fun seesThat(vararg thingsToCheck: Verifiable) {
        checks(*thingsToCheck)
    }

    fun checks(vararg thingsToCheck: Verifiable) {
        thingsToCheck.forEach { apply(it) }
    }

    fun <ABOUT : Any> wantsToKnow(answer: Answerable<ABOUT>): ABOUT {
        return answer(this)
    }

    fun <ABOUT : Any> remembers(what: Info<ABOUT>, answer: Answerable<ABOUT>) {
        remembers(what, wantsToKnow(answer))
    }

    fun <ABOUT : Any> remembers(what: Info<ABOUT>, content: ABOUT) {
        memory[what] = content
    }

    fun <ABOUT : Any> recalls(what: Info<ABOUT>): ABOUT? {
        return memory[what]?.let { what.cast(it) }
    }
}


fun actorAbleTo(vararg abilities: Ability) = Actor(abilities.toMutableList())