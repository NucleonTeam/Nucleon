package nucleon.event

class FilterChain<T : Event>(
    val eventClass: Class<out T>,
    val parent: FilterChain<in T>?
) {

    private val filters: MutableList<FilterAware<T>> = ArrayList()

    fun register(filter: FilterAware<T>) {
        filters.add(filter)
        filters.sortByDescending { it.priority }
    }

    fun execute(event: Event) {
        val genericEvent = eventClass.cast(event)

        for (filter in filters) {
            if (!genericEvent.cancelled || filter.handleCancelled) {
                filter.filter.execute(genericEvent)
            }
        }
    }
}