package net.gloryx.kda

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import net.dv8tion.jda.api.requests.RestAction
import net.dv8tion.jda.api.requests.restaction.pagination.PaginationAction
import net.dv8tion.jda.api.utils.concurrent.Task
import java.util.*
import java.util.concurrent.CompletionStage
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Awaits the result of this CompletableFuture
 *
 * @return Result
 */
suspend fun <T> CompletionStage<T>.await() = toCompletableFuture().run {
    suspendCancellableCoroutine<T> {
        it.invokeOnCancellation { cancel(true) }
        whenComplete { r, e ->
            when {
                e != null -> it.resumeWithException(e)
                else -> it.resume(r)
            }
        }
    }
}
/**
 * Awaits the result of this RestAction
 *
 * @return Result
 */
suspend fun <T> RestAction<T>.await(): T = submit().await()

fun <T> RestAction<T>.async(): Deferred<T> = jda.scope.async { await() }

/**
 * Awaits the result of this Task
 *
 * @return Result
 */
suspend fun <T> Task<T>.await() = suspendCancellableCoroutine<T> {
    it.invokeOnCancellation { cancel() }
    onSuccess { r -> it.resume(r) }
    onError { e -> it.resumeWithException(e) }
}

/**
 * Converts this PaginationAction to a [Flow]
 *
 * This is the same as
 * ```kotlin
 * flow {
 *   emitAll(produce())
 * }
 * ```
 *
 * @return[Flow] instance
 */
fun <T, M : PaginationAction<T, M>> M.asFlow(): Flow<T> = flow {
    cache(false)
    val queue = LinkedList<T>(await())
    while (queue.isNotEmpty()) {
        while (queue.isNotEmpty()) {
            emit(queue.poll())
        }
        queue.addAll(await())
    }
}
