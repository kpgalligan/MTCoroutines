package sample

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import platform.Foundation.NSThread
import kotlin.native.concurrent.SharedImmutable
import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.isFrozen

actual class Sample {
    actual fun checkMe() = 7
}

actual object Platform {
    actual val name: String = "iOS"
}

@SharedImmutable
actual val myBackgroundDispatcher: CoroutineDispatcher = newSingleThreadContext("mr background")

@SharedImmutable
actual val stateDispatcher: CoroutineDispatcher = newSingleThreadContext("state")

actual fun Throwable.printMe() {
    printStackTrace()
}