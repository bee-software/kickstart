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

    fun <T : Ability> abilityTo(doThings: KClass<T>): T? {
        return abilities.firstOrNull { it::class == doThings }?.let { doThings.cast(it) }
    }

    fun leave() = abilities.forEach { it.drop() }

    fun wasAbleTo(vararg thingsDone: Performable) = does(*thingsDone)

    fun has(vararg thingsDone: Performable) = does(*thingsDone)

    fun attemptsTo(vararg thingsToDo: Performable) = does(*thingsToDo)

    fun does(vararg thingsToDo: Performable) = thingsToDo.forEach { apply(it) }

    fun seesThat(vararg thingsToCheck: Verifiable) = checks(*thingsToCheck)

    fun checks(vararg thingsToCheck: Verifiable) = thingsToCheck.forEach { apply(it) }

    fun <ABOUT : Any> wantsToKnow(answer: Answerable<ABOUT>) = answer(this)

    fun <ABOUT : Any> remembers(what: Info<ABOUT>, answer: Answerable<ABOUT>) = remembers(what, wantsToKnow(answer))

    fun <ABOUT : Any> remembers(what: Info<ABOUT>, content: ABOUT) = memory.set(what, content)

    fun <ABOUT : Any> recalls(what: Info<ABOUT>): ABOUT? = memory[what]?.let { what.cast(it) }

    companion object {
        fun ableTo(vararg abilities: Ability) = Actor(abilities.toMutableList())
    }
}