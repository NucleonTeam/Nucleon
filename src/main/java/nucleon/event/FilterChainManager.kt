package nucleon.event

import java.lang.reflect.Modifier

object FilterChainManager {

    private fun isHandleable(type: Class<out Event>) =
        !Modifier.isAbstract(type.modifiers) || type.isAnnotationPresent(AllowHandle::class.java)

    @Suppress("UNCHECKED_CAST")
    private fun resolveNearestHandleableParent(baseType: Class<out Event>): Class<out Event>? {
        var superType = baseType.superclass
        while (superType != Event::class.java) {
            if (isHandleable(superType as Class<out Event>)) {
                return superType
            }
            superType = superType.superclass
        }
        return null
    }

    private val type2chainMap: MutableMap<Class<out Event>, FilterChain<*>> = HashMap()

    @Suppress("UNCHECKED_CAST")
    fun <T : Event> acquireChainFor(eventType: Class<out T>): FilterChain<T> {
        if (type2chainMap.contains(eventType)) {
            return type2chainMap[eventType] as FilterChain<T>
        }
        val parent = resolveNearestHandleableParent(eventType)
        val chain = FilterChain(eventType, if (parent != null) acquireChainFor(parent) else null)
        type2chainMap[eventType] = chain
        return chain
    }
}