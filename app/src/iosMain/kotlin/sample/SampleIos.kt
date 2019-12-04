package sample

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.newSingleThreadContext
import platform.Foundation.NSThread
import kotlin.native.concurrent.SharedImmutable
import kotlin.native.concurrent.ensureNeverFrozen
import kotlin.native.concurrent.isFrozen

@SharedImmutable
actual val workerDispatcher: CoroutineDispatcher = newSingleThreadContext("mr background")

actual fun Throwable.printMe() {
    printStackTrace()
}