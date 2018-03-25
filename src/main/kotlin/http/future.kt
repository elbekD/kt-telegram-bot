package http

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.suspendCancellableCoroutine
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.startCoroutine

suspend fun <T> CompletableFuture<T>.await(): T = suspendCancellableCoroutine<T> {
    whenComplete { res, exp -> if (exp == null) it.resume(res) else it.resumeWithException(exp) }
}

fun <T> future(context: CoroutineContext = CommonPool, block: suspend () -> T): CompletableFuture<T> =
        CompletableFutureCoroutine<T>(context).also { block.startCoroutine(completion = it) }

class CompletableFutureCoroutine<T>(override val context: CoroutineContext) : CompletableFuture<T>(), Continuation<T> {
    override fun resume(value: T) {
        complete(value)
    }

    override fun resumeWithException(exception: Throwable) {
        completeExceptionally(exception)
    }
}