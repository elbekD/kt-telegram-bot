package com.elbekD.bot.http

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.startCoroutine

public suspend fun <T> CompletableFuture<T>.await(): T = suspendCancellableCoroutine {
    whenComplete { res, ex -> if (ex == null) it.resume(res) else it.resumeWithException(ex) }
}

public fun <T> future(context: CoroutineContext = Dispatchers.IO, block: suspend () -> T): CompletableFuture<T> =
    CompletableFutureCoroutine<T>(context).also { block.startCoroutine(completion = it) }

public class CompletableFutureCoroutine<T>(override val context: CoroutineContext) : CompletableFuture<T>(),
    Continuation<T> {
    override fun resumeWith(result: Result<T>) {
        if (result.isSuccess) {
            complete(result.getOrThrow())
        } else {
            completeExceptionally(result.exceptionOrNull())
        }
    }
}