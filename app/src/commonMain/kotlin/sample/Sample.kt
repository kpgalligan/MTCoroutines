package sample

import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.isFrozen
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
expect val workerDispatcher: CoroutineDispatcher

expect fun Throwable.printMe()

class DbModel(private val dbId: Int) {
    fun showDbStuff() = mainScope.launch {
        val sd = loadDbInfo(dbId)
        println(sd)
    }

    private suspend fun loadDbInfo(id: Int) =
        withContext(workerDispatcher) {
            DB.find(id)
        }

    init {
        ensureNeverFrozen()
    }
}


val mainScope = ModelScope(Dispatchers.Main)

object DB {
    fun find(id: Int): SomeData = SomeData("id $id", id)
}

class GoModel(
) {
    private val scope = ModelScope(Dispatchers.Main)

    init {
        ensureNeverFrozen()
    }

    suspend fun printStuffBg() {
        val someData = SomeData("Hello 🐶🐶", 2)
        withContext(workerDispatcher) {
            println(someData)
        }
    }

    suspend fun printStuffForeground() {
        val someData = SomeData("Hello 🐶🐶", 2)

        val someMoreData = withContext(workerDispatcher) {
            SomeData("${someData.s}, Dogs!", someData.i + 1)
        }

        println(someMoreData)
        println("I am frozen? ${someMoreData.isFrozen()}")
    }

    fun forIOS() = scope.launch {
        printStuffForeground()
    }

    fun showError(t: Throwable) {
        println("Exception $t")
    }

    fun onDestroy() {
        scope.job.cancel()
    }
}

class ModelScope(private val mainContext: CoroutineContext) : CoroutineScope {
    internal val job = Job()
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        println("error $throwable")
        throwable.printMe()
    }

    override val coroutineContext: CoroutineContext
        get() = mainContext + job + exceptionHandler
}

data class SomeData(val s: String, val i: Int)