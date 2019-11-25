package sample

import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.isFrozen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
internal val stateMap = mutableMapOf<Int, Any>()

@ThreadLocal
internal var stateId = 0

expect fun <T> runBlocking(context: CoroutineContext = EmptyCoroutineContext, block: suspend CoroutineScope.() -> T): T

open class IsolateState<T : Any>(private val stateId: Int) {
    internal suspend fun <R> access(block: (T) -> R): R = withContext(stateDispatcher) {
        block(stateMap.get(stateId) as T)
    }
}

fun <T : Any> createStateBlocking(producer: () -> T): Int = runBlocking { createState(producer) }

suspend fun <T : Any> createState(producer: () -> T): Int = withContext(stateDispatcher) {
    val t = producer()
    if (t.isFrozen())
        throw IllegalStateException("Mutable state shouldn't be frozen")
    t.ensureNeverFrozen()
    val newId = stateId++
    stateMap.put(newId, t)
    newId
}

suspend fun removeState(id: Int) = withContext(stateDispatcher) {
    stateMap.remove(id)
}

class IsoMap<K,V>(stateId: Int) : IsolateState<MutableMap<K,V>>(stateId){
    suspend fun clear() = withContext(stateDispatcher) {
        access { it.clear() }
    }

    suspend fun size() = withContext(stateDispatcher) {
        access { it.size }
    }

    suspend fun get(mapKey: K): V? = withContext(stateDispatcher) {
        access { it.get(mapKey) }
    }

    suspend fun put(mapKey: K, data: V) = withContext(stateDispatcher) {
        access { it.put(mapKey, data) }
    }

    suspend fun putMap(map: Map<K, V>) = withContext(stateDispatcher) {
        access { it.putAll(map) }
    }
}

class BlockingMap<K, V>(){
    private val isoMap = IsoMap<K, V>(createStateBlocking { mutableMapOf<String, SomeData>() })

    fun clear() = runBlocking {
        isoMap.clear()
    }

    fun size() = runBlocking {
        isoMap.size()
    }

    fun get(mapKey: K): V? = runBlocking {
        isoMap.get(mapKey)
    }

    fun put(mapKey: K, data: V) = runBlocking {
        isoMap.put(mapKey, data)
    }


    fun putMap(map: Map<K, V>) = runBlocking {
        isoMap.putMap(map)
    }
}

data class SomeData(val s: String)