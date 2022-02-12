package kickstart;


interface Builder<out T : Any> {
    fun build(): T
}

fun <T : Any> Iterable<Builder<T>>.build(): Collection<T> = map { it.build() }

fun <T : Any> build(vararg items: Builder<T>): Collection<T> = items.map { it.build() }
