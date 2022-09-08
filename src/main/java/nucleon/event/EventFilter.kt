package nucleon.event

fun interface EventFilter<T : Event> {
    fun execute(event: T)
}