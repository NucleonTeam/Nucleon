package nucleon.event

abstract class Event(
    private val target: EventTarget? = null
) {

    private companion object {
        val RECURSION_DEPTH: ThreadLocal<Int> = ThreadLocal.withInitial { 0 }
        const val RECURSION_DEPTH_LIMIT = 64
    }

    var cancelled = false
        private set

    val name: String = javaClass.simpleName

    fun call() {
        val recursionDepthVal = RECURSION_DEPTH.get()

        if (recursionDepthVal >= RECURSION_DEPTH_LIMIT) {
            throw EventException("Recursion depth exceeded the limit value")
        }

        try {
            RECURSION_DEPTH.set(recursionDepthVal + 1)
            execute()
        } finally {
            RECURSION_DEPTH.set(recursionDepthVal)
        }
    }

    private fun execute() {
        var chain: FilterChain<*> = FilterChainManager.acquireChainFor(javaClass)

        while (true) {
            chain.execute(this)
            chain = chain.parent ?: break
        }

        if (!cancelled) {
            target?.execute()
        }
    }

    fun cancel() {
        check(this is Cancellable) { "Attempt to cancel a non-cancellable event" }
        cancelled = true
    }
}