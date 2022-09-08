package nucleon.event

import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

class MethodEventFilter<T : Event>(
    private val method: Method,
    private val listener: Listener
) : EventFilter<T> {

    override fun execute(event: T) {
        try {
            method.isAccessible = true
            method.invoke(listener, event)
        } catch (e: IllegalAccessException) {
            throw InternalError(e)
        } catch (e: InvocationTargetException) {
            throw EventException("Could not execute the event [" + event.name + "]", e.targetException)
        }
    }
}