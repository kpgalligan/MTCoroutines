package sample

import co.touchlab.stately.collections.frozenHashMap
import co.touchlab.stately.ensureNeverFrozen
import co.touchlab.stately.isFrozen
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlin.native.concurrent.SharedImmutable
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

expect class Sample() {
    fun checkMe(): Int
}

expect object Platform {
    val name: String
}

fun hello(): String = "Hello from ${Platform.name}"

class Proxy {
    fun proxyHello() = hello()
}

fun main() {
    println(hello())
}

data class HeyResult(val s:String, val i:Int)

@SharedImmutable
expect val myBackgroundDispatcher:CoroutineDispatcher

@SharedImmutable
expect val stateDispatcher:CoroutineDispatcher
expect fun Throwable.printMe()

class GoModel(
)  {

    class ModelScope(private val mainContext: CoroutineContext) : CoroutineScope {
        internal val job = Job()
        private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            println("error $throwable")
            throwable.printMe()
        }

        override val coroutineContext: CoroutineContext
            get() = mainContext + job + exceptionHandler
    }

    private val scope = ModelScope(Dispatchers.Main)
    init {
        ensureNeverFrozen()
    }

    @UseExperimental(ExperimentalTime::class)
    fun doStuffBlocking() {
        val imap = BlockingMap<String, SomeData>()
        val duration = measureTime {
            repeat(100_000) {
                imap.put("ttt $it", SomeData("jjj $it"))
            }

            /*repeat(100_000) {
                val hi = get("ttt $it")
                if (it % 10_000 == 0) {
                    println("val $hi, isFrozen ${hi.isFrozen()}")
                }
            }*/
        }

        println("Blocking duation ${duration} count ${imap.size()}")

        imap.clear()

        val coroutineBatchduration = measureTime {
            repeat(100) {outerLoop ->
                val dataMap = mutableMapOf<String, SomeData>()
                repeat(1000){
                    val index = (outerLoop * 1000) + it
                    dataMap.put("ttt $index", SomeData("jjj $index"))
                }
                imap.putMap(dataMap)
            }

            /*repeat(100_000) {
                val hi = get("ttt $it")
                if (it % 10_000 == 0) {
                    println("val $hi, isFrozen ${hi.isFrozen()}")
                }
            }*/
        }

        println("Blocking batch duation ${coroutineBatchduration} count ${imap.size()}")
    }

    @UseExperimental(ExperimentalTime::class)
    fun doStuff(block:(String)->Unit) = scope.launch {

        val imap = IsoMap<String, SomeData>(createState { mutableMapOf<String, SomeData>() })
        val duration = measureTime {
            repeat(100_000) {
                imap.put("ttt $it", SomeData("jjj $it"))
            }
        }

        println("Coroutine duation ${duration} count ${imap.size()}")

        imap.clear()

        val coroutineBatchduration = measureTime {
            repeat(100) {outerLoop ->
                val dataMap = mutableMapOf<String, SomeData>()
                repeat(1000){
                    val index = (outerLoop * 1000) + it
                    dataMap.put("ttt $index", SomeData("jjj $index"))
                }
                imap.putMap(dataMap)
            }
        }

        println("Coroutine batch duation ${coroutineBatchduration} count ${imap.size()}")

        imap.clear()

        val m = mutableMapOf<String, SomeData>()
        val mutableDuration = measureTime {
            repeat(100_000) {
                m.put("ttt $it", SomeData("jjj $it"))
            }
        }

        println("Mutable duation ${mutableDuration} count ${m.size}")

        val fm = frozenHashMap<String, SomeData>()
        val frozenDuration = measureTime {
            repeat(100_000) {
                fm.put("ttt $it", SomeData("jjj $it"))
            }
        }

        println("Frozen duation ${frozenDuration} count ${fm.size}")
    }

    fun showError(t: Throwable) {
        println("Exception $t")
    }

    fun onDestroy() {
        scope.job.cancel()
    }
}