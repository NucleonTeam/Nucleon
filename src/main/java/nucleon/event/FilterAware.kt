package nucleon.event

import nucleon.plugin.NucleonPlugin

class FilterAware<T : Event>(
    val priority: EventPriority,
    val handleCancelled: Boolean,
    val filter: EventFilter<T>,
    val plugin: NucleonPlugin
)